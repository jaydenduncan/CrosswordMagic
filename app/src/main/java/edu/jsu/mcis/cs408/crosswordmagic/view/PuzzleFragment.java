package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;

import edu.jsu.mcis.cs408.crosswordmagic.DefaultController;
import edu.jsu.mcis.cs408.crosswordmagic.R;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.FragmentPuzzleBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.Puzzle;

public class PuzzleFragment extends Fragment implements TabFragment, AbstractView {

    public static final String TAG = "PuzzleFragment";

    private String title;

    private DefaultController controller;

    private FragmentPuzzleBinding binding;

    private ArrayList<ArrayList<TextView>> gridLetters;
    private ArrayList<ArrayList<TextView>> gridNumbers;

    /* display geometry values (used for scaling the puzzle to fill the screen */

    private int windowHeightDp, windowWidthDp, windowOverheadDp, numberSize;

    /* "hand-tweaked" padding and scaling values for grid cells, numbers, and letters */

    private final double CELL_PADDING = 0.1;
    private final double LETTTER_SCALE = 0.55;
    private final double NUMBER_SCALE = 0.18;

    private ConstraintSet set;

    public PuzzleFragment() { super(); }

    public PuzzleFragment(String title) {
        super();
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPuzzleBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        /* Get display geometry (height/width and overhead of tab selector) */

        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        windowHeightDp = dm.heightPixels;
        windowWidthDp = dm.widthPixels;

        windowOverheadDp = 0;

        TypedValue tv = new TypedValue();

        if (requireActivity().getTheme().resolveAttribute(androidx.appcompat.R.attr.actionBarSize, tv, true)) {
            windowOverheadDp += TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        @SuppressLint({"InternalInsetResource", "DiscouragedApi"})
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            windowOverheadDp = windowOverheadDp + windowOverheadDp + getResources().getDimensionPixelSize(resourceId);
        }

        /* get controller, register Fragment as a View */

        this.controller = ((MainActivity)getContext()).getController();

        controller.addView(this);

        /* get initial grid contents */

        controller.getGridLetters();
        controller.getGridNumbers();

    }

    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {

        String propertyName = evt.getPropertyName();

        Log.i(TAG, "New " + propertyName + " Value(s) Received");

        if (propertyName.equals(DefaultController.GRID_LETTERS_PROPERTY)) {

            Character[][] letters = (Character[][]) evt.getNewValue();

            if (gridLetters == null) {
                int height = letters.length;
                int width = letters[0].length;
                init(height, width);
            }

            updateGridLetters(letters);

        }

        if (propertyName.equals(DefaultController.GRID_NUMBERS_PROPERTY)) {

            Integer[][] numbers = (Integer[][]) evt.getNewValue();

            if (gridNumbers == null) {
                int height = numbers.length;
                int width = numbers[0].length;
                init(height, width);
            }

            updateGridNumbers(numbers);

        }

        if (propertyName.equals(DefaultController.GUESS_PROPERTY)) {

            Boolean result = (Boolean) evt.getNewValue();

            if (result) {
                (Toast.makeText(binding.getRoot().getContext(), getResources().getText(R.string.guess_correct), Toast.LENGTH_SHORT)).show();
            }
            else {
                (Toast.makeText(binding.getRoot().getContext(), getResources().getText(R.string.guess_incorrect), Toast.LENGTH_SHORT)).show();
            }

        }

        if (propertyName.equals(DefaultController.SOLVED_PROPERTY)) {

            Boolean result = (Boolean) evt.getNewValue();

            if (result) {
                (Toast.makeText(binding.getRoot().getContext(), getResources().getText(R.string.puzzle_solved), Toast.LENGTH_LONG)).show();
            }

        }

    }

    private void init(int height, int width) {

        /* first, initialize View collections and event handler */

        gridLetters = new ArrayList<>();
        gridNumbers = new ArrayList<>();

        SquareClickListener listener = new SquareClickListener();

        for (int i = 0; i < height; ++i) {

            gridLetters.add(new ArrayList<>());

            ArrayList<TextView> row = new ArrayList<>();

            for (int j = 0; j < width; ++j) {
                row.add(null);
            }

            gridNumbers.add(row);

        }

        /* Next, compute grid Geometry (to enlarge grid to fill available space) */

        int gridWidth = Math.max(height, width);

        int squareSize = ( Math.min(windowHeightDp - windowOverheadDp, windowWidthDp) / gridWidth );
        int letterSize = (int)( squareSize * LETTTER_SCALE);
        numberSize = (int)( squareSize * NUMBER_SCALE );

        /* get ConstraintLayout; create and initialize grid */

        ConstraintLayout layout = binding.layoutPuzzle;

        for (int i = 0; i < height; ++i) {

            for (int j = 0; j < width; ++j) {

                TextView square = new TextView(this.getContext());
                square.setId(View.generateViewId());
                square.setTag(i + "," + j);
                square.setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(), R.drawable.closed_square));
                square.setLayoutParams(new ConstraintLayout.LayoutParams(squareSize, squareSize));

                square.setTextSize(letterSize);
                square.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                square.setTextColor(Color.BLACK);
                square.setIncludeFontPadding(false);
                square.setLineSpacing(0, 0);
                square.setPadding(0, (int)(squareSize * CELL_PADDING), 0, 0);
                square.setOnClickListener(listener);

                layout.addView(square);
                gridLetters.get(i).add(square);

            }

        }

        /* create constraints */

        set = new ConstraintSet();
        set.clone(layout);

        /* create grid chains (vertical) */

        int[] current = new int[height];

        for (int i = 0; i < height; ++i) {

            for (int j = 0; j < width; ++j) {

                int id = gridLetters.get(j).get(i).getId();
                current[j] = id;

            }

            set.createVerticalChain(ConstraintSet.PARENT_ID, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, current, null, ConstraintSet.CHAIN_PACKED);

        }

        /* create grid chains (horizontal) */

        current = new int[width];

        for (int i = 0; i < height; ++i) {

            for (int j = 0; j < width; ++j) {

                int id = gridLetters.get(i).get(j).getId();
                current[j] = id;

            }

            set.createHorizontalChain(ConstraintSet.PARENT_ID, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, current, null, ConstraintSet.CHAIN_PACKED);

        }

        /* apply new layout */

        set.applyTo(layout);

    }

    private void updateGridLetters(Character[][] letters) {

        /* update View to reflect new grid contents (as sent from the Model) */

        int height = letters.length;
        int width = letters[0].length;

        for (int i = 0; i < height; ++i) {

            for (int j = 0; j < width; ++j) {

                if (letters[i][j] != Puzzle.BLOCK_CHAR) {

                    openSquare(i, j);
                    gridLetters.get(i).get(j).setText(Character.toString(letters[i][j]));

                }

            }

        }

    }

    private void updateGridNumbers(Integer[][] numbers) {

        /* update View to display box numbers (as sent from the Model) */

        int height = numbers.length;
        int width = numbers[0].length;

        for (int i = 0; i < height; ++i) {

            for (int j = 0; j < width; ++j) {

                if (numbers[i][j] != 0) {

                    addNumber(i, j, numbers[i][j]);

                }

            }

        }

    }

    public void addNumber(int row, int column, int number) {

        /* add number to cell only if there is not already a number in this cell */

        if (gridNumbers.get(row).get(column) == null) {

            /* get ID of corresponding grid cell at given row and column */

            int square = gridLetters.get(row).get(column).getId();

            /* create new TextView for number; add to layout */

            TextView num = new TextView(this.getContext());
            num.setId(View.generateViewId());
            num.setTextSize(numberSize);
            num.setTextColor(Color.BLACK);
            num.setText(String.valueOf(number));

            binding.layoutPuzzle.addView(num);

            /* set constraints (to overlay number TextView over the corresponding cell */

            set.connect(num.getId(), ConstraintSet.TOP, square, ConstraintSet.TOP);
            set.connect(num.getId(), ConstraintSet.LEFT, square, ConstraintSet.LEFT, 4);
            set.connect(num.getId(), ConstraintSet.BOTTOM, square, ConstraintSet.BOTTOM);
            set.connect(num.getId(), ConstraintSet.RIGHT, square, ConstraintSet.RIGHT);

            /* add number TextView to collection */

            gridNumbers.get(row).set(column, num);

            /* apply new layout */

            set.applyTo(binding.layoutPuzzle);

        }

    }

    public void openSquare(int row, int column) {

        /* change cell background from a closed box to an open box */

        TextView square = gridLetters.get(row).get(column);
        square.setBackground(AppCompatResources.getDrawable(binding.getRoot().getContext(), R.drawable.open_square));

    }

    @Override
    public String getTabTitle() { return title; }

    /* click handler for numbered cells */

    private class SquareClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            String[] fields = v.getTag().toString().trim().split(",");
            int row = Integer.parseInt(fields[0]);
            int column = Integer.parseInt(fields[1]);

            if (gridNumbers.get(row).get(column) != null) {

                Integer num = Integer.parseInt(gridNumbers.get(row).get(column).getText().toString());

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(R.string.dialog_title);
                builder.setMessage(R.string.dialog_message);
                final EditText input = new EditText(v.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("GUESS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int i) {
                        String userInput = input.getText().toString();
                        HashMap<String, String> params = new HashMap<>();
                        params.put("num", String.valueOf(num));
                        params.put("guess", userInput.toUpperCase().trim());
                        controller.setGuess(params);
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int i) {
                        String userInput = "";
                        d.cancel();
                    }
                });

                AlertDialog aboutDialog = builder.show();

            }

        }

    }

}
