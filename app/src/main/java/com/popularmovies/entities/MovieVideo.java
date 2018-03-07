package com.popularmovies.entities;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.data.RequestParams;

/**
 * Created by emil.ivanov on 3/6/18.
 */

public class MovieVideo {

    public static final String TYPE_TRAILER = "";
    public static final String TYPE_TEASER ="";
    public static final String TYPE_CLIP ="";
    public static final String TYPE_FEATURETTE ="";

    @SerializedName(RequestParams.VIDEO_ID)
    String id;

    @SerializedName(RequestParams.VIDEO_KEY)
    String videoKey;

    @SerializedName(RequestParams.VIDEO_SITE)
    String videoSite;

    @SerializedName(RequestParams.VIDEO_SIZE)
    int videoSize;

    @SerializedName(RequestParams.VIDEO_TYPE)
    String videoType;

    @SerializedName(RequestParams.VIDEO_NAME)
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getVideoSite() {
        return videoSite;
    }

    public void setVideoSite(String videoSite) {
        this.videoSite = videoSite;
    }

    public int getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(int videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
