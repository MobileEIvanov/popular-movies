package com.popularmovies.presentation.movies;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.popularmovies.R;
import com.popularmovies.data.database.MovieDaoImpl;
import com.popularmovies.databinding.ActivityMovieListBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.presentation.details.MovieDetailActivity;
import com.popularmovies.utils.EqualSpacingItemDecoration;
import com.popularmovies.utils.UtilsAnimations;
import com.popularmovies.utils.UtilsConfiguration;
import com.popularmovies.utils.UtilsGrid;
import com.popularmovies.utils.UtilsNetworkConnection;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;


/**
 * An activity representing a collection of {@link MovieItem}. Upon user interaction wih item
 * he will be navigated to  {@link MovieDetailActivity} representing
 * item details. There is share element transition used for the navigation.
 */
public class MovieCollectionActivity extends AppCompatActivity implements ContractMoviesScreen.View, AdapterMovieCollection.ICollectionInteraction {

    public static final int REQUEST_MOVIE_DETAILS = 12356;
    private static final String TAG = "MovieCollection";

    private ActivityMovieListBinding mBinding;
    private UtilsConfiguration mUtilsConfig;
    private static final String DEFAULT_MOVIE_CATEGORY = "top_rated";
    private static final String MOVIE_CATEGORY_POPULAR = "popular";
    private static final String MOVIE_CATEGORY_FAVORITES = "favorites";
    private static final long DEFAULT_COLLECTION_PAGE = 1;

    private PresenterMovieCollection mPresenter;
    private AdapterMovieCollection mAdapterMovies;

    private List<MovieItem> mMovieColection;

    private final IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);


    private final BroadcastReceiver mConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final ConnectivityManager connMgr;
            connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connMgr == null) {
                return;
            }

            final android.net.NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


            if (wifi.isAvailable() || mobile.isAvailable()) {

                if (wifi.isConnected() || mobile.isConnected()) {
                    mBinding.layoutNoConnection.getRoot().setVisibility(View.INVISIBLE);
                    if (mBinding.layoutMovieList.movieList.getVisibility() == View.INVISIBLE) {
                        UtilsAnimations.createCircularReveal(mBinding.layoutMovieList.movieList);
                        loadScreenData(true);
                    }

                } else {
                    if (!mUtilsConfig.getCurrentMovieCategory().equals(MOVIE_CATEGORY_FAVORITES)) {
                        mBinding.emptyList.getRoot().setVisibility(View.INVISIBLE);
                        UtilsAnimations.createCircularReveal(mBinding.layoutNoConnection.getRoot());
                        mBinding.layoutMovieList.movieList.setVisibility(View.INVISIBLE);
                    } else {
                        mBinding.layoutMovieList.movieList.setVisibility(View.VISIBLE);
                        mBinding.layoutNoConnection.getRoot().setVisibility(View.INVISIBLE);
                        loadScreenData(true);
                    }
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);


        String category = UtilsConfiguration.getInstance(this).getCurrentMovieCategory();
        if (category != null) {
            setActionBarTitle(category);
        }
        mPresenter = new PresenterMovieCollection(this, new MovieDaoImpl(this));


        mUtilsConfig = UtilsConfiguration.getInstance(this);

        if (savedInstanceState != null) {
            mMovieColection = savedInstanceState.getParcelableArrayList(MovieItem.MOVIE_DATA);
            displayMovies(mMovieColection);
        } else {
            loadScreenData(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPresenter != null) {
            mPresenter.onResume();
        }
        this.registerReceiver(mConnectionReceiver, filter);
    }

    @Override
    protected void onPause() {
        this.unregisterReceiver(mConnectionReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mPresenter != null) {
            mPresenter.onStop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mUtilsConfig.getCurrentMovieCategory().equals(MOVIE_CATEGORY_FAVORITES) &&
                requestCode == REQUEST_MOVIE_DETAILS
                && data != null) {
            MovieItem movieItem = data.getParcelableExtra(MovieItem.MOVIE_DATA);
            mAdapterMovies.removeItemFromFavorite(movieItem);
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

            case R.id.filter_top_rated:
                if (!mUtilsConfig.getCurrentMovieCategory().equals(DEFAULT_MOVIE_CATEGORY)) {
                    mUtilsConfig.storeCurrentUserSelection(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
                    mPresenter.requestMoviesByCategory(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
                    setActionBarTitle(DEFAULT_MOVIE_CATEGORY);
                }
                return true;

            case R.id.filter_most_popular:
                if (!mUtilsConfig.getCurrentMovieCategory().equals(MOVIE_CATEGORY_POPULAR)) {
                    mUtilsConfig.storeCurrentUserSelection(MOVIE_CATEGORY_POPULAR, DEFAULT_COLLECTION_PAGE);
                    mPresenter.requestMoviesByCategory(MOVIE_CATEGORY_POPULAR, DEFAULT_COLLECTION_PAGE);

                    setActionBarTitle(MOVIE_CATEGORY_POPULAR);
                }
                return true;


            case R.id.filter_favorites:
                if (!mUtilsConfig.getCurrentMovieCategory().equals(MOVIE_CATEGORY_FAVORITES)) {
                    mUtilsConfig.storeCurrentUserSelection(MOVIE_CATEGORY_FAVORITES, DEFAULT_COLLECTION_PAGE);
                    mPresenter.requestFavorites();
                    setActionBarTitle(MOVIE_CATEGORY_FAVORITES);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setActionBarTitle(String currentFilter) {
        if (getSupportActionBar() == null) {
            return;
        }

        if (currentFilter != null) {

            switch (currentFilter) {
                case DEFAULT_MOVIE_CATEGORY:
                    getSupportActionBar().setTitle(R.string.label_top_rated);
                    break;
                case MOVIE_CATEGORY_POPULAR:
                    getSupportActionBar().setTitle(R.string.label_popular);
                    break;
                case MOVIE_CATEGORY_FAVORITES:
                    getSupportActionBar().setTitle(R.string.label_favorites);
                    break;
            }
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }


    private void loadScreenData(boolean isInitialLoad) {


        if (UtilsNetworkConnection.checkInternetConnection(this) && !mUtilsConfig.getCurrentMovieCategory().equals(MOVIE_CATEGORY_FAVORITES)) {
            if (mUtilsConfig.getImageBaseURL() != null && mUtilsConfig.getImageMinSize() != null) {

                if (mUtilsConfig.getCurrentMovieCategory() != null && !mUtilsConfig.getCurrentMovieCategory().isEmpty()) {

                    if (isInitialLoad) {
                        mPresenter.requestMoviesByCategory(mUtilsConfig.getCurrentMovieCategory(), DEFAULT_COLLECTION_PAGE);
                        mUtilsConfig.storeCurrentCollectionPage(DEFAULT_COLLECTION_PAGE);
                    } else {
                        mPresenter.requestMoviesByCategory(mUtilsConfig.getCurrentMovieCategory(), mUtilsConfig.getCurrentCollectionPage());
                    }
                } else {
                    mUtilsConfig.storeCurrentUserSelection(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
                    mPresenter.requestMoviesByCategory(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
                }
            } else {
                mPresenter.requestConfigurations();
            }

        } else {
            mBinding.layoutNoConnection.getRoot().setVisibility(View.INVISIBLE);
            mPresenter.requestFavorites();
            mUtilsConfig.storeCurrentUserSelection(MOVIE_CATEGORY_FAVORITES, DEFAULT_COLLECTION_PAGE);

        }
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
            mAdapterMovies = new AdapterMovieCollection(this, movieItemList, this, imagesBaseUrl);
            mBinding.layoutMovieList.movieList.setLayoutManager(new GridLayoutManager(this, columnSize));
            mBinding.layoutMovieList.movieList.setAdapter(mAdapterMovies);
            mBinding.layoutMovieList.movieList.addItemDecoration(new EqualSpacingItemDecoration(16, EqualSpacingItemDecoration.VERTICAL));
        } else {
            if (mUtilsConfig.getCurrentCollectionPage() > 1) {
                mMovieColection.addAll(movieItemList);
                mAdapterMovies.addItemsCollection(movieItemList);
            } else {
                mAdapterMovies.updateCollection(movieItemList);
                mMovieColection = movieItemList;
            }
        }

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onMovieSelected(MovieItem movieItem, View imageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(MovieCollectionActivity.this,
                        imageView,
                        ViewCompat.getTransitionName(imageView));
        // Pass data object in the bundle and populate details activity.
        intent.putExtra(MovieItem.MOVIE_DATA, movieItem);
        startActivityForResult(intent, REQUEST_MOVIE_DETAILS, options.toBundle());
    }

    @Override
    public void onLoadMore() {
        if (!mUtilsConfig.getCurrentMovieCategory().equals(MOVIE_CATEGORY_FAVORITES)) {
            long page = mUtilsConfig.getCurrentCollectionPage() + 1;
            mUtilsConfig.storeCurrentCollectionPage(page);

            if (!mUtilsConfig.getCurrentMovieCategory().isEmpty()) {
                mPresenter.requestMoviesByCategory(mUtilsConfig.getCurrentMovieCategory(), page);
            } else {

                mUtilsConfig.storeCurrentUserSelection(DEFAULT_MOVIE_CATEGORY, page);
                mPresenter.requestMoviesByCategory(DEFAULT_MOVIE_CATEGORY, page);
            }

        }
    }

    @Override
    public void showEmptyList() {
        mBinding.layoutMovieList.movieList.setVisibility(View.INVISIBLE);
        UtilsAnimations.createCircularReveal(mBinding.emptyList.getRoot());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MovieItem.MOVIE_DATA, (ArrayList<? extends Parcelable>) mMovieColection);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void storeImageBaseUrlLocally(String imageBaseURL) {
        boolean isSuccess = mUtilsConfig.storeImageBaseURL(imageBaseURL);
        if (isSuccess) {
            mPresenter.requestMoviesByCategory(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
        } else {
            showEmptyView();
        }
    }

    @Override
    public void storeStillImageSizesLocally(List<String> stillImageSizes) {
        for (int i = 0; i < stillImageSizes.size(); i++) {
            mUtilsConfig.storeAllowedStillSize(i, stillImageSizes.get(i));
        }
    }

    @Override
    public void onConfigurationRequestFailure() {
        Log.d(TAG, "onConfigurationRequestFailure: ");
    }

    @Override
    public void displayMovies(List<MovieItem> movieItemList) {
        if (movieItemList == null) {
            return;
        }

        String imageBaseUrl = mUtilsConfig.getImageBaseURL() + "/" + mUtilsConfig.getImageMediumSize();
        setupRecyclerView(movieItemList, imageBaseUrl);
        mBinding.emptyList.getRoot().setVisibility(View.INVISIBLE);
        mBinding.layoutMovieList.movieList.setVisibility(View.VISIBLE);

    }

    @Override
    public void showEmptyView() {
        mBinding.emptyList.getRoot().setVisibility(View.VISIBLE);
        mBinding.layoutMovieList.movieList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorMessage(String message) {
        Log.d(TAG, "showErrorMessage: " + message);
        Snackbar.make(mBinding.getRoot(), R.string.error_api_issue, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgressLoader() {
        if (mBinding.progressBar.getVisibility() != View.VISIBLE) {
            mBinding.progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressLoader() {
        if (mBinding.progressBar.getVisibility() != View.GONE) {
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }
}
