package com.popularmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.popularmovies.utils.UtilsNetworkConnection;
import com.squareup.picasso.Picasso;

/**
 * Created by emil.ivanov on 3/21/18.
 */


// https://stackoverflow.com/questions/23978828/how-do-i-use-disk-caching-in-picasso
public class PopularMoviesApplication extends Application {
    //    Red color indicates that image is fetched from network.
//
//    Green color indicates that image is fetched from cache memory.
//
//    Blue color indicates that image is fetched from disk memory.
    @Override
    public void onCreate() {
        super.onCreate();


        if (UtilsNetworkConnection.checkInternetConnection(this)) {
            Stetho.initializeWithDefaults(this);
        }

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(false);
        Picasso.setSingletonInstance(built);
    }
}
