package com.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.data.RequestParams;

/**
 * Created by emil.ivanov on 2/21/18.
 */

public class ImageEntitiy {

    @SerializedName(RequestParams.BACKDROP_SIZES)
    String[] backdropSizes;
    @SerializedName(RequestParams.LOGO_SIZES)
    String[] logoSizes;
    @SerializedName(RequestParams.POSTER_SIZES)
    String[] posterSizes;
    @SerializedName(RequestParams.PROFILE_SIZES)
    String[] profileSizes;
    @SerializedName(RequestParams.STILL_SIZES)
    String[] stillSizes;
    @SerializedName(RequestParams.BASE_URL)
    String baseUrl;
    @SerializedName(RequestParams.SECURE_BASE_URL)
    String secureBaseUrl;


}
