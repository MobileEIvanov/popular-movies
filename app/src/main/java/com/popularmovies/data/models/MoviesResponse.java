package com.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.entities.MovieItem;

import java.util.List;

import static com.popularmovies.data.RequestParams.PAGE;
import static com.popularmovies.data.RequestParams.RESULTS;
import static com.popularmovies.data.RequestParams.TOTAL_PAGES;
import static com.popularmovies.data.RequestParams.TOTAL_RESULTS;

/**
 * Created by emil.ivanov on 2/22/18.
 */

@SuppressWarnings("DefaultFileTemplate")
public class MoviesResponse {

    @SerializedName(TOTAL_RESULTS)
    private long totalResults;

    @SerializedName(TOTAL_PAGES)
    private long totalPages;

    @SerializedName(PAGE)
    private long pageNumber;

    @SerializedName(RESULTS)
    private List<MovieItem> movieItems;


    public List<MovieItem> getMovieItems() {
        return movieItems;
    }


}
