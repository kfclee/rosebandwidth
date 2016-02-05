package edu.rosehulman.jungckjp_leekf.rosebandwidth.utils;

import java.io.IOException;

import java.util.ArrayList;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Device;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Usage;

/**
 * Created by jonathan on 1/16/16.
 */
public class API{

    private static API me;

    ArrayList<Device> mDevices = new ArrayList<Device>();
    Usage mUsage;
    public MainActivity mActivity;

    public API(MainActivity mainActivity) throws IOException {
        mActivity = mainActivity;
    }

    public static API getInstance(MainActivity mainActivity) throws IOException {
        if (me == null) {
            me = new API(mainActivity);
        }
        return me;
    }

    public static API createNew(MainActivity mainActivity)throws IOException {
        me = new API(mainActivity);
        return me;
    }

    public Usage getUsage() {
        return mUsage;
    }

    public void setUsage(Usage mUsage) {
        this.mUsage = mUsage;
    }

    public ArrayList<Device> getDevices() {
        return mDevices;
    }

    public void setDevices(ArrayList<Device> mDevices) {
        this.mDevices = mDevices;
    }

    public void getData(){
        new getDataTask(this).execute();
    }
}
