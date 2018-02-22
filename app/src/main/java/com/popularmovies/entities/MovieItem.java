package com.popularmovies.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.popularmovies.data.RequestParams.ADULT;
import static com.popularmovies.data.RequestParams.BACKGROP_PATH;
import static com.popularmovies.data.RequestParams.GENRE_IDS;
import static com.popularmovies.data.RequestParams.MOVIE_ID;
import static com.popularmovies.data.RequestParams.ORIGINAL_LANGUAGE;
import static com.popularmovies.data.RequestParams.ORIGINAL_TITLE;
import static com.popularmovies.data.RequestParams.OVERVIEW;
import static com.popularmovies.data.RequestParams.POPULARITY;
import static com.popularmovies.data.RequestParams.POSTER_PATH;
import static com.popularmovies.data.RequestParams.RELEASE_DATE;
import static com.popularmovies.data.RequestParams.TITLE;
import static com.popularmovies.data.RequestParams.VIDEO;
import static com.popularmovies.data.RequestParams.VOTE_AVERAGE;
import static com.popularmovies.data.RequestParams.VOTE_COUNT;

/**
 * A dummy item representing a piece of content.
 */
public class MovieItem {

    public static final String MOVIE_DATA = "movie_data";

    @SerializedName(MOVIE_ID)
    private long id;
    @SerializedName(POSTER_PATH)
    private String posterPath;
    @SerializedName(ADULT)
    private boolean isAdult;

    @SerializedName(OVERVIEW)
    private String overview;

    @SerializedName(RELEASE_DATE)
    private String releaseDate;

    @SerializedName(GENRE_IDS)
    private String[] genreIds;


    @SerializedName(ORIGINAL_TITLE)
    private String originalTitle;

    @SerializedName(ORIGINAL_LANGUAGE)
    private String originalLanguage;

    @SerializedName(TITLE)
    private String title;

    @SerializedName(BACKGROP_PATH)
    private String backdropPath;

    @SerializedName(POPULARITY)
    private double popularity;
    @SerializedName(VOTE_COUNT)
    private long voteCount;

    @SerializedName(VIDEO)
    private boolean hasVideo;

    @SerializedName(VOTE_AVERAGE)
    private float voteAverage;

    public MovieItem() {
    }

    public MovieItem(String imageUrl) {
        this.posterPath = imageUrl;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public static List<MovieItem> generateMovies() {
        List<MovieItem> movieItems = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            movieItems.add(new MovieItem("https://picsum.photos/200/300/?random"));
        }
        return movieItems;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(String[] genreIds) {
        this.genreIds = genreIds;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isHasVideo() {
        return hasVideo;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }
}
