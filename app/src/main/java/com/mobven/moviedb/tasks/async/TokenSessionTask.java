package com.mobven.moviedb.tasks.async;

import android.os.AsyncTask;

import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.TokenSessionListener;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.config.TokenSession;

public class TokenSessionTask extends AsyncTask<Object,Void,TokenSession> {

    public static int USERNAME_ORDER = 0;
    public static int PASSWORD_ORDER = USERNAME_ORDER + 1;

    private TokenSessionListener listener;

    public TokenSessionTask(TokenSessionListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(TokenSession tokenSession) {
        super.onPostExecute(tokenSession);
        if(listener != null) {
            listener.onSucess(tokenSession);
        }
    }

    @Override
    protected TokenSession doInBackground(Object... objects) {
        String username = null;
        String password = null;
        TokenSession tokenSession = null;
        if(objects != null) {
            Object param;
            if(objects.length > USERNAME_ORDER && (param = objects[USERNAME_ORDER]) instanceof String) {
                username = (String) param;
            }
            if(objects.length > PASSWORD_ORDER && (param = objects[PASSWORD_ORDER]) instanceof String) {
                password = (String) param;
            }
        }
        if(username != null && password != null) {
            TmdbApi tmdbApi = new TmdbApi(SingletonTmdbApi.getInstance().getTmdbApiKeyV3());
            tokenSession = tmdbApi.getAuthentication().getSessionLogin(username,password);
        }
        return tokenSession;
    }
}
