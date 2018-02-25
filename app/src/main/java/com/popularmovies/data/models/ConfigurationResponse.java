package com.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static com.popularmovies.data.RequestParams.CHANGE_KEYS;
import static com.popularmovies.data.RequestParams.IMAGES;

/**
 * Created by user on 2/21/18.
 */

public class ConfigurationResponse {

    @SerializedName(IMAGES)
    ImageConfiguration imageConfiguration;


    @SerializedName(CHANGE_KEYS)
    List<String> changedKeys;

    public ImageConfiguration getImageConfiguration() {
        return imageConfiguration;
    }

    public void setImageConfiguration(ImageConfiguration imageConfiguration) {
        this.imageConfiguration = imageConfiguration;
    }

    public List<String> getChangedKeys() {
        return changedKeys;
    }

    public void setChangedKeys(List<String> changedKeys) {
        this.changedKeys = changedKeys;
    }
}
