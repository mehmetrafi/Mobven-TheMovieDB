package com.mobven.moviedb.interfaces.tmdb;

import info.movito.themoviedbapi.model.config.TokenSession;

public interface TokenSessionListener {

    void onSucess(TokenSession tokenSession);

}
