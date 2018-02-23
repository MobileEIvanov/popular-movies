package com.popularmovies.data;


import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.MoviesResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface QueryRequest {


    String URL_CONFIGURATIONS = "configuration";
    String URL_MOVIE_POPULAR = "movie/popular";
    String URL_MOVIE_TOP_RATED = "movie/top_rated";
    String URL_MOVIE_BY_ID   = "movie/{movie_id}";
    String URL_MOVIES_BY_CATEGORY = "movie/{category}";

    // TODO: 2/23/18 Use append request for sub movie by id https://developers.themoviedb.org/3/getting-started/append-to-response
    @GET(URL_CONFIGURATIONS)
    Observable<ConfigurationResponse> requestConfigurations(@Query(RequestParams.API_KEY) String apiKey);

    @GET(URL_MOVIES_BY_CATEGORY)
    Observable<MoviesResponse> requestMoviesByCategory(@Path(RequestParams.MOVIE_CATEGORY) String category,@Query(RequestParams.API_KEY) String apiKey, @Query(RequestParams.PAGE) long page);



}
