package com.example.kranthi.gpstoggle.loaders;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by kranthi on 18/02/16.
 */
public class AppListLoader extends AbstractAsyncLoader<List<PackageInfo>> {

    public AppListLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public List<PackageInfo> loadInBackground() {
        return getContext().getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
    }
}
