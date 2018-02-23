package com.popularmovies.data;

import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.MoviesResponse;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Query;
import rx.Observable;


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
    public Observable<MoviesResponse> requestMoviesByCategory(String category, String apiKey, long page ) {
        return mRequestQuery.requestMoviesByCategory(category,apiKey,page);
    }
}
