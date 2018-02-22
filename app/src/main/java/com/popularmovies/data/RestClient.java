package com.popularmovies.data;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by emil ivanov on 2/21/18.
 */

public class RestClient {

   public static final String API_KEY = "<YOUR_API_KEY>";



    public static QueryRequest initConnection(String baseURL) {

        Retrofit retrofitRx = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(getCustomHeaderWithoutXAuth())
                .build();
       return retrofitRx.create(QueryRequest.class);
    }


    //TODO Refactor you don't need all of it
    private static OkHttpClient getCustomHeaderWithoutXAuth() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
//        httpClient.connectTimeout(60, TimeUnit.SECONDS);

        httpClient.addNetworkInterceptor(new StethoInterceptor());

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();

        return client;
    }


}
