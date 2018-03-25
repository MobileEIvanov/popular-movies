package com.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.entities.MovieVideo;

import java.util.List;

import static com.popularmovies.data.RequestParams.MOVIE_ID;
import static com.popularmovies.data.RequestParams.RESULTS;

/**
 * Created by emil.ivanov on 2/22/18.
 * <p>
 * Response object after request for {@link MovieVideo}
 * items part of {@link com.popularmovies.entities.MovieItem}
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
