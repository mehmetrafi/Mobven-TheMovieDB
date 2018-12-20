package com.mobven.moviedb.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.mobven.moviedb.interfaces.fragment.FragmentFetchDataListener;

public abstract class BaseFetchDataFragment extends BaseContentViewFragment implements FragmentFetchDataListener {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchFragmentData();
    }

    @Override
    public void fetchFragmentData() {

    }
}
