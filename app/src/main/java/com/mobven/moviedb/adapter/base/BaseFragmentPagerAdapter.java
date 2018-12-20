package com.mobven.moviedb.adapter.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.mobven.moviedb.helper.raw.RawFileHelper;

import java.util.List;

import mjson.Json;

public abstract class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    private Json pagerData;
    private SparseArray<Fragment> activeFragments;

    public BaseFragmentPagerAdapter(FragmentManager fm, @NonNull int pagerDataResId) {
        super(fm);
        prepareFragmentPagerAdapterData(pagerDataResId);
        initActiveFragmentHolder();
    }

    private void prepareFragmentPagerAdapterData(int pagerDataResId) {
        Json data = RawFileHelper.getInstance().getJsonFromRawResource(pagerDataResId);
        if(data != null) {
            setPagerData(data);
        }
    }

    private void initActiveFragmentHolder() {
        activeFragments = new SparseArray<>();
    }

    private Json getPagerData() {
        return pagerData;
    }

    //Helper if pager data is array
    //Future work add helper if pager data is object
    protected Json getPagerItemData(int position) {
        Json data = null;
        List<Json> pagerDataAsList;
        if((pagerDataAsList = getPagerDataAsList()) != null) {
            if(position >= 0 && position < pagerDataAsList.size()) {
                data = pagerDataAsList.get(position);
            }
        }
        return data;
    }

    private List<Json> getPagerDataAsList() {
        List<Json> pagerDataAsList = null;
        if(getPagerData().isArray()) {
            pagerDataAsList = getPagerData().asJsonList();
        }
        return pagerDataAsList;
    }

    private void setPagerData(Json pagerData) {
        this.pagerData = pagerData;
    }

    @Override
    public int getCount() {
        int count = 0;
        List<Json> pagerDataAsList;
        if((pagerDataAsList = getPagerDataAsList()) != null) {
            count = pagerDataAsList.size();
        }
        return count;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        activeFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        activeFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getFromActiveFragment(int position) {
        return activeFragments.get(position);
    }
}
