package edu.jsu.mcis.cs408.crosswordmagic.view;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.crosswordmagic.DefaultController;
import edu.jsu.mcis.cs408.crosswordmagic.databinding.FragmentClueBinding;

public class ClueFragment extends Fragment implements TabFragment, AbstractView {

    public static final String TAG = "ClueFragment";

    private String title;

    private DefaultController controller;

    private FragmentClueBinding binding;

    public ClueFragment() { super(); }

    public ClueFragment(String title) {
        super();
        this.title = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentClueBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        /* get controller, register Fragment as a View */

        this.controller = ((MainActivity)getContext()).getController();

        controller.addView(this);

        /* get clues */

        controller.getCluesDown();
        controller.getCluesAcross();

    }

    @Override
    public void modelPropertyChange(final PropertyChangeEvent evt) {

        String propertyName = evt.getPropertyName();

        if (propertyName.equals(DefaultController.CLUES_ACROSS_PROPERTY)) {

            binding.aContainer.setText(evt.getNewValue().toString());
            binding.aContainer.setMovementMethod(new ScrollingMovementMethod());

        }

        else if (propertyName.equals(DefaultController.CLUES_DOWN_PROPERTY)) {

            binding.dContainer.setText(evt.getNewValue().toString());
            binding.dContainer.setMovementMethod(new ScrollingMovementMethod());

        }

    }

    @Override
    public String getTabTitle() { return title; }

}