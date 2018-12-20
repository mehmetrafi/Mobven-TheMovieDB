package com.mobven.moviedb.interfaces.tmdb;

import info.movito.themoviedbapi.model.core.ResponseStatus;

public interface ChangeFavoriteMovieListener {

    void onSucess(ResponseStatus responseStatus);

}
