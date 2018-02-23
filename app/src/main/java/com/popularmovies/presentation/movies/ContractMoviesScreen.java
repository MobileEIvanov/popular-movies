package com.popularmovies.presentation.movies;

import com.popularmovies.entities.MovieItem;

import java.util.List;

/**
 * Created by user on 2/23/18.
 */

public interface ContractMoviesScreen {
    interface Presenter {
        void requestMoviesByCategory(String category, long page);

        void requestConfigurations();

        void onStop();
    }

    interface View {
        void displayMovies(List<MovieItem> movieItemList);

        void showEmptyView();

        void showErrorMessage(String message);

    }
}
