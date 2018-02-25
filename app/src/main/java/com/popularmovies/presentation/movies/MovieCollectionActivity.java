package com.popularmovies.presentation.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.popularmovies.R;
import com.popularmovies.databinding.ActivityMovieListBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.presentation.AdapterMovieCollection;
import com.popularmovies.presentation.MovieDetailActivity;
import com.popularmovies.utils.UtilsConfiguration;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieCollectionActivity extends AppCompatActivity implements ContractMoviesScreen.View, AdapterMovieCollection.ICollectionInteraction {

    private static final String TAG = "MovieCollection";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private ActivityMovieListBinding mBinding;
    private static final int GRID_COLUMN_COUNT = 2;
    private UtilsConfiguration mImageConfig;
    private static String DEFAULT_MOVIE_CATEGORY = "top_rated";
    private static long DEFAULT_COLLECTION_PAGE = 1;

    private PresenterMovieCollection mPresenter;
    private AdapterMovieCollection mAdapterMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);

        Fresco.initialize(this);
        initToolbar();

        mPresenter = new PresenterMovieCollection(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadScreenData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    private void loadScreenData() {
        mImageConfig = UtilsConfiguration.getInstance(this);
        if (mImageConfig.getImageBaseURL() != null && mImageConfig.getImageMinSize() != null) {
            mPresenter.requestMoviesByCategory(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
        } else {
            mPresenter.requestConfigurations();
        }
    }

    private void initToolbar() {

        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle(getTitle());
    }

    /**
     * Setup adapter and load the current movie collection.
     *
     * @param movieItemList - movie list retrieved
     * @param imagesBaseUrl - the base url used to load images content
     */
    private void setupRecyclerView(@Nonnull List<MovieItem> movieItemList, String imagesBaseUrl) {

        if (mAdapterMovies == null) {
            mAdapterMovies = new AdapterMovieCollection(movieItemList, this, imagesBaseUrl);
            mBinding.layoutMovieList.movieList.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN_COUNT));
            mBinding.layoutMovieList.movieList.setAdapter(mAdapterMovies);
        } else {
            mAdapterMovies.updateCollection(movieItemList);
        }
    }


    @Override
    public void onMovieSelected(MovieItem movieItem, View imageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);



        // Pass data object in the bundle and populate details activity.

        intent.putExtra(MovieItem.MOVIE_DATA, movieItem);
//        ActivityOptionsCompat options = ActivityOptionsCompat.
//                makeSceneTransitionAnimation(this, imageView, getString(R.string.share_component_list_details));
        startActivity(intent);
    }


    @Override
    public void storeImageBaseUrlLocally(String imageBaseURL) {
        boolean isSuccess = mImageConfig.storeImageBaseURL(imageBaseURL);
        if (isSuccess) {
            mPresenter.requestMoviesByCategory(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
        } else {
            showEmptyView();
        }
    }

    @Override
    public void storeStillImageSizesLocally(List<String> stillImageSizes) {
        for (int i = 0; i < stillImageSizes.size(); i++) {
            mImageConfig.storeAllowedStillSize(i, stillImageSizes.get(i));
        }
    }

    @Override
    public void onConfigurationRequestFailure() {

    }

    @Override
    public void displayMovies(List<MovieItem> movieItemList) {
        if (mImageConfig.getImageBaseURL() == null) {
            showEmptyView();
            return;
        }

        if (mImageConfig.getImageMediumSize() == null) {
            return;
        }

        String imageBaseUrl = mImageConfig.getImageBaseURL() + "/" + mImageConfig.getImageMediumSize();
        Log.d(TAG, "displayMovies: " + imageBaseUrl);
        setupRecyclerView(movieItemList, imageBaseUrl);
    }

    @Override
    public void showEmptyView() {
        // TODO: 2/25/18 Show empty view
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }
}
