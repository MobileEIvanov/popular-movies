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

public class MoviesResponse {

    @SerializedName(TOTAL_RESULTS)
    private long totalResults;

    @SerializedName(TOTAL_PAGES)
    private long totalPages;

    @SerializedName(PAGE)
    private long pageNumber;

    @SerializedName(RESULTS)
    private List<MovieItem> movieItems;

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<MovieItem> getMovieItems() {
        return movieItems;
    }

    public void setMovieItems(List<MovieItem> movieItems) {
        this.movieItems = movieItems;
    }
}
