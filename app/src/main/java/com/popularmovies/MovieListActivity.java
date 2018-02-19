package com.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.popularmovies.databinding.ActivityMovieListBinding;
import com.popularmovies.entities.MovieItem;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements AdapterMovieCollection.ICollectionInteraction {

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

        mBinding= DataBindingUtil.setContentView(this ,R.layout.activity_movie_list);


        initToolbar();
        setupRecyclerView();
    }


    private void initToolbar(){

        setSupportActionBar(mBinding.toolbar);
        mBinding.toolbar.setTitle(getTitle());
    }


    private void setupRecyclerView() {
        mBinding.layoutMovieList.movieList.setLayoutManager(new GridLayoutManager(this, GRID_COLUMN_COUNT));
        mBinding.layoutMovieList.movieList.setAdapter(new AdapterMovieCollection(MovieItem.generateMovies(),this));
    }


    @Override
    public void onMovieSelected() {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        startActivity(intent);
    }
}
