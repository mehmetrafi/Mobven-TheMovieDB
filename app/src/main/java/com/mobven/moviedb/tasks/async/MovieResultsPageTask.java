package com.mobven.moviedb.tasks.async;

import android.os.AsyncTask;
import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;
import com.mobven.moviedb.interfaces.tmdb.MovieResultsPageListener;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.SessionToken;

public class MovieResultsPageTask extends AsyncTask<Object,Void,MovieResultsPage> {

    public static int LANGUAGE_PARAM_ORDER = 0;
    public static int PAGE_PARAM_ORDER = 1;
    public static int REGION_PARAM_ORDER = 2;

    private int TMDB_MRP_Type;
    private MovieResultsPageListener listener;

    public MovieResultsPageTask(int TMDB_MRP_Type, MovieResultsPageListener listener) {
        this.TMDB_MRP_Type = TMDB_MRP_Type;
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
    protected MovieResultsPage doInBackground(Object... objects) {
        Integer page = null;
        String language = null;
        String region = null;

        MovieResultsPage movieResultsPage = null;

        if(objects != null) {
            Object param;
            if(objects.length > LANGUAGE_PARAM_ORDER && (param = objects[LANGUAGE_PARAM_ORDER]) instanceof String) {
                language = (String)param;
            }
            if(objects.length > PAGE_PARAM_ORDER && (param = objects[PAGE_PARAM_ORDER]) instanceof Integer) {
                page = (Integer)param;
            }
            if(objects.length > REGION_PARAM_ORDER && (param = objects[REGION_PARAM_ORDER]) instanceof String) {
                region = (String)param;
            }
        }

        TmdbApi tmdbApi = new TmdbApi(SingletonTmdbApi.getInstance().getTmdbApiKeyV3());
        
        switch (TMDB_MRP_Type) {
            case SingletonTmdbApi.TMDB_MRP_PopularMovies: {
                movieResultsPage = tmdbApi.getMovies().getPopularMovies(language,page);
            }
            break;
            case SingletonTmdbApi.TMDB_MRP_TopratedMovies: {
                movieResultsPage = tmdbApi.getMovies().getTopRatedMovies(language,page);
            }
            break;
            case SingletonTmdbApi.TMDB_MRP_UpcomingMovies: {
                movieResultsPage = tmdbApi.getMovies().getNowPlayingMovies(language,page,region);
            }
            break;
            case SingletonTmdbApi.TMDB_MRP_FavoriteMovies: {
                String sessionId;
                int accountId;
                if((sessionId = SingletonTmdbApi.getInstance().getSessionId()) != null && (accountId = SingletonTmdbApi.getInstance().getAccountId()) != Integer.MIN_VALUE) {
                    SessionToken sessionToken = new SessionToken(sessionId);
                    AccountID accountID = new AccountID(accountId);
                    movieResultsPage = tmdbApi.getAccount().getFavoriteMovies(sessionToken,accountID);
                }
            }
            break;
            //No need default we set the result null at very beginning
        }
        return movieResultsPage;
    }

}
