package edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.IOException;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.API;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsageFragment extends Fragment {
    public DonutProgress mDonut;
    private API mAPI;
    private TextView mStatus;

    public UsageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usage, container, false);
        mDonut = (DonutProgress)view.findViewById(R.id.donut_progress);
        mStatus = (TextView)view.findViewById(R.id.usage_status);

        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        fab.setVisibility(View.GONE);

        try {
            mAPI = API.getInstance((MainActivity)getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(mAPI.getUsage() != null){
            int progress = (int)mAPI.getUsage().getDownload()/80;
            mDonut.setProgress(progress);
            mStatus.setText(mAPI.getUsage().getStatus());
        }


        return view;
    }

}
