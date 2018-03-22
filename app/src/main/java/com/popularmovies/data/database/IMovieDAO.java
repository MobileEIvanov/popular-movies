package com.popularmovies.data.database;

import android.database.Cursor;
import android.net.Uri;

import com.popularmovies.entities.MovieItem;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by emil.ivanov on 3/18/18.
 */

public interface IMovieDAO {


    Single<Boolean> findRecord(MovieItem movieItem);

    Uri createRecord(MovieItem movieItem);

    int updateRecord(MovieItem movieItem);

    boolean isFavorite(MovieItem movieItem);

    int bulkInsertItems(MovieItem movieItem);

    Observable<Cursor> query(MovieItem movieItem);

    int deleteRecord(MovieItem movieItem);

    Single<Cursor> queryAll(String whereClause, String[] args);


}
