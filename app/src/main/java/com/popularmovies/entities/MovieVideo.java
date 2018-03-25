package com.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.data.RequestParams;

/**
 * Created by emil.ivanov on 3/6/18.
 */

@SuppressWarnings({"CanBeFinal", "DefaultFileTemplate"})
public class MovieVideo implements Parcelable {

    @SerializedName(RequestParams.VIDEO_ID)
    private String id;

    @SerializedName(RequestParams.VIDEO_KEY)
    private String videoKey;

    @SerializedName(RequestParams.VIDEO_SITE)
    private String videoSite;

    @SerializedName(RequestParams.VIDEO_SIZE)
    private int videoSize;

    @SerializedName(RequestParams.VIDEO_TYPE)
    private String videoType;

    @SerializedName(RequestParams.VIDEO_NAME)
    private String name;

    @SuppressWarnings("WeakerAccess")
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
