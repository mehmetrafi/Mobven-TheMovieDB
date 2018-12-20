package com.mobven.moviedb.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public abstract class BaseFragment extends Fragment {

    //Helper method to easly cast fragments activity to use activity methods
    protected <T extends FragmentActivity> T getActivityAs(Class<T> expectedAcitivtyClass) {
        return (expectedAcitivtyClass.isInstance(getActivity())) ? expectedAcitivtyClass.cast(getActivity()) : null;
    }

    //Helper method to easly get fragment instance with arguments
    public static <T extends Fragment> T newInstance(Bundle args, Class<T> fragmentClass) {
        T fragment = null;
        try {
            fragment = fragmentClass.newInstance();
            fragment.setArguments(args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return fragment;
    }

}
