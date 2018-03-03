package com.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.data.RequestParams;

import java.util.List;

/**
 * Created by emil.ivanov on 2/21/18.
 */

public class ImageConfiguration {
    public static final int STILL_SIZE_MIN_POSITION = 0;
    public static final int STILL_SIZE_MEDIUM_POSITION = 1;
    public static final int STILL_SIZE_MAX_POSITION = 2;
    public static final int STILL_SIZE_ORIGINAL_POSITION = 3;


    @SerializedName(RequestParams.BACKDROP_SIZES)
    private List<String> backdropSizes;
    @SerializedName(RequestParams.LOGO_SIZES)
    private List<String> logoSizes;
    @SerializedName(RequestParams.POSTER_SIZES)
    private List<String> posterSizes;
    @SerializedName(RequestParams.PROFILE_SIZES)
    private List<String> profileSizes;
    @SerializedName(RequestParams.STILL_SIZES)
    private List<String> stillSizes;
    @SerializedName(RequestParams.IMAGE_BASE_URL)
    private String baseUrl;
    @SerializedName(RequestParams.SECURE_BASE_URL)
    private String secureBaseUrl;

    public List<String> getStillSizes() {
        return stillSizes;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

}
