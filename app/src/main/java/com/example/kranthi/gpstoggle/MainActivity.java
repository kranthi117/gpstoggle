package com.example.kranthi.gpstoggle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void whitelist(View v) {
        Intent selectionIntent = new Intent();
        selectionIntent.setClass(this, AppPickerActivity.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> selectedApps = prefs.getStringSet(AppPickerActivity.SELECTED_APPS_EXTRA, null);
        if (selectedApps != null) {
            selectionIntent.putStringArrayListExtra(AppPickerActivity.SELECTED_APPS_EXTRA, new ArrayList<>(selectedApps));
        }
        startActivityForResult(selectionIntent, AppPickerActivity.SELECTED_APPS_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppPickerActivity.SELECTED_APPS_CODE) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> selectedApps = data.getStringArrayListExtra(AppPickerActivity.SELECTED_APPS_EXTRA);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = prefs.edit();
                if (selectedApps == null || selectedApps.isEmpty()) {
                    editor.remove(AppPickerActivity.SELECTED_APPS_EXTRA);
                } else {
                    editor.putStringSet(AppPickerActivity.SELECTED_APPS_EXTRA, new HashSet<>(selectedApps));
                }
                editor.apply();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
