package com.popularmovies.presentation.details;

import android.net.Uri;
import android.util.Log;

import com.popularmovies.data.RequestParams;
import com.popularmovies.data.RestClient;
import com.popularmovies.data.RestDataSource;
import com.popularmovies.data.database.MovieDaoImpl;
import com.popularmovies.entities.MovieItem;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by emil.ivanov on 3/9/18.
 * Presenter class implementing the {@link ContractMovieDetails.Presenter} part of MVP architecture patter.
 */

public class PresenterMovieDetails implements ContractMovieDetails.Presenter {

    private final ContractMovieDetails.View mView;
    private final MovieDaoImpl mMovieDao;
    private Disposable mNetworkOperation;
    private Disposable mDatabaseOperation;

    PresenterMovieDetails(ContractMovieDetails.View view, MovieDaoImpl movieDao) {
        this.mView = view;
        this.mMovieDao = movieDao;
    }


    @Override
    public void onStop() {

        if (mDatabaseOperation != null && !mDatabaseOperation.isDisposed()) {
            mDatabaseOperation.dispose();
        }

        if (mNetworkOperation != null && !mNetworkOperation.isDisposed()) {
            mNetworkOperation.dispose();
        }
    }


    @Override
    public void requestMovieDetails(MovieItem movieItem) {
        String appendToRequest = RequestParams.VIDEOS +
                "," +
                RequestParams.REVIEWS;

        RestDataSource restDataSource = new RestDataSource();
        mNetworkOperation = restDataSource.requestMovieDetails(movieItem.getId(), RestClient.API_KEY, appendToRequest)
                .map(movieItem1 -> {
                    boolean isFavorite = mMovieDao.isFavorite(movieItem1);
                    movieItem1.setFavorite(isFavorite);
                    return movieItem1;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessDetailsResponse, onFailureDetailsResponse);
    }


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


    private final Consumer<Object> onSuccessRecordUpdate = operationResult -> {
        if (operationResult instanceof Integer) {
            Log.d("Preseter: ", "accept: " + operationResult);
        } else if (operationResult instanceof Uri) {
            Log.d("Preseter: ", "accept: " + operationResult.toString());
        }
    };

    private final Consumer<Throwable> onFailureRecordUpdate = throwable -> Log.d("Preseter: ", "accept: " + throwable.getMessage());

}
