package com.mobven.moviedb.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.mobven.moviedb.R;
import com.mobven.moviedb.interfaces.activity.ActivityOptionsMenuListener;

public abstract class BaseAppBarLayoutActivity extends BaseContentViewActivity implements ActivityOptionsMenuListener {

    private Toolbar toolbar;

    @Override
    public void makeContentViewConnections() {
        super.makeContentViewConnections();
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public int getOptionsMenuResId() {
        return Integer.MIN_VALUE;
    }



    @Override
    public void notifyOptionsMenuChanged() {
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean onCreateOptionsMenu = super.onCreateOptionsMenu(menu);
        int menuResId;
        if((menuResId = getOptionsMenuResId()) != Integer.MIN_VALUE) {
            getMenuInflater().inflate(menuResId,menu);
            onCreateOptionsMenu = true;
        }
        return onCreateOptionsMenu;
    }

}
