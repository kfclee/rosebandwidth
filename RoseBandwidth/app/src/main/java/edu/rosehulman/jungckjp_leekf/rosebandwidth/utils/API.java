package edu.rosehulman.jungckjp_leekf.rosebandwidth.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;

import java.util.ArrayList;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Device;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Usage;

/**
 * Created by jonathan on 1/16/16.
 */
public class API implements ChildEventListener {

    private static API me;

    Firebase mConstantsRef;

    ArrayList<Device> mDevices = new ArrayList<Device>();
    Usage mUsage;
    public static MainActivity mActivity;

    public API(MainActivity mainActivity) throws IOException {
        mActivity = mainActivity;
        mConstantsRef = new Firebase(Constants.CONSTANTS_PATH);
        mConstantsRef.addChildEventListener(this);
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

    public static String getCurrentUser() {
        return PreferenceManager.getDefaultSharedPreferences(mActivity).getString(Constants.USERNAME, "");
    }

    public static void setCurrentUser(String uid) {
        PreferenceManager.getDefaultSharedPreferences(mActivity).edit().putString(Constants.USERNAME, uid).commit();
    }

    public static void removeCurrentUser() {
        PreferenceManager.getDefaultSharedPreferences(mActivity).edit().remove(Constants.USERNAME);
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

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String data_url = dataSnapshot.child("data_url").getValue(String.class);
        PreferenceManager.getDefaultSharedPreferences(mActivity).edit().putString("data_url", data_url).commit();
//        String bandwidth_cap = dataSnapshot.child("bandwidth_cap").getValue(String.class);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//        dataSnapshot("data_url")
        String data_url = dataSnapshot.getValue(String.class);
        PreferenceManager.getDefaultSharedPreferences(mActivity).edit().putString("data_url", data_url).commit();
        Log.d(Constants.TAG, "CHANGED: " + data_url);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {

    }
}
