package com.mobven.moviedb.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mobven.moviedb.adapter.base.BaseFragmentPagerAdapter;
import com.mobven.moviedb.fragment.base.BaseContentViewFragment;
import com.mobven.moviedb.fragment.base.BaseFragment;
import com.mobven.moviedb.helper.mjson.DataExtractor;

import mjson.Json;

public class MovieListSectionsPagerAdapter extends BaseFragmentPagerAdapter {

    private static final String FRAGMENT_CLASS_KEY = "fragmet_class";
    private static final String BUNDLE_KEY = "bundle";

    public MovieListSectionsPagerAdapter(FragmentManager fm, @NonNull int pagerDataResId) {
        super(fm, pagerDataResId);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Fragment getItem(int position) {
        Json fragmentMetaData;
        Fragment fragment = null;
        if((fragmentMetaData = getPagerItemData(position)) != null) {
            String className = DataExtractor.getString(FRAGMENT_CLASS_KEY,fragmentMetaData);
            try {
                Object o = Class.forName(className).newInstance();
                if(o instanceof BaseFragment) {
                    fragment = (BaseFragment)o;
                    Bundle bundle;
                    if((bundle = DataExtractor.getBundle(BUNDLE_KEY,fragmentMetaData)) != null) {
                        fragment.setArguments(bundle);
                    }
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        if(fragment == null) {
            fragment = BaseFragment.newInstance(null, BaseContentViewFragment.class);
        }
        return fragment;
    }
}
