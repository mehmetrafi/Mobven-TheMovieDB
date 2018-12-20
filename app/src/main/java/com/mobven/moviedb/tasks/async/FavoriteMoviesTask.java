package com.mobven.moviedb.tasks.async;

import android.os.AsyncTask;

import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.MovieResultsPageListener;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.SessionToken;

public class FavoriteMoviesTask extends AsyncTask<Void,Void,MovieResultsPage> {

    private MovieResultsPageListener listener;

    public FavoriteMoviesTask(MovieResultsPageListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(MovieResultsPage movieResultsPage) {
        super.onPostExecute(movieResultsPage);
        if(listener != null) {
            listener.onSucess(movieResultsPage);
        }
    }

    @Override
    protected MovieResultsPage doInBackground(Void... voids) {
        MovieResultsPage movieResultsPage = null;
        String sessionId;
        int accountId;
        if((sessionId = SingletonTmdbApi.getInstance().getSessionId()) != null && (accountId = SingletonTmdbApi.getInstance().getAccountId()) != Integer.MIN_VALUE) {
            TmdbApi tmdbApi = new TmdbApi(SingletonTmdbApi.getInstance().getTmdbApiKeyV3());
            movieResultsPage = tmdbApi.getAccount().getFavoriteMovies(new SessionToken(sessionId),new AccountID(accountId));
        }
        return movieResultsPage;
    }
}
