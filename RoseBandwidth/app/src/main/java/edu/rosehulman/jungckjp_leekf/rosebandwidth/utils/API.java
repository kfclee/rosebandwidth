package edu.rosehulman.jungckjp_leekf.rosebandwidth.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.io.IOException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Alarm;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Device;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.models.Usage;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.services.AlarmBroadcastReceiver;

/**
 * Created by jonathan on 1/16/16.
 */
public class API implements ChildEventListener {

    private static API me;
    private final Firebase mAlarmsRef;

    Firebase mConstantsRef;

    ArrayList<Device> mDevices = new ArrayList<Device>();
    ArrayList<String> mAlarms = new ArrayList<>();
    Usage mUsage;
    public static MainActivity mActivity;

    public API(MainActivity mainActivity) throws IOException {
        mActivity = mainActivity;
        mConstantsRef = new Firebase(Constants.CONSTANTS_PATH);
        mConstantsRef.addChildEventListener(this);

        mAlarmsRef = new Firebase(Constants.ALARMS_PATH);

        Query query = mAlarmsRef.orderByChild("user").equalTo(this.getCurrentUser());
        query.addChildEventListener(new AlarmsChildEventListener());
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

    public ArrayList<String> getAlarms() {
        return mAlarms;
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

    private class AlarmsChildEventListener implements ChildEventListener {
        private void updateNotification() {
            Intent intent = new Intent(mActivity, AlarmBroadcastReceiver.class);

            String name = PreferenceManager.getDefaultSharedPreferences(mActivity).getString(Constants.USERNAME, "");
            String password = PreferenceManager.getDefaultSharedPreferences(mActivity).getString("password", "");
            String urlString = PreferenceManager.getDefaultSharedPreferences(mActivity).getString("data_url", "https://netreg.rose-hulman.edu/tools/networkUsageData.pl");

//            ArrayList<String> arr = new ArrayList<String>();
//            arr.add("one");

//            intent.putExtra("alarms", arr);
            intent.putExtra("url", urlString);
            intent.putExtra("name", name);
            intent.putExtra("password", password);


            if (mAlarms != null && mAlarms.size() > 0) {
                intent.putStringArrayListExtra("alarms", mAlarms);
                boolean alarmRunning = (PendingIntent.getBroadcast(mActivity, Constants.ALARM_CODE, intent, PendingIntent.FLAG_NO_CREATE) != null);
                if (alarmRunning == false) {
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity, Constants.ALARM_CODE, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 5, pendingIntent);
                }
            }
        }

        private void add(DataSnapshot dataSnapshot) {
            Alarm alarm = dataSnapshot.getValue(Alarm.class);
            alarm.setKey(dataSnapshot.getKey());
            String currentAlarm = new String();
            currentAlarm += (alarm.getName());
            currentAlarm += "," + (Float.toString(alarm.getAmount()));
            currentAlarm += "," + (Integer.toString(alarm.getType()));
            currentAlarm += "," + (alarm.getKey());
            currentAlarm += "," + (alarm.isEnabled());
            mAlarms.add(currentAlarm);
            updateNotification();
            Log.d(Constants.TAG, "alarm changed");
        }

        private int remove(String key) {
            for (String alarm : mAlarms) {
                if (alarm.split(",")[3].equals(key)) {
                    int foundPos = mAlarms.indexOf(alarm);
                    mAlarms.remove(alarm);
                    updateNotification();
                    Log.d(Constants.TAG, "alarm changed");
                    return foundPos;
                }
            }
            return -1;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.d(Constants.TAG, "My course: " + dataSnapshot);
            add(dataSnapshot);
            //refresh alarm service
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            remove(dataSnapshot.getKey());
            add(dataSnapshot);
            //refresh alarm service
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            int position = remove(dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // empty
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
            Log.e("TAG", "onCancelled. Error: " + firebaseError.getMessage());

        }
    }
}
