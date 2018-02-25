package com.popularmovies.data;

import android.util.Log;

import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.MoviesResponse;

import io.reactivex.Observable;


public class RestDataSource implements QueryRequest {
    public  static  final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/";
    String BASE_API_URL = "https://api.themoviedb.org/";
    String API_VERSION = "3";
//    https://api.themoviedb.org/3/movie/550?api_key=d697cb5b08dda13f992c27272775af90



    QueryRequest mRequestQuery;
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
}
