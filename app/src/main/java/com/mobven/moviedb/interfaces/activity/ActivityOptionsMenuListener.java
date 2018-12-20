package com.mobven.moviedb.interfaces.activity;

public interface ActivityOptionsMenuListener {

    //As future work we can add method to allow supress parents menu items @onCreateOptionsMenu method
    int getOptionsMenuResId();
    void notifyOptionsMenuChanged();

}
