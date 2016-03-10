package com.example.kranthi.gpstoggle;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.ArraySet;
import android.util.Log;

import com.jaredrummler.android.processes.ProcessManager;

import java.util.List;
import java.util.Set;

public class GpsEnabledReceiver extends BroadcastReceiver {

    private static final String TAG = GpsEnabledReceiver.class.getSimpleName();
    private static final int MAX_TRIES = 10;
    private static final int MULTIPLYING_FACTOR = 2;
    private static final int TIMEOUT = 2000;

    public GpsEnabledReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("android.location.PROVIDERS_CHANGED")) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.w(TAG, "GPS is not enabled. Ignoring");
                return;
            }

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            Set<String> selectedApps = prefs.getStringSet(AppPickerActivity.SELECTED_APPS_EXTRA, null);

            ScheduleDisableGps scheduleDisableGps = new ScheduleDisableGps(context);
            scheduleDisableGps.selectedApps = selectedApps;
            scheduleDisableGps.execute();
        }
    }

    private void turnGpsOff(Context context) {

    }

    static class ScheduleDisableGps extends AsyncTask {
        Set<String> selectedApps;
        Context mContext;

        public ScheduleDisableGps(Context context) {
            mContext = context;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            int i = 0;
            while (true) {
                boolean isActive = false;
                if (selectedApps != null){
                    List<ActivityManager.RunningAppProcessInfo> processes = ProcessManager.getRunningAppProcessInfo(mContext);
                    Log.w(TAG, "Checking " + selectedApps.toString());
                    for (ActivityManager.RunningAppProcessInfo process : processes) {
                        if (selectedApps.contains(process.processName)) {
                            Log.w(TAG, process.processName + " is active. Ignoring");
                            isActive = true;
                            break;
                        }
                    }
                }
                if (!isActive) {
                    Settings.Secure.putString(mContext.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED, "network,wifi");
                    Log.i(TAG, "Starting service to disable GPS");
                    return null;
                }
                if (i <= MAX_TRIES) {
                    SystemClock.sleep(TIMEOUT * MULTIPLYING_FACTOR);
                    i++;
                } else {
                    break;
                }
            }
            return null;
        }
    }
}
