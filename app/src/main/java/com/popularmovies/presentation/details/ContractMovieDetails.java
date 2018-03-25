package com.popularmovies.presentation.details;

import com.popularmovies.entities.MovieItem;
import com.popularmovies.entities.MovieReview;
import com.popularmovies.entities.MovieVideo;

import java.util.List;

/**
 * Created by emil.ivanov on 3/9/18.
 * Contract representation of Presenter and View actions part of the MVP architecture.
 */

@SuppressWarnings("WeakerAccess")
public interface ContractMovieDetails {

    interface Presenter {

        void onStop();

        void requestMovieDetails(MovieItem movieItem);

        void requestChangeFavoriteStatus(MovieItem movieItem);

    }

    interface View {

        void displayMovieDetails(MovieItem movieItem);

        void displayVideos(List<MovieVideo> movieVideos);

        void displayReviews(List<MovieReview> movieReviews);

        void displayEmptyVideos();

        void displayEmptyReviews();

    }
}
