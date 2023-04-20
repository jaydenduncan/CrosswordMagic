package edu.jsu.mcis.cs408.crosswordmagic.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TabLayoutAdapter extends FragmentStateAdapter {

    private final ArrayList<Fragment> fragments;

    public TabLayoutAdapter(Fragment fragment, ArrayList<Fragment> fragments) {

        super(fragment);
        this.fragments = fragments;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) { return fragments.get(position); }

    @Override
    public int getItemCount() { return fragments.size(); }

}