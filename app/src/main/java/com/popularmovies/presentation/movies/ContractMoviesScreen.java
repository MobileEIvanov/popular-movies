package com.popularmovies.presentation.movies;

import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.ImageConfiguration;
import com.popularmovies.entities.MovieItem;

import java.util.List;

/**
 * Created by user on 2/23/18.
 */

public interface ContractMoviesScreen {
    interface Presenter {

        void requestConfigurations();

        void requestMoviesByCategory(String category, long page);

        void onStop();
    }

    interface View {
        void storeImageBaseUrlLocally(String imageBaseURL);

        void storeStillImageSizesLocally(List<String> stillImageSizes);

        void onConfigurationRequestFailure();

        void displayMovies(List<MovieItem> movieItemList);

        void showEmptyView();

        void showErrorMessage(String message);

    }
}
