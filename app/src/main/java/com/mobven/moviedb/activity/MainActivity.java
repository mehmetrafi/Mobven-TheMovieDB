package com.mobven.moviedb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.mobven.moviedb.R;
import com.mobven.moviedb.activity.base.BaseAppBarLayoutActivity;
import com.mobven.moviedb.adapter.MovieListSectionsPagerAdapter;
import com.mobven.moviedb.interfaces.fragment.FragmentFetchDataListener;

public class MainActivity extends BaseAppBarLayoutActivity {

    private MovieListSectionsPagerAdapter movieListSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout mTablayout;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    public void makeContentViewConnections() {
        super.makeContentViewConnections();
        mViewPager = findViewById(R.id.container);
        mTablayout = findViewById(R.id.tabs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        movieListSectionsPagerAdapter = new MovieListSectionsPagerAdapter(getSupportFragmentManager(),R.raw.activity_main_fragment_tabs);
        mViewPager.setAdapter(movieListSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTablayout));
        mTablayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //We update the Favorite Tab data to sync if user add or remove movie from favorites
        //For future work we can track if user change the state in Detail Activity then if yes we can update the data
        //For now we do not check the resultCode or requestCode and update everytime if the tab is front

        final int FAVORITE_MOVIES_TAB_ORDER = 3;
        int position = mViewPager.getCurrentItem();
        if(position == FAVORITE_MOVIES_TAB_ORDER) {
            Fragment fragment = movieListSectionsPagerAdapter.getFromActiveFragment(position);
            if(fragment instanceof FragmentFetchDataListener) {
                ((FragmentFetchDataListener)fragment).fetchFragmentData();
            }
        }
    }
}
