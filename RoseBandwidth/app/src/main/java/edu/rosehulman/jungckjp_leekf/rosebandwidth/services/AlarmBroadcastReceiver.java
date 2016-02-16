package edu.rosehulman.jungckjp_leekf.rosebandwidth.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;

/**
 * Created by jonathan on 2/14/16.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private String mName;
    private String mPassword;
    private String mUrl;
    private ArrayList<String> mAlarms;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        mName = extras.getString("name");
        mPassword = extras.getString("password");
        mUrl = extras.getString("url");
        mAlarms = extras.getStringArrayList("alarms");

//        mAlarms = new ArrayList<String>();
//        mAlarms.add("Alarm,500,1,0");
        Log.d(Constants.TAG, mAlarms.get(0));

        Intent background = new Intent(context, AlarmService.class);
        background.putExtra("name", mName);
        background.putExtra("password", mPassword);
        background.putExtra("url", mUrl);
        background.putExtra("alarms", mAlarms);
        context.startService(background);
    }
}
