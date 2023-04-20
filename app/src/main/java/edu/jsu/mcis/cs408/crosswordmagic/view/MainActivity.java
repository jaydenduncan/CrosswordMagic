package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.crosswordmagic.DefaultController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.ActivityMainBinding;
import edu.jsu.mcis.cs408.crosswordmagic.model.*;

public class MainActivity extends AppCompatActivity implements AbstractView {

    public static final String TAG = "MainActivity";

    private ActivityMainBinding binding;

    private DefaultController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /* Create Controller and Model */

        controller = new DefaultController();

        PuzzleModel model = new PuzzleModel(new PuzzleDatabaseModel(this, null, null, 1));

        /* Register View(s) and Model(s) with Controller */

        controller.addModel(model);

        controller.addView(this);

        /* Initialize Model */

        model.init();

    }

    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {

        String propertyName = evt.getPropertyName();

        Log.i(TAG, "New " + propertyName + " Value(s) Received");

    }

    public DefaultController getController() {
        return controller;
    }

}