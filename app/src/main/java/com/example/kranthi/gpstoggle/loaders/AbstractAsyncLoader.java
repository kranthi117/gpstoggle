package com.example.kranthi.gpstoggle.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;


abstract public class AbstractAsyncLoader<E> extends AsyncTaskLoader<E> {

    private E mData;

    public AbstractAsyncLoader(Context ctx) {
        super(ctx);
    }

    @Override
    public void deliverResult(E data) {
        if (isReset()) {
            releaseResources(data);
            return;
        }
        E oldData = mData;
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }
        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if (mData != null) {
            releaseResources(mData);
            mData = null;
        }
    }

    @Override
    public void onCanceled(E data) {
        super.onCanceled(data);
        releaseResources(data);
    }

    private void releaseResources(E data) {
    }
}