package edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        fab.setVisibility(View.GONE);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

}
