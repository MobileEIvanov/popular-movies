package com.popularmovies.presentation.movies;

import android.util.Log;

import com.popularmovies.data.RestClient;
import com.popularmovies.data.RestDataSource;
import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.MoviesResponse;
import com.popularmovies.presentation.movies.ContractMoviesScreen.Presenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by emil.ivanov on 2/23/18.
 */

public class PresenterMovieCollection implements Presenter {

    private final ContractMoviesScreen.View mView;

    private final CompositeDisposable mCompositeDisposable;
    private Disposable mDisplosableMovies;
    private Disposable mDisplosableConfig;

     PresenterMovieCollection(ContractMoviesScreen.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }


    @Override
    public void requestConfigurations() {
        mView.showProgressLoader();
        RestDataSource restDataSource = new RestDataSource();
        mDisplosableConfig = restDataSource.requestConfigurations(RestClient.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessConfigurations, onFailureConfigurations);

        mCompositeDisposable.add(mDisplosableConfig);
    }

    @Override
    public void requestMoviesByCategory(String category, long page) {
        mView.showProgressLoader();
        RestDataSource restDataSource = new RestDataSource();
        mDisplosableMovies = restDataSource.requestMoviesByCategory(category, page, RestClient.API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessMoviesResponse, onFailureMoviesResponse);

        mCompositeDisposable.add(mDisplosableMovies);
    }

    @Override
    public void onResume() {
        Log.d("Presenter: ", "onResume: Called");
        if (mCompositeDisposable != null) {
            if (mDisplosableMovies != null && mDisplosableMovies.isDisposed()) {
                mCompositeDisposable.add(mDisplosableMovies);
            }
            if (mDisplosableConfig != null && mDisplosableConfig.isDisposed()) {
                mCompositeDisposable.add(mDisplosableConfig);
            }
        }
    }


    @Override
    public void onStop() {
        Log.d("Presenter ", "onStop: CALLED ");
        if (mCompositeDisposable != null) {
            if (mDisplosableMovies != null && mDisplosableMovies.isDisposed()) {
                mCompositeDisposable.remove(mDisplosableMovies);
            }
            if (mDisplosableConfig != null && mDisplosableConfig.isDisposed()) {
                mCompositeDisposable.remove(mDisplosableConfig);
            }
        }
    }

    private final Consumer<ConfigurationResponse> onSuccessConfigurations = new Consumer<ConfigurationResponse>() {
        @Override
        public void accept(ConfigurationResponse result) throws Exception {
            if (result.getImageConfiguration() != null) {

                String imageBaseUrl = result.getImageConfiguration().getBaseUrl();
                if (imageBaseUrl != null) {
                    mView.storeImageBaseUrlLocally(imageBaseUrl);
                } else {
                    mView.onConfigurationRequestFailure();
                }

                List<String> imageStillSizes = result.getImageConfiguration().getStillSizes();
                if (imageStillSizes != null && imageStillSizes.size() > 0) {
                    mView.storeStillImageSizesLocally(imageStillSizes);
                } else {
                    mView.onConfigurationRequestFailure();
                }
            }
        }
    };

    private final Consumer<Throwable> onFailureConfigurations = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            mView.onConfigurationRequestFailure();
            mView.hideProgressLoader();
        }
    };

    private final Consumer<MoviesResponse> onSuccessMoviesResponse = new Consumer<MoviesResponse>() {
        @Override
        public void accept(MoviesResponse result) throws Exception {
            if (result != null && result.getMovieItems() != null) {
                mView.displayMovies(result.getMovieItems());
            } else {
                mView.showEmptyView();
            }
            mView.hideProgressLoader();
        }
    };

    private final Consumer<Throwable> onFailureMoviesResponse = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            if (throwable.getMessage() != null) {
                mView.showErrorMessage(throwable.getMessage());
            }
            mView.showEmptyView();
            mView.hideProgressLoader();
        }
    };

}
