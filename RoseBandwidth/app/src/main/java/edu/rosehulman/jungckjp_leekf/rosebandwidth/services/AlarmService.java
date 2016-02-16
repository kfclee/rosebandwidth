package edu.rosehulman.jungckjp_leekf.rosebandwidth.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import edu.rosehulman.jungckjp_leekf.rosebandwidth.R;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.activities.MainActivity;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.API;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.Constants;
import edu.rosehulman.jungckjp_leekf.rosebandwidth.utils.getDataTask;

/**
 * Created by jonathan on 2/14/16.
 *
 *
 * BACKGROUND SERVICE: http://javatechig.com/android/creating-a-background-service-in-android
 */
public class AlarmService extends Service implements getBackgroundDataTask.DataConsumer {
//    /**
//     * Creates an IntentService.  Invoked by your subclass's constructor.
//     *
//     * @param name Used to name the worker thread, important only for debugging.
//     */
//    public AlarmService(String name) {
//        super(name);
//    }
//
//    public AlarmService() {
//        super("alarmService");
//    }

    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private String mName;
    private String mPassword;
    private String mUrl;
    private ArrayList<String> mAlarms;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        public void run() {
            // Do something here
            Log.d(Constants.TAG, "Running Alarm!");
            new getBackgroundDataTask(mName, mPassword, mUrl, AlarmService.this).execute();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            Bundle extras = intent.getExtras();
            mName = extras.getString("name");
            mPassword = extras.getString("password");
            mUrl = extras.getString("url");
            mAlarms = extras.getStringArrayList("alarms");
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDataLoaded(float usage) {
        Log.d(Constants.TAG, "annnnd this is updated code");
        if (mAlarms != null) {
            Log.d(Constants.TAG, "Hi");
            for (String a : mAlarms) {
                String[] alarm = a.split(",");
                if (alarm[4].equals("true")) {
                    if (Float.parseFloat(alarm[1]) < usage) {
                        Log.d(Constants.TAG, alarm[0] + " went off at " + alarm[1]);
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Intent intent = new Intent(this, MainActivity.class);
                        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
                        Notification notification = getNotification(alarm[0], alarm[1], pIntent);
//            notification.setLatestEventInfo(this, context, pIntent);
                        notificationManager.notify(123, notification);
                        stopSelf();
                    }
                }
            }
        }


    }

    private Notification getNotification(String name, String amount, PendingIntent pendingIntent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(name);
        builder.setContentText("You exceeded " + amount + "MB!");
        builder.setSmallIcon(R.drawable.ic_logo);
        Bitmap thumbnail = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo), 96, 96, true);
        builder.setLargeIcon(thumbnail);
        int UNUSED = 3245243;
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }
}
