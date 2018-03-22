package com.popularmovies.presentation.details;

import android.net.Uri;
import android.util.Log;

import com.popularmovies.data.RequestParams;
import com.popularmovies.data.RestClient;
import com.popularmovies.data.RestDataSource;
import com.popularmovies.data.database.MovieDaoImpl;
import com.popularmovies.data.models.MovieReviewsResponse;
import com.popularmovies.data.models.MoviesVideoResponse;
import com.popularmovies.entities.MovieItem;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by emil.ivanov on 3/9/18.
 */

public class PresenterMovieDetails implements ContractMovieDetails.Presenter {

    private ContractMovieDetails.View mView;
    private MovieDaoImpl mMovieDao;

    public PresenterMovieDetails(ContractMovieDetails.View view, MovieDaoImpl movieDao) {
        this.mView = view;
        this.mMovieDao = movieDao;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onStop() {
        if (!mDatabaseOperation.isDisposed()) {
            mDatabaseOperation.dispose();
        }

        if (!mNetworkOperationReview.isDisposed()) {
            mNetworkOperationReview.dispose();
        }

        if (!mNetworkOperationVideos.isDisposed()) {
            mNetworkOperationVideos.dispose();
        }

    }

    Disposable mNetworkOperationVideos;

    @Override
    public void requestMovieVideos(long movieId) {
        RestDataSource restDataSource = new RestDataSource();
        mNetworkOperationVideos = restDataSource.requestMovieVideos(movieId, RestClient.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessVideosResponse, onFailureVideosResponse);
    }

    private final Consumer<MoviesVideoResponse> onSuccessVideosResponse = moviesVideoResponse -> {
        if (moviesVideoResponse != null && moviesVideoResponse.getMovieVideos() != null && moviesVideoResponse.getMovieVideos().size() > 0) {
            mView.displayVideos(moviesVideoResponse.getMovieVideos());
        } else {
            mView.displayEmptyVideos();
        }
    };

    private final Consumer<Throwable> onFailureVideosResponse = throwable -> {
        mView.displayEmptyVideos();
    };

    Disposable mNetworkOperationReview;

    @Override
    public void requestMovieReviews(long movieId) {

        // TODO: 3/8/18 http://www.zoftino.com/retrofit-rxjava-android-example
        RestDataSource restDataSource = new RestDataSource();
        mNetworkOperationReview = restDataSource.requestMovieReviews(movieId, RestClient.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessReviewsResponse, onFailureReviewsResponse);
    }


    @Override
    public void requestMovieDetails(MovieItem movieItem) {
        String appendToRequest = RequestParams.VIDEOS +
                "," +
                RequestParams.REVIEWS;

        RestDataSource restDataSource = new RestDataSource();
        mNetworkOperationReview = restDataSource.requestMovieDetails(movieItem.getId(), RestClient.API_KEY, appendToRequest)
                .map(new Function<MovieItem, MovieItem>() {
                    @Override
                    public MovieItem apply(MovieItem movieItem) throws Exception {
                        boolean isFavorite = mMovieDao.isFavorite(movieItem);
                        movieItem.setFavorite(isFavorite);
                        return movieItem;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessDetailsResponse, onFailureDetailsResponse);
    }

    Disposable mDatabaseOperation;

    @Override
    public void requestChangeFavoriteStatus(MovieItem movieItem) {
        mDatabaseOperation = mMovieDao.findRecord(movieItem).map((Function<Boolean, Object>) recordExists -> {
            Log.d("Presenter", "apply: Back THREAD ");
            if (recordExists) {
                return mMovieDao.updateRecord(movieItem);

            } else {
                return mMovieDao.createRecord(movieItem);
            }

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessRecordUpdate, onFailureRecordUpdate);
        Log.d("Presenter", "requestChangeFavoriteStatus: MAIN THREAD ");
    }



    private final Consumer<MovieItem> onSuccessDetailsResponse = new Consumer<MovieItem>() {
        @Override
        public void accept(MovieItem movieItem) throws Exception {
            if (movieItem != null) {
                mView.displayMovieDetails(movieItem);
            }
        }
    };

    private final Consumer<Throwable> onFailureDetailsResponse = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            mView.displayEmptyReviews();
        }
    };


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


    private final Consumer<Object> onSuccessRecordUpdate = operationResult -> {
        if (operationResult instanceof Integer) {
            Log.d("Preseter: ", "accept: " + operationResult);
        } else if (operationResult instanceof Uri) {
            Log.d("Preseter: ", "accept: " + operationResult.toString());
        }
    };

    private final Consumer<Throwable> onFailureRecordUpdate = throwable -> Log.d("Preseter: ", "accept: " + throwable.getMessage());

}
