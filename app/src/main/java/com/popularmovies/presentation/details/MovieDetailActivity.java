package com.popularmovies.presentation.details;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.popularmovies.R;
import com.popularmovies.data.RestClient;
import com.popularmovies.data.RestDataSource;
import com.popularmovies.data.models.MovieReviewsResponse;
import com.popularmovies.data.models.MoviesResponse;
import com.popularmovies.data.models.MoviesVideoResponse;
import com.popularmovies.databinding.ActivityMovieDetailBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.entities.MovieReview;
import com.popularmovies.entities.MovieVideo;
import com.popularmovies.utils.EqualSpacingItemDecoration;
import com.popularmovies.utils.GridSpacingItemDecoration;
import com.popularmovies.utils.UtilsConfiguration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * An activity representing a single {@link MovieItem} detail screen.
 */
public class MovieDetailActivity extends AppCompatActivity implements ContractMovieDetails.View, AdapterMovieVideos.ICollectionVideosInteraction, AdapterMovieReviews.ICollectionReviewInteraction {

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

    private final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    private PresenterMovieDetails mPresenter;

    private final BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ConnectivityManager connMgr;
            connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr != null) {
                final android.net.NetworkInfo wifi = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                final android.net.NetworkInfo mobile = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


                if (wifi.isAvailable() || mobile.isAvailable()) {

                    if (wifi.isConnected() || mobile.isConnected()) {


                        mBinding.layoutNoConnection.getRoot().setVisibility(View.INVISIBLE);
                        if (mBinding.contentDetails.getVisibility() == View.INVISIBLE) {
                            mBinding.contentDetails.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (mBinding.layoutNoConnection.getRoot().getVisibility() == View.INVISIBLE) {
                            mBinding.layoutNoConnection.getRoot().setVisibility(View.VISIBLE);
                            mBinding.contentDetails.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
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


        mPresenter = new PresenterMovieDetails(this);
        mPresenter.requestMovieVideos(mMovieItem.getId());
        mPresenter.requestMovieReviews(mMovieItem.getId());


    }


    @Override
    protected void onResume() {
        super.onResume();
        this.registerReceiver(mConnectionReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(mConnectionReceiver);
    }

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        populateMovieData();
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

        displayMovieRating(mMovieItem.getVoteAverage());
    }

    /**
     * Shows the movie rating value and background color based on intervals
     * Below RATING_BAR_LOW - red background
     * From RATING_BAR_LOW to RATING_BAR_HIGH - blue background
     * From RATING_BAR_HIGH - green background
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

        String imagePosterUrl = mImageConfig.getImageBaseURL() + mImageConfig.getImageMaxSize() + mMovieItem.getPosterPath();

        Picasso.with(this).load(imagePosterUrl)
                // TODO: 3/5/18  Add placeholder image and on Error
//                .placeholder(R.drawable.user_placeholder)
//                .error(R.drawable.user_placeholder_error)
                .fit().noFade().into(mBinding.ivThumbMovie, new Callback() {
            @Override
            public void onSuccess() {
                if (isInitialLoad) {
                    supportStartPostponedEnterTransition();
                } else {
                    populateMovieData();
                }
            }

            @Override
            public void onError() {
                if (isInitialLoad) {
                    supportStartPostponedEnterTransition();
                } else {
                    populateMovieData();
                }
            }
        });


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
        supportFinishAfterTransition();
        super.onBackPressed();
    }

    @Override
    public void displayVideos(List<MovieVideo> movieVideos) {
        AdapterMovieVideos adapterMovieVideos = new AdapterMovieVideos(this, movieVideos, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvMovieVideos.setLayoutManager(layoutManager);
        mBinding.rvMovieVideos.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));
        mBinding.rvMovieVideos.setAdapter(adapterMovieVideos);
    }

    @Override
    public void displayReviews(List<MovieReview> movieReviews) {
        AdapterMovieReviews adapterMovieReviews = new AdapterMovieReviews(this, movieReviews, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvMovieReviews.setLayoutManager(layoutManager);
        mBinding.rvMovieReviews.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.HORIZONTAL));

        mBinding.rvMovieReviews.setAdapter(adapterMovieReviews);


    }

    @Override
    public void displayEmptyVideos() {

    }

    @Override
    public void displayEmptyReviews() {

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
    public void onReviewSelected(MovieReview movieReviewItem) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieReviewItem.getUrl()));
        startActivity(webIntent);
    }

    @Override
    public void onLoadMore() {

    }
}
