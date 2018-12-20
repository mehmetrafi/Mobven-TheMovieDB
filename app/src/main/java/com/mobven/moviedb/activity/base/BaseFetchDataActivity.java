package com.mobven.moviedb.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mobven.moviedb.interfaces.activity.ActivityFetchDataListener;

public abstract class BaseFetchDataActivity extends BaseAppBarLayoutActivity implements ActivityFetchDataListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchFragmentData();
    }

    @Override
    public void fetchFragmentData() {

    }
}
