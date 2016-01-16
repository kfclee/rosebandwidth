package edu.rosehulman.jungckjp_leekf.rosebandwidth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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

        mAdapter = new DeviceAdapter(getContext(), recyclerView);
        recyclerView.setAdapter(mAdapter);

        // Inflate the layout for this fragment
        return recyclerView;
    }

}
