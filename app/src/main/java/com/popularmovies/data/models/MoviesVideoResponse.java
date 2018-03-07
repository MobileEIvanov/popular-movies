package com.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.entities.MovieVideo;

import java.util.List;

import static com.popularmovies.data.RequestParams.MOVIE_ID;
import static com.popularmovies.data.RequestParams.PAGE;
import static com.popularmovies.data.RequestParams.RESULTS;
import static com.popularmovies.data.RequestParams.TOTAL_PAGES;
import static com.popularmovies.data.RequestParams.TOTAL_RESULTS;

/**
 * Created by emil.ivanov on 2/22/18.
 */

public class MoviesVideoResponse {
    @SerializedName(MOVIE_ID)
    private long id;
    @SerializedName(RESULTS)
    private List<MovieVideo> movieItems;


    public List<MovieVideo> getMovieVideos() {
        return movieItems;
    }


}
