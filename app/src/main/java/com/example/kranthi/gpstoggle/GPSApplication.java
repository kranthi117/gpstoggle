package com.example.kranthi.gpstoggle;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class GPSApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}