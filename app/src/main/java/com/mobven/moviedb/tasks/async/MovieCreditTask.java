package com.mobven.moviedb.tasks.async;

import android.os.AsyncTask;

import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.MovieCreditListener;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Credits;
import info.movito.themoviedbapi.model.config.Account;
import info.movito.themoviedbapi.model.config.TokenSession;
import info.movito.themoviedbapi.model.core.SessionToken;

public class MovieCreditTask extends AsyncTask<Object,Void,Credits> {

    public static int MOVIE_ID_ORDER = 0;

    private MovieCreditListener listener;

    public MovieCreditTask(MovieCreditListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Credits credits) {
        super.onPostExecute(credits);
        if(listener != null) {
            listener.onSucess(credits);
        }
    }

    @Override
    protected Credits doInBackground(Object... objects) {
        Credits credits = null;
        Integer movieId = null;
        if(objects != null) {
            Object param;
            if(objects.length > MOVIE_ID_ORDER && (param = objects[MOVIE_ID_ORDER]) instanceof Integer) {
                movieId = (Integer)param;
            }
        }
        if(movieId != null) {
            TmdbApi tmdbApi = new TmdbApi(SingletonTmdbApi.getInstance().getTmdbApiKeyV3());
            credits = tmdbApi.getMovies().getCredits(movieId);
        }
        return credits;
    }
}
