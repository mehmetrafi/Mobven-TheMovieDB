package com.mobven.moviedb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobven.moviedb.R;
import com.mobven.moviedb.activity.MovieDetailActivity;
import com.mobven.moviedb.adapter.MovieResultPageAdapter;
import com.mobven.moviedb.fragment.base.BaseFetchDataFragment;
import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.MovieResultsPageListener;

import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MovieListingFragment extends BaseFetchDataFragment implements MovieResultsPageListener {

    private static final String TMDB_MRP_TYPE_KEY = "TMDB_MRP_Type";

    private ListView listView;

    @Override
    public int getContentViewResId() {
        return R.layout.fragment_movie_listing;
    }

    @Override
    public void makeContentViewConnections() {
        super.makeContentViewConnections();
        listView = getView().findViewById(R.id.listView);
    }

    //Place data fetch operations after content view created to prevent race conditions
    @Override
    public void fetchFragmentData() {
        super.fetchFragmentData();
        if(getArguments() != null && getArguments().containsKey(TMDB_MRP_TYPE_KEY)) {
            int TMDB_MRP_type = Integer.parseInt(getArguments().getString(TMDB_MRP_TYPE_KEY));
            SingletonTmdbApi.getInstance().getMovies(TMDB_MRP_type,this);
        }
    }

    //We need to fetch data onResume for favorites tab.
    //Viewpager loads fragments before they come to visible. So if user update a favorite movie in UpComing tab
    //We should update Favorite tab data.
    //Getting double data for this tab under certain conditions is known bug and can be fixed in relaxed development time.
    @Override
    public void onResume() {
        super.onResume();
        if(getArguments() != null && getArguments().containsKey(TMDB_MRP_TYPE_KEY)) {
            int TMDB_MRP_type = Integer.parseInt(getArguments().getString(TMDB_MRP_TYPE_KEY));
            if(TMDB_MRP_type == SingletonTmdbApi.TMDB_MRP_FavoriteMovies) {
                fetchFragmentData();
            }
        }
    }

    @Override
    public void onSucess(MovieResultsPage movieResultsPage) {
        MovieResultPageAdapter movieResultPagerAdapter = new MovieResultPageAdapter(getContext(),movieResultsPage);
        listView.setAdapter(movieResultPagerAdapter);
        listView.setOnItemClickListener(new MovieOnClickListener());
    }

    public class MovieOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(parent != null && parent.getAdapter() instanceof MovieResultPageAdapter) {
                MovieResultPageAdapter movieResultPagerAdapter = (MovieResultPageAdapter)parent.getAdapter();
                MovieDb movieDb =  (MovieDb)movieResultPagerAdapter.getItem(position);

                Intent contentDetailIntent = new Intent(MovieListingFragment.this.getActivity(),MovieDetailActivity.class);
                contentDetailIntent.putExtra(MovieDetailActivity.MOVIE_ID_KEY,movieDb.getId());
                startActivityForResult(contentDetailIntent,MovieDetailActivity.REQUEST_CODE);
            }
        }

    }
}
