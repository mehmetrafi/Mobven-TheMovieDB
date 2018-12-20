package com.mobven.moviedb.tasks.async;

import android.os.AsyncTask;

import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.MovieDetailListener;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieDetailTask extends AsyncTask<Object,Void,MovieDb> {

    public static int MOVIE_ID_ORDER = 0;

    private MovieDetailListener listener;

    public MovieDetailTask(MovieDetailListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(MovieDb movieDb) {
        super.onPostExecute(movieDb);
        if(listener != null) {
            listener.onSucess(movieDb);
        }
    }

    @Override
    protected MovieDb doInBackground(Object... objects) {
        MovieDb movieDb = null;
        TmdbApi tmdbApi = new TmdbApi(SingletonTmdbApi.getInstance().getTmdbApiKeyV3());
        Integer movieId = null;
        if(objects != null) {
            Object param;
            if(objects.length > MOVIE_ID_ORDER && (param = objects[MOVIE_ID_ORDER]) instanceof Integer) {
                movieId = (Integer)param;
            }
        }
        if(movieId != null) {
            movieDb = tmdbApi.getMovies().getMovie(movieId,null,null);
        }
        return movieDb;
    }
}
