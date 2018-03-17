package com.popularmovies.presentation.movies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.popularmovies.R;
import com.popularmovies.data.database.DatabaseHelper;
import com.popularmovies.data.database.MovieDaoImpl;
import com.popularmovies.data.database.TestUtil;
import com.popularmovies.databinding.ActivityMovieListBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.presentation.details.MovieDetailActivity;
import com.popularmovies.utils.EqualSpacingItemDecoration;
import com.popularmovies.utils.GridSpacingItemDecoration;
import com.popularmovies.utils.UtilsAnimations;
import com.popularmovies.utils.UtilsConfiguration;
import com.popularmovies.utils.UtilsGrid;
import com.popularmovies.utils.UtilsNetworkConnection;

import java.util.List;

import javax.annotation.Nonnull;


/**
 * An activity representing a collection of {@link MovieItem}. Upon user interaction wih item
 * he will be navigated to  {@link MovieDetailActivity} representing
 * item details. There is share element transition used for the navigation.
 */
public class MovieCollectionActivity extends AppCompatActivity implements ContractMoviesScreen.View, AdapterMovieCollection.ICollectionInteraction {

    private static final String TAG = "MovieCollection";
    private ActivityMovieListBinding mBinding;
    private UtilsConfiguration mUtilsConfig;
    private static final String DEFAULT_MOVIE_CATEGORY = "top_rated";
    private static final String MOVIE_CATEGORY_POPULAR = "popular";
    private static final String MOVIE_CATEGORY_FAVORITES = "favorites";
    private static final long DEFAULT_COLLECTION_PAGE = 1;

    private PresenterMovieCollection mPresenter;
    private AdapterMovieCollection mAdapterMovies;


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
                        loadScreenData(true);
                        UtilsAnimations.createCircularReveal(mBinding.layoutMovieList.movieList);
                    }

                } else {

                    mBinding.emptyList.getRoot().setVisibility(View.INVISIBLE);
                    UtilsAnimations.createCircularReveal(mBinding.layoutNoConnection.getRoot());
                    mBinding.layoutMovieList.movieList.setVisibility(View.INVISIBLE);


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

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();


//        TestUtil.insertFakeData(db);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_list);


        String category = UtilsConfiguration.getInstance(this).getCurrentMovieCategory();
        if (category != null) {
            setActionBarTitle(category);
        }
        mPresenter = new PresenterMovieCollection(this, new MovieDaoImpl(this));

        if (UtilsNetworkConnection.checkInternetConnection(this)) {
            Stetho.initializeWithDefaults(this);
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

                mPresenter.requestMoviesByCategory(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
                mUtilsConfig.storeCurrentUserSelection(DEFAULT_MOVIE_CATEGORY, DEFAULT_COLLECTION_PAGE);
                setActionBarTitle(DEFAULT_MOVIE_CATEGORY);
                return true;

            case R.id.filter_most_popular:


                mPresenter.requestMoviesByCategory(MOVIE_CATEGORY_POPULAR, DEFAULT_COLLECTION_PAGE);
                mUtilsConfig.storeCurrentUserSelection(MOVIE_CATEGORY_POPULAR, DEFAULT_COLLECTION_PAGE);
                setActionBarTitle(MOVIE_CATEGORY_POPULAR);
                return true;


            case R.id.filter_favorites:

                mPresenter.requestFavorites();
                mUtilsConfig.storeCurrentUserSelection(MOVIE_CATEGORY_FAVORITES, DEFAULT_COLLECTION_PAGE);
                setActionBarTitle(MOVIE_CATEGORY_FAVORITES);
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
            }
        } else {
            getSupportActionBar().setTitle(R.string.app_name);
        }
    }


    private void loadScreenData(boolean isInitialLoad) {
        mUtilsConfig = UtilsConfiguration.getInstance(this);

        if (mUtilsConfig.getImageBaseURL() != null && mUtilsConfig.getImageMinSize() != null) {

            if (mUtilsConfig.getCurrentMovieCategory() != null) {

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
                mAdapterMovies.addItemsCollection(movieItemList);
            } else {
                mAdapterMovies.updateCollection(movieItemList);
            }
        }
    }

    @Override
    public void onMovieSelected(MovieItem movieItem, View imageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(MovieCollectionActivity.this,
                        imageView,
                        ViewCompat.getTransitionName(imageView));
        // Pass data object in the bundle and populate details activity.
        intent.putExtra(MovieItem.MOVIE_DATA, movieItem);
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onLoadMore() {
        long page = mUtilsConfig.getCurrentCollectionPage() + 1;
        mUtilsConfig.storeCurrentCollectionPage(page);
        mPresenter.requestMoviesByCategory(mUtilsConfig.getCurrentMovieCategory(), page);
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

    }

    @Override
    public void displayMovies(List<MovieItem> movieItemList) {
        if (mUtilsConfig.getImageBaseURL() == null) {
            showEmptyView();
            return;
        }

        if (mUtilsConfig.getImageMediumSize() == null) {
            return;
        }

        String imageBaseUrl = mUtilsConfig.getImageBaseURL() + "/" + mUtilsConfig.getImageMediumSize();
        Log.d(TAG, "displayMovies: " + imageBaseUrl);
        setupRecyclerView(movieItemList, imageBaseUrl);
    }

    @Override
    public void showEmptyView() {
        mBinding.emptyList.getRoot().setVisibility(View.VISIBLE);
        mBinding.layoutMovieList.movieList.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG).show();
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
