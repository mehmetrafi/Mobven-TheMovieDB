package com.mobven.moviedb.helper.tmdb;

import android.content.Context;
import android.content.res.Resources;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mobven.moviedb.R;
import com.mobven.moviedb.interfaces.tmdb.AccountDetailListener;
import com.mobven.moviedb.interfaces.tmdb.ChangeFavoriteMovieListener;
import com.mobven.moviedb.interfaces.tmdb.MovieCreditListener;
import com.mobven.moviedb.interfaces.tmdb.MovieDetailListener;
import com.mobven.moviedb.interfaces.tmdb.MovieResultsPageListener;
import com.mobven.moviedb.interfaces.tmdb.TokenSessionListener;
import com.mobven.moviedb.tasks.async.AccountDetailTask;
import com.mobven.moviedb.tasks.async.ChangeFavoriteMovieTask;
import com.mobven.moviedb.tasks.async.FavoriteMoviesTask;
import com.mobven.moviedb.tasks.async.MovieCreditTask;
import com.mobven.moviedb.tasks.async.MovieDetailTask;
import com.mobven.moviedb.tasks.async.MovieResultsPageTask;
import com.mobven.moviedb.tasks.async.TokenSessionTask;

import java.util.HashMap;
import java.util.Map;

import info.movito.themoviedbapi.model.config.Account;
import info.movito.themoviedbapi.model.config.TokenSession;
import mjson.Json;

public class SingletonTmdbApi implements TokenSessionListener, AccountDetailListener {

    private Context context;
    private String tmdbApiKeyV3;
    private TokenSession tokenSession;
    private Account account;

    private static final String USERNAME = "v_rider";
    private static final String PASSWORD = "18011337";


    private SingletonTmdbApi() { }

    private static SingletonTmdbApi INSTANCE;

    //Enums are not used for sake of performance and less memory usage.
    //Bitwise shift used for future implementation of multiple request at a time
    public static final int TMDB_MRP_PopularMovies = 0x1;
    public static final int TMDB_MRP_TopratedMovies = TMDB_MRP_PopularMovies << 1;
    public static final int TMDB_MRP_UpcomingMovies = TMDB_MRP_TopratedMovies << 1;
    public static final int TMDB_MRP_FavoriteMovies = TMDB_MRP_UpcomingMovies << 1;

    public static SingletonTmdbApi getInstance() {
        if(INSTANCE == null || INSTANCE.getTmdbApiKeyV3() == null ) {
            return null;
        }
        return INSTANCE;
    }

    public static class SingletonTmdbApiInitError extends Throwable {
        public SingletonTmdbApiInitError(Throwable cause) {
            super(cause);
        }
    }

    public static void init(Context context) throws SingletonTmdbApiInitError  {
        if(context != null && INSTANCE == null) {
            try {
                String tmdbApiKeyV3 = context.getApplicationContext().getResources().getString(R.string.the_movie_db_api_key_v3);
                INSTANCE = new SingletonTmdbApi();
                INSTANCE.setTmdbApiKeyV3(tmdbApiKeyV3);
                INSTANCE.setContext(context);
                //We r making early fetch for some important data like SessionID and Account ID to future use
                //It can cause a race condition if the user try to use them before they r ready
                //As future work we can make the app wait for them to open.
                INSTANCE.fetchImportantData();
            } catch (Resources.NotFoundException e) {
                throw new SingletonTmdbApiInitError(e);
            }
        }
    }

    private void fetchImportantData() {
        getTokenSession(this, USERNAME,PASSWORD);
    }

    public void getAccountDetails(AccountDetailListener listener) {
        AccountDetailTask accountDetailTask = new AccountDetailTask(listener);
        accountDetailTask.execute();
    }

    public int getAccountId() {
        int accountId = Integer.MIN_VALUE;
        if(account != null) {
            accountId = account.getId();
        }
        return accountId;
    }

    @Override
    public void onSucess(Account account) {
        this.account = account;
    }

    public void getTokenSession(TokenSessionListener listener, Object... params) {
        TokenSessionTask tokenSessionTask = new TokenSessionTask(listener);
        tokenSessionTask.execute(params);
    }

    public String getSessionId() {
        String sessionId = null;
        if(tokenSession != null) {
            sessionId = tokenSession.getSessionId();
        }
        return sessionId;
    }

    @Override
    public void onSucess(TokenSession tokenSession) {
        this.tokenSession = tokenSession;
        getAccountDetails(this);
    }

    public void changeFavoriteMovie(ChangeFavoriteMovieListener listener,Object... params) {
        ChangeFavoriteMovieTask changeFavoriteMovieTask = new ChangeFavoriteMovieTask(listener);
        changeFavoriteMovieTask.execute(params);
    }

    public void getFavoriteMovies(MovieResultsPageListener listener) {
        FavoriteMoviesTask favoriteMoviesTask = new FavoriteMoviesTask(listener);
        favoriteMoviesTask.execute();
    }

    public void getMovieCredits(MovieCreditListener listener, Object... params) {
        MovieCreditTask movieCreditTask = new MovieCreditTask(listener);
        movieCreditTask.execute(params);
    }

    public void getMovieDetail(MovieDetailListener listener, Object... params) {
        MovieDetailTask movieDetailTask = new MovieDetailTask(listener);
        movieDetailTask.execute(params);
    }

    public void getMovies(int TMDB_MRP_Type, MovieResultsPageListener listener,Object... params) {
        MovieResultsPageTask task = new MovieResultsPageTask(TMDB_MRP_Type,listener);
        task.execute(params);
    }

    public void getPopularMovies(MovieResultsPageListener listener,Object... params) {
        getMovies(TMDB_MRP_PopularMovies,listener,params);
    }

    public void getTopratedMovies(MovieResultsPageListener listener,Object... params) {
        getMovies(TMDB_MRP_TopratedMovies,listener,params);
    }

    public void getUpcoming(MovieResultsPageListener listener,Object... params) {
        getMovies(TMDB_MRP_UpcomingMovies,listener);
    }

    public void changeFavoriteMovie(final int movieId, final boolean status, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        final String sessionId = SingletonTmdbApi.getInstance().getSessionId();
        final String accountId = String.valueOf(SingletonTmdbApi.getInstance().getAccountId());
        final String apiKey = SingletonTmdbApi.getInstance().getTmdbApiKeyV3();

        String url = "https://api.themoviedb.org/3/account/" + accountId + "/favorite?session_id="+ sessionId +"&api_key=" + apiKey;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("content-type","application/json;charset=utf-8");
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Json body = Json.read("{\"media_type\":\"movie\",\"media_id\":" + movieId + ",\"favorite\":" + status + "}");
                return body.toString().getBytes();
            }
        };

        Volley.newRequestQueue(getContext()).add(stringRequest);
    }

    public String getTmdbApiKeyV3() {
        return tmdbApiKeyV3;
    }

    private void setTmdbApiKeyV3(String tmdbApiKeyV3) {
        this.tmdbApiKeyV3 = tmdbApiKeyV3;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
