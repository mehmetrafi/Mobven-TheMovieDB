package com.mobven.moviedb.interfaces.tmdb;

import info.movito.themoviedbapi.model.Credits;

public interface MovieCreditListener {

    void onSucess(Credits credits);

}
