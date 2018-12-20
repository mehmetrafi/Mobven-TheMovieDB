package com.mobven.moviedb.fragment.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobven.moviedb.R;
import com.mobven.moviedb.interfaces.ContentViewListener;

public class BaseContentViewFragment extends BaseFragment implements ContentViewListener {

    private TextView errorTextView;

    @Override
    public void makeContentViewConnections() {
        errorTextView = getView().findViewById(R.id.errorTextView);
    }

    //We set default (error) content view to warn if we forget to implement getContentViewResId method
    @Override
    public int getContentViewResId() {
        return R.layout.fragment_no_content_view;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getContentViewResId(),container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        makeContentViewConnections();
        updateErrorTextIfAvaible();
    }

    private void updateErrorTextIfAvaible() {
        if(errorTextView != null) {
            String errorText = getContext().getString(R.string.content_view_listener_error,getClass().getSimpleName());
            errorTextView.setText(errorText);
        }
    }

}
