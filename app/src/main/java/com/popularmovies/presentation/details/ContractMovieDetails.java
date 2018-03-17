package com.popularmovies.presentation.details;

import com.popularmovies.entities.MovieItem;
import com.popularmovies.entities.MovieReview;
import com.popularmovies.entities.MovieVideo;

import java.util.List;

/**
 * Created by emil.ivanov on 3/9/18.
 */

public interface ContractMovieDetails {

    interface Presenter {
        void onResume();

        void onStop();

        void requestMovieVideos(long movieId);

        void requestMovieReviews(long movieId);

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