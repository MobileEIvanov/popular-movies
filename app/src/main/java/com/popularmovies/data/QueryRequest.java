package com.popularmovies.data;


import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.MovieReviewsResponse;
import com.popularmovies.data.models.MoviesResponse;
import com.popularmovies.data.models.MoviesVideoResponse;
import com.popularmovies.entities.MovieItem;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Used to describe the request for information requested from  API https://developers.themoviedb.org/3/
 * {@link QueryRequest} is then used to create an Retrofit client in order to retrieve the information {@link RestClient}
 */
public interface QueryRequest {

    String URL_CONFIGURATIONS = "configuration";
    String URL_MOVIES_BY_CATEGORY = "movie/{category}";
    String URL_MOVIE_REVIEWS = "movie/{id}/reviews";
    String URL_MOVIE_VIDEOS = "movie/{id}/videos";
    String URL_MOVIE_DETAILS = "movie/{id}";

    @GET(URL_CONFIGURATIONS)
    Observable<ConfigurationResponse> requestConfigurations(@Query(RequestParams.API_KEY) String apiKey);

    @GET(URL_MOVIES_BY_CATEGORY)
    Observable<MoviesResponse> requestMoviesByCategory(@Path(RequestParams.MOVIE_CATEGORY) String category,
                                                       @Query(RequestParams.PAGE) long page,
                                                       @Query(RequestParams.API_KEY) String apiKey);

    @GET(URL_MOVIE_REVIEWS)
    Observable<MovieReviewsResponse> requestMovieReviews(@Path(RequestParams.ID) long id,
                                                         @Query(RequestParams.API_KEY) String apiKey);

    @GET(URL_MOVIE_VIDEOS)
    Observable<MoviesVideoResponse> requestMovieVideos(@Path(RequestParams.ID) long id,
                                                       @Query(RequestParams.API_KEY) String apiKey);

    @GET(URL_MOVIE_DETAILS)
    Observable<MovieItem> requestMovieDetails(@Path(RequestParams.ID) long id,
                                              @Query(RequestParams.API_KEY) String apiKey,
                                              @Query(RequestParams.APPEND_TO_RESPONSE) String appendToRequest);
}
