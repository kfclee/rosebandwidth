package edu.rosehulman.jungckjp_leekf.rosebandwidth.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;

/**
 * Created by jonathan on 2/14/16.
 *
 *
 * BACKGROUND SERVICE: http://javatechig.com/android/creating-a-background-service-in-android
 */
public class AlarmService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AlarmService(String name) {
        super(name);
    }

    public AlarmService() {
        super("alarmService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "Alarm Service onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(Constants.TAG, "Service Started");
    }
}
