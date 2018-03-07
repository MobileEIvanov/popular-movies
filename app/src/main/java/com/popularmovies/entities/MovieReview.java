package com.popularmovies.entities;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.data.RequestParams;

/**
 * Created by emil.ivanov on 3/6/18.
 */

public class MovieReview {

    @SerializedName(RequestParams.REVIEW_ID)
    String id;
    @SerializedName(RequestParams.REVIEW_AUTHOR)
    String author;
    @SerializedName(RequestParams.REVIEW_CONTENT)
    String content;
    @SerializedName(RequestParams.REVIEW_URL)
    String url;

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
}
