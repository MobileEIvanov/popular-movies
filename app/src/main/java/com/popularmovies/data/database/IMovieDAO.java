package com.popularmovies.data.database;

import android.database.Cursor;
import android.net.Uri;
import com.popularmovies.entities.MovieItem;
import io.reactivex.Single;

/**
 * Created by emil.ivanov on 3/18/18.
 * <p>
 * Data access interface that marks the valid request that can be made to the Movie database.
 * {@link MovieDaoImpl} holds the implementation of each operation
 */
interface IMovieDAO {


    Single<Boolean> findRecord(MovieItem movieItem);

    Uri createRecord(MovieItem movieItem);

    int updateRecord(MovieItem movieItem);

    boolean isFavorite(MovieItem movieItem);

    Single<Cursor> queryAll(String whereClause, String[] args);

}
