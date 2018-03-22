package com.popularmovies.presentation.movies;

import android.database.Cursor;
import android.util.Log;

import com.popularmovies.data.RestClient;
import com.popularmovies.data.RestDataSource;
import com.popularmovies.data.database.ContractMoviesData;
import com.popularmovies.data.database.MovieDaoImpl;
import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.MoviesResponse;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.presentation.movies.ContractMoviesScreen.Presenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by emil.ivanov on 2/23/18.
 */

public class PresenterMovieCollection implements Presenter {

    private final ContractMoviesScreen.View mView;
    private MovieDaoImpl mMovieDao;

    private final CompositeDisposable mCompositeDisposable;
    private Disposable mDisplosableMovies;
    private Disposable mDisplosableConfig;


    public PresenterMovieCollection(ContractMoviesScreen.View mView, MovieDaoImpl mMovieDao) {
        this.mView = mView;
        this.mMovieDao = mMovieDao;
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

                // TODO: 3/15/18 Make a request for all the movies in the database? and map if there are favorite?
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccessMoviesResponse, onFailureMoviesResponse);

        mCompositeDisposable.add(mDisplosableMovies);
    }

    @Override
    public void requestFavorites() {
        String whereClause = ContractMoviesData.MovieEntry.COLUMN_IS_FAVORITE + "=?";
        String[] selectionArgs = {"1"};
        mMovieDao.queryAll(whereClause, selectionArgs).map(new Function<Cursor, List<MovieItem>>() {
            @Override
            public List<MovieItem> apply(Cursor cursor) throws Exception {
                int indexId = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_MOVIE_ID);
                int indexTitle = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_MOVIE_TITLE);
                int indexFavorite = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_IS_FAVORITE);
                int indexRating = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_MOVIE_RATING);
                int indexReleaseDate = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_MOVIE_RELEASE_DATE);
                int indexPlotOverview = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_MOVIE_PLOT_OVERVIEW);
                int indexOriginalTitle = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_MOVIE_TITLE_ORIGINAL);
                int indexPosterImage = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_POSTER_PATH);
                List<MovieItem> movieItems = new ArrayList<>();

                while (cursor.moveToNext()) {
                    MovieItem movieItem = new MovieItem();

                    movieItem.setTitle(cursor.getString(indexTitle));
                    movieItem.setFavorite(cursor.getInt(indexFavorite) == 1);
                    movieItem.setId(cursor.getLong(indexId));
                    movieItem.setOriginalTitle(cursor.getString(indexOriginalTitle));
                    movieItem.setOverview(cursor.getString(indexPlotOverview));
                    movieItem.setVoteAverage(cursor.getFloat(indexRating));
                    movieItem.setReleaseDate(cursor.getString(indexReleaseDate));
                    movieItem.setPosterPath(cursor.getString(indexPosterImage));
                    movieItems.add(movieItem);
                }

                return movieItems;
            }
        }).subscribe(new Consumer<List<MovieItem>>() {
            @Override
            public void accept(List<MovieItem> movieItems) throws Exception {
                if (movieItems.size() > 0) {
                    mView.displayMovies(movieItems);
                } else {
                    mView.showEmptyView();
                }
            }
        });

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
