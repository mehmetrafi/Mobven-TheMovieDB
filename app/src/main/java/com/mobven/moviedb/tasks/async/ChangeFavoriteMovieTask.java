package com.mobven.moviedb.tasks.async;

import android.os.AsyncTask;

import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.ChangeFavoriteMovieListener;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.ResponseStatus;
import info.movito.themoviedbapi.model.core.SessionToken;

public class ChangeFavoriteMovieTask extends AsyncTask<Object,Void,ResponseStatus> {

    public static int MOVIE_ID_ORDER = 0;
    public static int IS_FAVORITE_ORDER = MOVIE_ID_ORDER + 1;

    private ChangeFavoriteMovieListener listener;

    public ChangeFavoriteMovieTask(ChangeFavoriteMovieListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(ResponseStatus responseStatus) {
        super.onPostExecute(responseStatus);
        if(listener != null) {
            listener.onSucess(responseStatus);
        }
    }

    @Override
    protected ResponseStatus doInBackground(Object... objects) {
        ResponseStatus responseStatus = null;
        Integer movieId = null;
        Boolean isFavorite = false;
        if(objects != null) {
            Object param;
            if(objects.length > MOVIE_ID_ORDER && (param = objects[MOVIE_ID_ORDER]) instanceof Integer) {
                movieId = (Integer) param;
            }
            if(objects.length > IS_FAVORITE_ORDER && (param = objects[IS_FAVORITE_ORDER]) instanceof Boolean) {
                isFavorite = (Boolean) param;
            }
        }
        String sessionId;
        int accountId;
        if(movieId != null &&
                (sessionId = SingletonTmdbApi.getInstance().getSessionId()) != null &&
                (accountId = SingletonTmdbApi.getInstance().getAccountId()) != Integer.MIN_VALUE) {
            TmdbApi tmdbApi = new TmdbApi(SingletonTmdbApi.getInstance().getTmdbApiKeyV3());

            SessionToken sessionToken = new SessionToken(sessionId);
            AccountID accountID = new AccountID(accountId);
            //We r only thinking about movies thats why we did not get MediaType as param
            //Methods addFavorite and removeFavorite keeps crashing "Caused by: ResponseStatus{code=34, message=The resource you requested could not be found.}"
            //On The Movie DB site "https://developers.themoviedb.org/3/account/mark-as-favorite" it works
            //So we will use a simple Volley Request
            if(isFavorite) {
//                responseStatus = tmdbApi.getAccount().addFavorite(sessionToken,accountID,movieId,TmdbAccount.MediaType.MOVIE);
            } else {
//                responseStatus = tmdbApi.getAccount().removeFavorite(sessionToken,accountID,movieId,TmdbAccount.MediaType.MOVIE);
            }
        }
        return responseStatus;
    }
}
