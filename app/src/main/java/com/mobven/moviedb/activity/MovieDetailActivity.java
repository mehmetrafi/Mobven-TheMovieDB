package com.mobven.moviedb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mobven.moviedb.R;
import com.mobven.moviedb.activity.base.BaseFetchDataActivity;
import com.mobven.moviedb.adapter.MovieDetailAdapter;
import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.ChangeFavoriteMovieListener;
import com.mobven.moviedb.interfaces.tmdb.MovieCreditListener;
import com.mobven.moviedb.interfaces.tmdb.MovieDetailListener;
import com.mobven.moviedb.interfaces.tmdb.MovieResultsPageListener;

import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.ResponseStatus;

public class MovieDetailActivity extends BaseFetchDataActivity implements MovieResultsPageListener, MovieDetailListener, MovieCreditListener,ChangeFavoriteMovieListener, Response.Listener<String>,Response.ErrorListener {

    public static final String MOVIE_ID_KEY = "MOVIE_ID";
    public static final int REQUEST_CODE = 51;

    private ListView listView;
    private boolean isMovieBookmarked;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_movie_detail;
    }

    @Override
    public int getOptionsMenuResId() {
        return R.menu.menu_movie_detail_bookmark;
    }

    @Override
    public void makeContentViewConnections() {
        super.makeContentViewConnections();
        listView = findViewById(R.id.listView);
        listView.setAdapter(new MovieDetailAdapter(this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_bookmarked).setVisible(isMovieBookmarked);
        menu.findItem(R.id.action_bookmark).setVisible(!isMovieBookmarked);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean onOptionsItemSelected = super.onOptionsItemSelected(item);
        if(!onOptionsItemSelected) {
            switch (item.getItemId()) {
                case android.R.id.home: {
                    setResult(RESULT_OK);
                    finish();
                    onOptionsItemSelected = true;
                }
                break;
                case R.id.action_bookmark: {
                    int movieId = getIntent().getIntExtra(MOVIE_ID_KEY,Integer.MIN_VALUE);
                    if(movieId != Integer.MIN_VALUE) {
//                        Please read ChangeFavoriteMovieTask doInBackground method
//                        SingletonTmdbApi.getInstance().changeFavoriteMovie(this,movieId,true);
                        SingletonTmdbApi.getInstance().changeFavoriteMovie(movieId,true,this,this);

                    }
                    onOptionsItemSelected = true;
                }
                break;
                case R.id.action_bookmarked: {
                    int movieId = getIntent().getIntExtra(MOVIE_ID_KEY,Integer.MIN_VALUE);
                    if(movieId != Integer.MIN_VALUE) {
//                        Please read ChangeFavoriteMovieTask doInBackground method
//                        SingletonTmdbApi.getInstance().changeFavoriteMovie(this,movieId,false);
                        SingletonTmdbApi.getInstance().changeFavoriteMovie(movieId,false,this,this);
                    }
                    onOptionsItemSelected = true;
                }
                break;
            }
        }
        return onOptionsItemSelected;
    }

    @Override
    public void fetchFragmentData() {
        if(getIntent() != null && !getIntent().getExtras().isEmpty() && getIntent().getExtras().containsKey(MOVIE_ID_KEY)) {
            int movieId = getIntent().getIntExtra(MOVIE_ID_KEY,Integer.MIN_VALUE);
            if(movieId != Integer.MIN_VALUE) {
                updateActionToolBarBookmarkButton();
                SingletonTmdbApi.getInstance().getMovieDetail(this,movieId);
                SingletonTmdbApi.getInstance().getMovieCredits(this,movieId);
            }

        }
    }

    private void updateActionToolBarBookmarkButton() {
        SingletonTmdbApi.getInstance().getFavoriteMovies(this);
    }

    @Override
    public void onSucess(ResponseStatus responseStatus) {
        //If the add and remove Favorite methods on wrapper worked then we can update the options menu button status...
    }

    @Override
    public void onSucess(MovieDb movieDb) {
        MovieDetailAdapter movieDetailAdapter;
        if((movieDetailAdapter = getMovieDetailAdapter()) != null) {
            movieDetailAdapter.setMovieDb(movieDb);
            movieDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSucess(Credits credits) {
        MovieDetailAdapter movieDetailAdapter;
        if((movieDetailAdapter = getMovieDetailAdapter()) != null) {
            movieDetailAdapter.setCredits(credits);
            movieDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSucess(MovieResultsPage movieResultsPage) {
        isMovieBookmarked = false;
        //We r checking only first page because of limited time of development
        //Under normal conditions we have to check all pages if we marked the movie before a favorite
        //May be there is a method on API but i could not find it :(
        //I tought while getting movie detail we can send some personal data (SessionID and AccountID) to get response if its favorite movie but no luck...
        //Better way can be storing favorite movieID's locally and sync it with remote for performance and less network activity
        int movieId = getIntent().getIntExtra(MOVIE_ID_KEY,Integer.MIN_VALUE);
        if(movieId != Integer.MIN_VALUE) {
            for (MovieDb movieDb : movieResultsPage.getResults()) {
                if(movieDb.getId() == movieId) {
                    isMovieBookmarked = true;
                    break;
                }
            }
        }
        notifyOptionsMenuChanged();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //Error while changing favorite movie status;
    }

    @Override
    public void onResponse(String response) {
        updateActionToolBarBookmarkButton();
    }

    private MovieDetailAdapter getMovieDetailAdapter() {
        MovieDetailAdapter movieDetailAdapter = null;
        if(listView!=null && listView.getAdapter() instanceof MovieDetailAdapter) {
            movieDetailAdapter = (MovieDetailAdapter)listView.getAdapter();
        }
        return movieDetailAdapter;
    }


}

