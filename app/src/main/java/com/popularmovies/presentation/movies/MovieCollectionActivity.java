package com.popularmovies.presentation.movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.popularmovies.R;
import com.popularmovies.databinding.ActivityMovieListBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.presentation.AdapterMovieCollection;
import com.popularmovies.presentation.MovieDetailActivity;
import com.popularmovies.utils.UtilsConfiguration;
import com.popularmovies.utils.UtilsGrid;

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
    private static String MOVIE_CATEGORY_POPULAR = "popular";
    private static long DEFAULT_COLLECTION_PAGE = 1;

    private PresenterMovieCollection mPresenter;
    private AdapterMovieCollection mAdapterMovies;


    private IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);


    BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ConnectivityManager connMgr;
            connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


            if (wifi.isAvailable() || mobile.isAvailable()) {

                if (wifi.isConnected() || mobile.isConnected()) {

                    loadScreenData();

                    mBinding.layoutNoConnection.getRoot().setVisibility(View.INVISIBLE);
                    mBinding.layoutMovieList.movieList.setVisibility(View.VISIBLE);


                } else {
                    mBinding.layoutNoConnection.getRoot().setVisibility(View.VISIBLE);
                    mBinding.layoutMovieList.movieList.setVisibility(View.INVISIBLE);

                }
            }

        }
    };


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

        this.registerReceiver(mConnectionReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(mConnectionReceiver);
        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_filter_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.filter_most_popular:
                mPresenter.requestMoviesByCategory(MOVIE_CATEGORY_POPULAR, DEFAULT_COLLECTION_PAGE);
                return true;
            case R.id.filter_top_rated:
                mPresenter.requestMoviesByCategory(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
            int columnSize = UtilsGrid.calculateNoOfColumns(this);
            mAdapterMovies = new AdapterMovieCollection(movieItemList, this, imagesBaseUrl);
            mBinding.layoutMovieList.movieList.setLayoutManager(new GridLayoutManager(this, columnSize));
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
//      ActivityOptionsCompat options = ActivityOptionsCompat.
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
        mBinding.emptyList.getRoot().setVisibility(View.VISIBLE);
        mBinding.layoutMovieList.movieList.setVisibility(View.INVISIBLE);
        // TODO: 2/25/18 Show empty view
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
    }
}
