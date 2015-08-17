package com.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.myapplication.Activity_Setting;
import com.myapplication.MainActivity;
import com.myapplication.R;

public class AccountService extends Service {

    private Intent sendintent = new Intent("com.example.communication.RECEIVER");
    public static final int MAX_PROGRESS = 50;
    /**
     * 进度条的进度值
     */
    private int progress = 0;

    public AccountService() {
    }

    @Override
    public void onCreate() {
        Log.i("account_service", "create");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i("account_service", "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("account_service", "onStartCommand");
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (progress < MAX_PROGRESS) {
                    progress += 5;
                    Log.i("account_service", "sendBroadcast");
                    sendintent.putExtra("progress", progress);
                    sendBroadcast(sendintent);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
