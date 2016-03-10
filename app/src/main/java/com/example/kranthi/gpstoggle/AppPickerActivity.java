package com.example.kranthi.gpstoggle;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kranthi.gpstoggle.loaders.AppListLoader;

import java.util.ArrayList;
import java.util.List;

public class AppPickerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<PackageInfo>> {

    public static final int SELECTED_APPS_CODE = 3;
    public static final String SELECTED_APPS_EXTRA = "selected_apps";
    ActivityListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_picker);

        Intent intent = getIntent();
        ArrayList<String> selectedApps = intent.getStringArrayListExtra(SELECTED_APPS_EXTRA);

        // setup recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ActivityListAdapter();
        mAdapter.setContext(this);
        if (selectedApps != null && !selectedApps.isEmpty()) {
            mAdapter.addSelections(selectedApps);
        }
        recyclerView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_app_picker, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done_selection) {
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra(SELECTED_APPS_EXTRA, ActivityListAdapter.selectedPackages);
            setResult(RESULT_OK, resultIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<PackageInfo>> onCreateLoader(int id, Bundle args) {
        return new AppListLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<PackageInfo>> loader, List<PackageInfo> data) {
        mAdapter.setPackages(data);

    }

    @Override
    public void onLoaderReset(Loader<List<PackageInfo>> loader) {

    }
}
