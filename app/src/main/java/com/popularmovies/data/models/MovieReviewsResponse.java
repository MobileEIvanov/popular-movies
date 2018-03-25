package com.popularmovies.data.models;

import com.google.gson.annotations.SerializedName;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.entities.MovieReview;

import java.util.List;

import static com.popularmovies.data.RequestParams.PAGE;
import static com.popularmovies.data.RequestParams.RESULTS;
import static com.popularmovies.data.RequestParams.TOTAL_PAGES;
import static com.popularmovies.data.RequestParams.TOTAL_RESULTS;

/**
 * Created by emil.ivanov on 2/22/18.
 * <p>
 * Response object holding the information after request for {@link MovieReview}
 * representation used in  {@link MovieItem}
 */

public class MovieReviewsResponse {

    @SerializedName(TOTAL_RESULTS)
    private long totalResults;

    @SerializedName(TOTAL_PAGES)
    private long totalPages;

    @SerializedName(PAGE)
    private long pageNumber;

    @SerializedName(RESULTS)
    private List<MovieReview> movieReviewsItems;


    public List<MovieReview> getMovieReviewsItems() {
        return movieReviewsItems;
    }


}
