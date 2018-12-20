package com.mobven.moviedb.tasks.async;

import android.os.AsyncTask;

import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.AccountDetailListener;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.config.Account;
import info.movito.themoviedbapi.model.core.SessionToken;

public class AccountDetailTask extends AsyncTask<Void,Void,Account> {

    private AccountDetailListener listener;

    public AccountDetailTask(AccountDetailListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(Account account) {
        super.onPostExecute(account);
        if(listener != null) {
            listener.onSucess(account);
        }
    }

    @Override
    protected Account doInBackground(Void... voids) {
        Account account = null;
        String sessionId;
        if((sessionId = SingletonTmdbApi.getInstance().getSessionId()) != null) {
            TmdbApi tmdbApi = new TmdbApi(SingletonTmdbApi.getInstance().getTmdbApiKeyV3());
            account = tmdbApi.getAccount().getAccount(new SessionToken(sessionId));
        }
        return account;
    }

}
