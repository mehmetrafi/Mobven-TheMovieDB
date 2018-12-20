package com.mobven.moviedb.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import com.mobven.moviedb.R;
import com.mobven.moviedb.interfaces.ContentViewListener;

public abstract class BaseContentViewActivity extends BaseActivity implements ContentViewListener {

    private TextView errorTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        makeContentViewConnections();
        updateErrorTextIfAvaible();
    }

    //We set default (error) content view to warn if we forget to implement getContentViewResId method
    @Override
    public int getContentViewResId() {
        return R.layout.activity_no_content_view;
    }

    @Override
    public void makeContentViewConnections() {
        errorTextView = findViewById(R.id.errorTextView);
    }

    private void updateErrorTextIfAvaible() {
        if(errorTextView != null) {
            String errorText = getString(R.string.content_view_listener_error,getClass().getSimpleName());
            errorTextView.setText(errorText);
        }
    }
}
