package com.mobven.moviedb.adapter.base;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class BaseBasicAdapter extends BaseAdapter {

    protected Context context;

    public BaseBasicAdapter(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
