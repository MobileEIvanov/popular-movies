package com.popularmovies.presentation.details;

import android.util.Log;

import com.popularmovies.data.RestClient;
import com.popularmovies.data.RestDataSource;
import com.popularmovies.data.models.MovieReviewsResponse;
import com.popularmovies.data.models.MoviesVideoResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by emil.ivanov on 3/9/18.
 */

public class PresenterMovieDetails implements ContractMovieDetails.Presenter {

    private ContractMovieDetails.View mView;

    public PresenterMovieDetails(ContractMovieDetails.View mView) {
        this.mView = mView;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void requestMovieVideos(long movieId) {
        RestDataSource restDataSource = new RestDataSource();
        restDataSource.requestMovieVideos(movieId, RestClient.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessVideosResponse, onFailureVideosResponse);
    }

    private final Consumer<MoviesVideoResponse> onSuccessVideosResponse = moviesVideoResponse -> {
        if (moviesVideoResponse != null && moviesVideoResponse.getMovieVideos() != null && moviesVideoResponse.getMovieVideos().size() > 0) {
                mView.displayVideos(moviesVideoResponse.getMovieVideos());
        }else{
            mView.displayEmptyVideos();
        }
    };

    private final Consumer<Throwable> onFailureVideosResponse = throwable -> {
        mView.displayEmptyVideos();
    };

    @Override
    public void requestMovieReviews(long movieId) {

        // TODO: 3/8/18 http://www.zoftino.com/retrofit-rxjava-android-example
        RestDataSource restDataSource = new RestDataSource();
        restDataSource.requestMovieReviews(movieId, RestClient.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessReviewsResponse, onFailureReviewsResponse);
    }

    private final Consumer<MovieReviewsResponse> onSuccessReviewsResponse = new Consumer<MovieReviewsResponse>() {
        @Override
        public void accept(MovieReviewsResponse movieReviewsResponse) throws Exception {
            if (movieReviewsResponse != null && movieReviewsResponse.getMovieReviewsItems() != null && movieReviewsResponse.getMovieReviewsItems().size() > 0) {
                mView.displayReviews(movieReviewsResponse.getMovieReviewsItems());
            } else {
                mView.displayEmptyReviews();
            }
        }
    };

    private final Consumer<Throwable> onFailureReviewsResponse = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            mView.displayEmptyReviews();
        }
    };

}
