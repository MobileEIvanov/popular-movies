package com.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;

import static com.popularmovies.data.RequestParams.IMAGES;

/**
 * Created by emil.ivanov on 2/21/18.
 */

public class ConfigurationResponse {

    @SerializedName(IMAGES)
    private ImageConfiguration imageConfiguration;


    public ImageConfiguration getImageConfiguration() {
        return this.imageConfiguration;
    }


}
