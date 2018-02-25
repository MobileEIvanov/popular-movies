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
    List<String> backdropSizes;
    @SerializedName(RequestParams.LOGO_SIZES)
    List<String>  logoSizes;
    @SerializedName(RequestParams.POSTER_SIZES)
    List<String>  posterSizes;
    @SerializedName(RequestParams.PROFILE_SIZES)
    List<String>  profileSizes;
    @SerializedName(RequestParams.STILL_SIZES)
    List<String> stillSizes;
    @SerializedName(RequestParams.IMAGE_BASE_URL)
    String baseUrl;
    @SerializedName(RequestParams.SECURE_BASE_URL)
    String secureBaseUrl;

    public List<String> getBackdropSizes() {
        return backdropSizes;
    }

    public void setBackdropSizes(List<String> backdropSizes) {
        this.backdropSizes = backdropSizes;
    }

    public List<String> getLogoSizes() {
        return logoSizes;
    }

    public void setLogoSizes(List<String> logoSizes) {
        this.logoSizes = logoSizes;
    }

    public List<String> getPosterSizes() {
        return posterSizes;
    }

    public void setPosterSizes(List<String> posterSizes) {
        this.posterSizes = posterSizes;
    }

    public List<String> getProfileSizes() {
        return profileSizes;
    }

    public void setProfileSizes(List<String> profileSizes) {
        this.profileSizes = profileSizes;
    }

    public List<String> getStillSizes() {
        return stillSizes;
    }

    public void setStillSizes(List<String> stillSizes) {
        this.stillSizes = stillSizes;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSecureBaseUrl() {
        return secureBaseUrl;
    }

    public void setSecureBaseUrl(String secureBaseUrl) {
        this.secureBaseUrl = secureBaseUrl;
    }
}
