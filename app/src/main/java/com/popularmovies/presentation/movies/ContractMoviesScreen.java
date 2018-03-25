package com.popularmovies.presentation.movies;

import com.popularmovies.entities.MovieItem;

import java.util.List;

/**
 * Created by emil.ivanov on 2/23/18.
 * Contract representation of Presenter and View actions part of the MVP architecture.
 */

interface ContractMoviesScreen {
    interface Presenter {

        void requestConfigurations();

        void requestMoviesByCategory(String category, long page);

        void requestFavorites();

        void onResume();

        void onStop();
    }

    interface View {
        void storeImageBaseUrlLocally(String imageBaseURL);

        void storeStillImageSizesLocally(List<String> stillImageSizes);

        void onConfigurationRequestFailure();

        void displayMovies(List<MovieItem> movieItemList);

        void showEmptyView();

        void showErrorMessage(String message);

        void showProgressLoader();

        void hideProgressLoader();

    }
}
