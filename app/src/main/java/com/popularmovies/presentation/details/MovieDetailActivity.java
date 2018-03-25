package com.popularmovies.presentation.details;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.popularmovies.R;
import com.popularmovies.data.database.MovieDaoImpl;
import com.popularmovies.databinding.ActivityMovieDetailBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.entities.MovieReview;
import com.popularmovies.entities.MovieVideo;
import com.popularmovies.presentation.movies.MovieCollectionActivity;
import com.popularmovies.utils.EqualSpacingItemDecoration;
import com.popularmovies.utils.UtilsAnimations;
import com.popularmovies.utils.UtilsConfiguration;
import com.popularmovies.utils.UtilsNetworkConnection;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * An activity representing a single {@link MovieItem} detail screen.
 */
public class MovieDetailActivity extends AppCompatActivity implements ContractMovieDetails.View,
        AdapterMovieVideos.ICollectionVideosInteraction,
        AdapterMovieReviews.ICollectionReviewInteraction {

    private static final float RATING_BAR_LOW = 5.0f;
    private static final float RATING_BAR_HIGH = 7.0f;
    private static final float ANIMATION_DOWN_SCALE_FACTOR = 0.8f;
    private static final float ANIMATION_UP_SCALE_FACTOR = 1.0f;
    private static final int ANIMATION_DELAY = 300;
    private static final int ANIMATION_DURATION = 300;
    private static final int ANIMATE_ALPHA_DOWN = 0;
    private static final int ANIMATE_ALPHA_UP = 100;

    private ActivityMovieDetailBinding mBinding;

    private MovieItem mMovieItem;
    private UtilsConfiguration mImageConfig;

    private PresenterMovieDetails mPresenter;

    private AdapterMovieVideos mAdapterVideos;
    private AdapterMovieReviews mAdapterReviews;

    private final View.OnClickListener mListenerChangeIsFavorite = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            if (mMovieItem.isFavorite()) {
                mMovieItem.setFavorite(false);
                mBinding.fabMarkAsFavorite.setSelected(false);
            } else {
                mMovieItem.setFavorite(true);
                mBinding.fabMarkAsFavorite.setSelected(true);
            }

            mPresenter.requestChangeFavoriteStatus(mMovieItem);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail);
        mImageConfig = UtilsConfiguration.getInstance(this);

        if (savedInstanceState != null) {
            mMovieItem = savedInstanceState.getParcelable(MovieItem.MOVIE_DATA);
            requestMoviePoster(false);
        } else {
            mMovieItem = getIntent().getParcelableExtra(MovieItem.MOVIE_DATA);
            requestMoviePoster(true);
        }


        if (mMovieItem != null) {
            setActionBarTitle(mMovieItem.getTitle());
        }


        mPresenter = new PresenterMovieDetails(this, new MovieDaoImpl(this));

        mBinding.fabMarkAsFavorite.setOnClickListener(mListenerChangeIsFavorite);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        loadMovieData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(MovieItem.MOVIE_DATA, mMovieItem);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Displays the {@link MovieItem} data or finishes the activity if such is no provided.
     */
    private void populateMovieData() {
        if (mMovieItem == null) {
            Toast.makeText(this, getString(R.string.message_movie_data_failure), Toast.LENGTH_LONG).show();
            finish();
            return;

        }
        if (mMovieItem.getOriginalTitle() != null && !mMovieItem.getOriginalTitle().isEmpty()) {
            mBinding.tvMovieTitleOriginal.setText(mMovieItem.getOriginalTitle());
        } else {
            mBinding.tvMovieTitleOriginal.setText(R.string.text_empty_data_item);
        }

        if (mMovieItem.getReleaseDate() != null && !mMovieItem.getReleaseDate().isEmpty()) {
            mBinding.tvReleaseDate.setText(mMovieItem.getReleaseDate());
        } else {
            mBinding.tvReleaseDate.setText(R.string.text_empty_data_item);
        }

        if (mMovieItem.getOverview() != null && !mMovieItem.getOverview().isEmpty()) {
            mBinding.tvPlotSynopsis.setText(mMovieItem.getOverview());
        } else {
            mBinding.tvPlotSynopsis.setText(R.string.text_empty_data_item);
        }

        mBinding.fabMarkAsFavorite.setSelected(mMovieItem.isFavorite());


        displayMovieRating(mMovieItem.getVoteAverage());

    }

    /**
     * Shows the movie rating value and background color based on intervals
     * Below {@link MovieDetailActivity#RATING_BAR_LOW} - red background
     * From {@link MovieDetailActivity#RATING_BAR_LOW} to {@link MovieDetailActivity#RATING_BAR_HIGH} - blue background
     * From {@link MovieDetailActivity#RATING_BAR_HIGH} - green background
     *
     * @param movieRating - {@link MovieItem} rating
     */
    private void displayMovieRating(float movieRating) {
        if (movieRating <= RATING_BAR_LOW) {
            mBinding.layoutRatingView.flRatingContainer.setBackgroundResource(R.drawable.rating_low_circle_backround);
        } else if (movieRating > RATING_BAR_LOW && movieRating <= RATING_BAR_HIGH) {
            mBinding.layoutRatingView.flRatingContainer.setBackgroundResource(R.drawable.rating_medium_circle_background);
        } else if (movieRating > RATING_BAR_HIGH) {
            mBinding.layoutRatingView.flRatingContainer.setBackgroundResource(R.drawable.rating_high_circle_backround);
        }
        mBinding.layoutRatingView.tvMovieRating.setText(String.valueOf(movieRating));
        mBinding.layoutRatingView.flRatingContainer.setAlpha(ANIMATE_ALPHA_DOWN);
        mBinding.layoutRatingView.flRatingContainer.setScaleX(ANIMATION_DOWN_SCALE_FACTOR);
        mBinding.layoutRatingView.flRatingContainer.setScaleY(ANIMATION_DOWN_SCALE_FACTOR);
        mBinding.layoutRatingView.flRatingContainer.setVisibility(View.VISIBLE);

        mBinding.layoutRatingView.flRatingContainer.animate()
                .alpha(ANIMATE_ALPHA_UP)
                .scaleX(ANIMATION_UP_SCALE_FACTOR)
                .scaleY(ANIMATION_UP_SCALE_FACTOR)
                .setDuration(ANIMATION_DURATION)
                .setStartDelay(ANIMATION_DELAY);
    }

    /**
     * Requests, movie poster base on the {@link MovieItem} poster url and {@link com.popularmovies.data.models.ImageConfiguration}
     *
     * @param isInitialLoad - parameter shows if this is the initial load from collection activity or after recreation.
     */
    private void requestMoviePoster(boolean isInitialLoad) {
        if (mImageConfig == null || mMovieItem == null) {

            Toast.makeText(this, getString(R.string.message_movie_data_failure), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String imagePosterUrl = mImageConfig.getImageBaseURL() + "/" + mImageConfig.getImageMediumSize() + mMovieItem.getPosterPath();


        Picasso.with(this).load(imagePosterUrl)
                .placeholder(R.color.colorPrimary)
                .error(R.drawable.empty_image)
                .fit()
                .noFade()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mBinding.ivThumbMovie, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (isInitialLoad) {
                            supportStartPostponedEnterTransition();
                        } else {
                            loadMovieData();
                        }
                    }

                    @Override
                    public void onError() {
                        if (isInitialLoad) {
                            supportStartPostponedEnterTransition();
                        } else {
                            loadMovieData();
                        }
                    }
                });


    }

    private void loadMovieData() {
        populateMovieData();
        if (UtilsNetworkConnection.checkInternetConnection(MovieDetailActivity.this)) {
            mPresenter.requestMovieDetails(mMovieItem);
        } else {
            displayEmptyVideos();
        }

    }

    private void setActionBarTitle(String actionBarTitle) {
        if (actionBarTitle == null) {
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
        }
    }

    @Override
    public void onBackPressed() {
        mBinding.layoutRatingView.flRatingContainer.setVisibility(View.INVISIBLE);

        setResult(MovieCollectionActivity.REQUEST_MOVIE_DETAILS,
                getIntent().putExtra(MovieItem.MOVIE_DATA, mMovieItem));

        supportFinishAfterTransition();
    }

    @Override
    public void displayMovieDetails(MovieItem movieItem) {
        mMovieItem = movieItem;

        mBinding.fabMarkAsFavorite.setSelected(mMovieItem.isFavorite());

        if (mMovieItem.getReviews() != null && mMovieItem.getReviews().getMovieReviewsItems().size() > 0) {
            displayReviews(mMovieItem.getReviews().getMovieReviewsItems());
        } else {
            displayEmptyReviews();
        }

        if (mMovieItem.getVideos() != null && mMovieItem.getVideos().getMovieVideos().size() > 0) {
            displayVideos(mMovieItem.getVideos().getMovieVideos());
        } else {
            displayEmptyVideos();
        }
    }


    @Override
    public void displayVideos(List<MovieVideo> movieVideos) {

        if (mAdapterVideos == null) {
            mAdapterVideos = new AdapterMovieVideos(movieVideos, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mBinding.rvMovieVideos.setLayoutManager(layoutManager);
            mBinding.rvMovieVideos.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));
            mBinding.rvMovieVideos.setAdapter(mAdapterVideos);
            UtilsAnimations.createCircularReveal(mBinding.rvMovieVideos);
        }
    }

    @Override
    public void displayReviews(List<MovieReview> movieReviews) {

        if (mAdapterReviews == null) {
            mAdapterReviews = new AdapterMovieReviews(movieReviews, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            mBinding.rvMovieReviews.setLayoutManager(layoutManager);
            mBinding.rvMovieReviews.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));
            mBinding.rvMovieReviews.setAdapter(mAdapterReviews);
        }
    }

    @Override
    public void displayEmptyVideos() {
        mBinding.flMovieVideos.setVisibility(View.GONE);
    }

    @Override
    public void displayEmptyReviews() {
        mBinding.flMovieReview.setVisibility(View.GONE);
    }


    @Override
    public void onVideoSelected(MovieVideo videoItem) {

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoItem.getVideoKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + videoItem.getVideoKey()));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }


        Snackbar.make(mBinding.getRoot(), "Video: " + videoItem.getName(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onShareVideo(MovieVideo videoItem) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String uriYoutube = String.valueOf(Uri.parse(getString(R.string.uri_path_youtube) + videoItem.getVideoKey()));
        String webYoutube = getString(R.string.url_path_youtube_app) + videoItem.getVideoKey();

        String shareMessage = getString(R.string.share_message_watch_youtube_app) +
                "\n\n" +
                uriYoutube +
                "\n\n" +
                getString(R.string.share_message_watch_on_web) +
                "\n\n" +
                webYoutube +
                "\n\n" +
                getString(R.string.share_message_enjoy);


        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mMovieItem.getTitle() + "\n" + getString(R.string.share_message_subject));
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_message_dialog_title)));
    }

    @Override
    public void onReviewSelected(MovieReview movieReviewItem) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieReviewItem.getUrl()));
        startActivity(webIntent);
    }


}
