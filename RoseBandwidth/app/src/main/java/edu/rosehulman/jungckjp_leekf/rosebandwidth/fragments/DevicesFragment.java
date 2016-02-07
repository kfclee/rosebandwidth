package edu.rosehulman.jungckjp_leekf.rosebandwidth.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.adapters.DeviceAdapter;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends Fragment {

    public DeviceAdapter mAdapter;


    public DevicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        getFragmentManager().get
        RecyclerView recyclerView = (RecyclerView)inflater.inflate(R.layout.fragment_devices, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        FloatingActionButton fab = ((MainActivity) getActivity()).getFab();
        fab.setVisibility(View.GONE);

        try {
            mAdapter = new DeviceAdapter(getContext(), recyclerView);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return recyclerView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            mAdapter.notifyDataSetChanged();
//            getCurrentFragment();
//            Log.d(Constants.TAG, "changed");
            return true;
        }
        return true;
    }

    public DeviceAdapter getAdapter(){
        return mAdapter;
    }

}
