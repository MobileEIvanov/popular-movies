package com.popularmovies.data;

import android.util.Log;

import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.MovieDetailsResponse;
import com.popularmovies.data.models.MovieReviewsResponse;
import com.popularmovies.data.models.MoviesResponse;
import com.popularmovies.data.models.MoviesVideoResponse;
import com.popularmovies.entities.MovieItem;

import io.reactivex.Observable;


public class RestDataSource implements QueryRequest {
    private static final String BASE_API_URL = "https://api.themoviedb.org/";
    private static final String API_VERSION = "3";


    private final QueryRequest mRequestQuery;

    public RestDataSource() {
        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(BASE_API_URL)
                .append(API_VERSION)
                .append("/");
        Log.d("Rest:", "RestDataSource: " + baseUrl.toString());
        mRequestQuery = RestClient.initConnection(baseUrl.toString());
    }


    @Override
    public Observable<ConfigurationResponse> requestConfigurations(String apiKey) {
        return mRequestQuery.requestConfigurations(apiKey);
    }


    @Override
    public Observable<MoviesResponse> requestMoviesByCategory(String category, long page, String apiKey) {
        return mRequestQuery.requestMoviesByCategory(category, page, apiKey);
    }

    @Override
    public Observable<MovieReviewsResponse> requestMovieReviews(long id, String apiKey) {
        return mRequestQuery.requestMovieReviews(id, apiKey);
    }

    @Override
    public Observable<MoviesVideoResponse> requestMovieVideos(long id, String apiKey) {
        return mRequestQuery.requestMovieVideos(id, apiKey);
    }

    @Override
    public Observable<MovieItem> requestMovieDetails(long id, String apiKey, String appendToRequest) {
        return mRequestQuery.requestMovieDetails(id, apiKey, appendToRequest);
    }


}
