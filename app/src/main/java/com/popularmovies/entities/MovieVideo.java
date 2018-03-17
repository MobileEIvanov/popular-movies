package com.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.data.RequestParams;

/**
 * Created by emil.ivanov on 3/6/18.
 */

public class MovieVideo implements Parcelable {

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

    protected MovieVideo(Parcel in) {
        id = in.readString();
        videoKey = in.readString();
        videoSite = in.readString();
        videoSize = in.readInt();
        videoType = in.readString();
        name = in.readString();
    }

    public static final Creator<MovieVideo> CREATOR = new Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel in) {
            return new MovieVideo(in);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoKey() {
        return videoKey;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(videoKey);
        parcel.writeString(videoSite);
        parcel.writeInt(videoSize);
        parcel.writeString(videoType);
        parcel.writeString(name);
    }
}
