package com.popularmovies.data.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.popularmovies.entities.MovieItem;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by emil.ivanov on 3/18/18.
 */

public class MovieDaoImpl implements IMovieDAO {

    Context mContext;

    public MovieDaoImpl(Context context) {
        mContext = context;
    }


    @Override
    public Single<Boolean> findRecord(MovieItem movieItem) {

        String stringId = String.valueOf(movieItem.getId());
        Uri uri = ContractMoviesData.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);

        boolean hasRecord = false;

        if (cursor != null && cursor.moveToNext()) {
            hasRecord = true;
        }
        cursor.close();

        return Single.just(hasRecord);
    }

    @Override
    public Uri createRecord(MovieItem movieItem) {
        ContentResolver contentResolver = mContext.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_MOVIE_ID, movieItem.getId());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_MOVIE_TITLE, movieItem.getTitle());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_IS_FAVORITE, movieItem.isFavorite());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_POSTER_PATH, movieItem.getPosterPath());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_MOVIE_TITLE_ORIGINAL, movieItem.getOriginalTitle());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movieItem.getReleaseDate());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_MOVIE_PLOT_OVERVIEW, movieItem.getOverview());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_MOVIE_RATING, movieItem.getVoteAverage());

        Uri uri = contentResolver.insert(ContractMoviesData.MovieEntry.CONTENT_URI, contentValues);


        return uri;
    }

    @Override
    public int updateRecord(MovieItem movieItem) {
        String stringId = String.valueOf(movieItem.getId());
        Uri uri = ContractMoviesData.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        ContentResolver contentResolver = mContext.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_MOVIE_ID, movieItem.getId());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_MOVIE_TITLE, movieItem.getTitle());
        contentValues.put(ContractMoviesData.MovieEntry.COLUMN_IS_FAVORITE, movieItem.isFavorite());

        int rowsUpdate = contentResolver.update(uri, contentValues, null, null);
        return rowsUpdate;
    }

    @Override
    public boolean isFavorite(MovieItem movieItem) {

        String stringId = String.valueOf(movieItem.getId());
        Uri uri = ContractMoviesData.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        Cursor cursor = mContext.getContentResolver().query(uri, new String[]{ContractMoviesData.MovieEntry.COLUMN_IS_FAVORITE}, null, null, null);

        int indexFavorite = cursor.getColumnIndex(ContractMoviesData.MovieEntry.COLUMN_IS_FAVORITE);
        boolean isFavorite = false;

        if (cursor != null && cursor.moveToNext()) {
            isFavorite = cursor.getInt(indexFavorite) == 1 ? true : false;
        }
        cursor.close();

        return isFavorite;
    }

    @Override
    public int bulkInsertItems(MovieItem movieItem) {
        return 0;
    }

    @Override
    public Observable<Cursor> query(MovieItem movieItem) {
        return null;
    }

    @Override
    public int deleteRecord(MovieItem movieItem) {

        // Build appropriate uri with String row id appended
        String stringId = String.valueOf(movieItem.getId());
        Uri uri = ContractMoviesData.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        // COMPLETED (2) Delete a single row of data using a ContentResolver
        int deletedRows = mContext.getContentResolver().delete(uri, null, null);
        return deletedRows;
    }

    @Override
    public Single<Cursor> queryAll(String whereClause, String[] args) {

        Uri uri = ContractMoviesData.MovieEntry.CONTENT_URI;
        Cursor cursor = mContext.getContentResolver().query(uri, null, whereClause, args, ContractMoviesData.MovieEntry.COLUMN_TIMESTAMP);

        return Single.just(cursor);
    }
}
