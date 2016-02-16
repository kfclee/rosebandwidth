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
import java.util.ArrayList;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.adapters.DeviceAdapter;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Usage;
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
    private TextView mUsage;
    private ArrayList<View> mUIObjects;
    private View mProgressSpinner;

    public UsageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usage, container, false);
        mDonut = (DonutProgress)view.findViewById(R.id.donut_progress);
        mStatus = (TextView)view.findViewById(R.id.usage_status);
        mUsage = (TextView) view.findViewById(R.id.usage_number);
        mProgressSpinner = view.findViewById(R.id.login_progress);

        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        fab.setVisibility(View.GONE);

        try {
            mAPI = API.getInstance((MainActivity)getActivity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        mUIObjects = new ArrayList<View>();
        mUIObjects.add(mDonut);
        mUIObjects.add(mStatus);
        mUIObjects.add(mUsage);

        if(mAPI.getUsage() != null){
            int progress = (int)mAPI.getUsage().getDownload()/80;
            mDonut.setProgress(progress);
            Usage usage = mAPI.getUsage();
            mStatus.setText(usage.getStatus());
            mUsage.setText(usage.getDownload() + " MB Downloaded\n" + usage.getUpload() + " MB Uploaded");
        } else {
            updateRefreshUI(false);
        }

//        if(mDonut.getProgress() > 90){
//            mDonut.setFinishedStrokeColor(R.color.colorAccent);
//            mDonut.setTextColor(R.color.colorAccent);
//            mDonut.refreshDrawableState();
//        }else{
//            mDonut.setFinishedStrokeColor(R.color.green);
//            mDonut.setTextColor(R.color.green);
//            mDonut.refreshDrawableState();
//        }

        return view;
    }

    public void updateRefreshUI(boolean hasData) {
        if (hasData) {
            mProgressSpinner.animate().alpha(0.0f).setDuration(500).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mProgressSpinner.setVisibility(View.GONE);
                }
            }).start();
            for (View v : mUIObjects) {
                v.animate().alpha(1.0f).setDuration(500).start();
            }
        } else {
            mProgressSpinner.setVisibility(View.VISIBLE);
            for (View v : mUIObjects) {
                v.setAlpha(0.2f);
            }
        }
    }

    public void notifyDataSetChanged() {
        if(mAPI.getUsage() != null){
            int progress = (int)mAPI.getUsage().getDownload()/80;
            mDonut.setProgress(progress);
            Usage usage = mAPI.getUsage();
            mStatus.setText(usage.getStatus());
            mUsage.setText(usage.getDownload() + " MB Downloaded\n" + usage.getUpload() + " MB Uploaded");
            updateRefreshUI(true);
        }
    }

}
