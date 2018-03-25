package com.popularmovies.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.data.RequestParams;

/**
 * Created by emil.ivanov on 3/6/18.
 * Review object representation used in  {@link MovieItem}
 */

public class MovieReview implements Parcelable {

    @SerializedName(RequestParams.REVIEW_ID)
    private String id;
    @SerializedName(RequestParams.REVIEW_AUTHOR)
    private String author;
    @SerializedName(RequestParams.REVIEW_CONTENT)
    private String content;
    @SerializedName(RequestParams.REVIEW_URL)
    private String url;

    @SuppressWarnings("WeakerAccess")
    protected MovieReview(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MovieReview{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
        parcel.writeString(url);
    }
}
