package com.popularmovies.presentation.movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.popularmovies.R;
import com.popularmovies.data.RestClient;
import com.popularmovies.data.RestDataSource;
import com.popularmovies.data.models.ConfigurationResponse;
import com.popularmovies.data.models.MoviesResponse;
import com.popularmovies.databinding.ActivityMovieListBinding;
import com.popularmovies.entities.MovieItem;
import com.popularmovies.presentation.AdapterMovieCollection;
import com.popularmovies.presentation.MovieDetailActivity;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieCollectionActivity extends AppCompatActivity implements ContractMoviesScreen.View, AdapterMovieCollection.ICollectionInteraction {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private ActivityMovieListBinding mBinding;
    private static final int GRID_COLUMN_COUNT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        mBinding= DataBindingUtil.setContentView(this , R.layout.activity_movie_list);


        initToolbar();


        RestDataSource restDataSource = new RestDataSource();

        restDataSource
                .requestConfigurations(RestClient.API_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ConfigurationResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ConfigurationResponse configurationResponse) {
                            if(configurationResponse != null){

                            }
                    }
                });



        restDataSource.requestMoviesByCategory("top_rated",RestClient.API_KEY,1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MoviesResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MoviesResponse moviesResponse) {
                            if(moviesResponse!=null && moviesResponse.getMovieItems()!=null && moviesResponse.getMovieItems().size()>0){
                                setupRecyclerView(moviesResponse.getMovieItems());
                            }
                    }
                });
    }


    private void initToolbar(){

        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle(getTitle());
    }


    private void setupRecyclerView(List<MovieItem> movieItemList) {
        mBinding.layoutMovieList.movieList.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN_COUNT));
        mBinding.layoutMovieList.movieList.setAdapter(new AdapterMovieCollection(movieItemList,this));
    }


    @Override
    public void onMovieSelected(MovieItem movieItem, View imageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        // Pass data object in the bundle and populate details activity.
        intent.putExtra(MovieItem.MOVIE_DATA, movieItem.getPosterPath());
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this,imageView , getString(R.string.share_component_list_details));
        startActivity(intent, options.toBundle());

    }

    @Override
    public void displayMovies(List<MovieItem> movieItemList) {

    }

    @Override
    public void showEmptyView() {

    }

    @Override
    public void showErrorMessage(String message) {

    }
}
