package com.example.kranthi.gpstoggle;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ToggleGpsService extends IntentService {

    public static final String TOGGLE_GPS = "toggle_gps";
    public static final int DISABLE_GPS = 100;
    private static final String TAG = ToggleGpsService.class.getSimpleName();

    public ToggleGpsService(String name) {
        super(name);
    }

    public ToggleGpsService() {
        super("ToggleGpsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "Received " + intent);
        String action = intent.getAction();
        if (TOGGLE_GPS.equals(action)) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setOngoing(true);
            Intent disableGps = new Intent(getApplicationContext(), DisableGpsActivity.class);
            PendingIntent pi = PendingIntent.getActivity(this, 0, disableGps, 0);
            builder.setContentIntent(pi);
            builder.setContentInfo("No applications are using GPS. Tap here to disable that and save battery");
            notificationManager.notify(1, builder.build());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
