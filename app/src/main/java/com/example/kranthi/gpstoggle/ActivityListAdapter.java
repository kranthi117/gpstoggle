package com.example.kranthi.gpstoggle;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kranthi on 19/02/16.
 */
public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder> {

    private static final String TAG = ActivityListAdapter.class.getSimpleName();
    private static SelectionListener selectionListener = new SelectionListener();
    public static ArrayList<String> selectedPackages;
    public ArrayList<PackageInfo> packages;
    private Context context;

    @Override
    public ActivityListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new ViewHolder(view);
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void onBindViewHolder(ActivityListAdapter.ViewHolder holder, int position) {
        PackageInfo packageInfo = packages.get(position);
        if (selectedPackages != null && selectedPackages.contains(packageInfo.packageName)) {
            holder.toggle.setChecked(true);
        }
        holder.toggle.setTag(R.id.package_name, packageInfo.packageName);
        holder.toggle.setOnCheckedChangeListener(selectionListener);
        holder.appName.setText(packageInfo.applicationInfo.loadLabel(context.getPackageManager()));
        holder.appIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.appIcon.setImageDrawable(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.toggle.setOnCheckedChangeListener(null);
        holder.toggle.setTag(R.id.package_name, null);
        holder.toggle.setChecked(false);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return packages == null ? 0 : packages.size();
    }

    public void setPackages(List<PackageInfo> packages) {
        if (this.packages == null) {
            this.packages = new ArrayList<>();
        }
        this.packages.addAll(packages);
        notifyDataSetChanged();
    }

    public void addSelections(List<String> selections) {
        if (selectedPackages == null) {
            selectedPackages = new ArrayList<>();
        }
        selectedPackages.addAll(selections);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView appName;
        ImageButton appIcon;
        Switch toggle;

        public ViewHolder(View itemView) {
            super(itemView);
            toggle = (Switch) itemView.findViewById(R.id.app_selection);
            toggle.setOnCheckedChangeListener(selectionListener);
            appIcon = (ImageButton) itemView.findViewById(R.id.app_icon);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            toggle.toggle();
        }
    }

    static class SelectionListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String packageName = (String) buttonView.getTag(R.id.package_name);
            if (packageName == null) {
                Log.e(TAG, "No package name set. Returning");
                return;
            }
            if (selectedPackages == null) {
                selectedPackages = new ArrayList<>();
            }
            if (isChecked) {
                selectedPackages.add(packageName);
            } else if (selectedPackages.contains(packageName)) {
                selectedPackages.remove(packageName);
            } else {
                Log.wtf(TAG, "The package does not exist in the selected packages list");
            }
        }
    }
}
