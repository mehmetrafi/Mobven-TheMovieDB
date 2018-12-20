package com.mobven.moviedb.application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mobven.moviedb.application.base.BaseApplication;
import com.mobven.moviedb.helper.raw.RawFileHelper;
import com.mobven.moviedb.helper.tmdb.SingletonTmdbApi;

public class MobvenApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initRawFileHelper();
        initSingletonTmdbApi();
        initializeFresco();
    }

    private void initRawFileHelper() {
        RawFileHelper.init(getApplicationContext());
    }

    private void initSingletonTmdbApi() {
        try {
            SingletonTmdbApi.init(getApplicationContext());
        } catch (SingletonTmdbApi.SingletonTmdbApiInitError singletonTmdbApiInitError) {
            singletonTmdbApiInitError.printStackTrace();
        }
    }

    private void initializeFresco() {
        Fresco.initialize(getApplicationContext());
    }

}
