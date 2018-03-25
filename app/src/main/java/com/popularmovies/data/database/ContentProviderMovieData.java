package com.popularmovies.data.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.popularmovies.data.database.ContractMoviesData.*;
import static com.popularmovies.data.database.ContractMoviesData.MovieEntry.TABLE_NAME;

/**
 * Created by emil.ivanov on 3/17/18.
 * Content provider class used for accessing and storing data for {@link com.popularmovies.entities.MovieItem}
 * the solution is based on the example videos provided by Udacity.
 */

public class ContentProviderMovieData extends ContentProvider {

    private DatabaseHelper mDatabaseHelper;

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;

    @Override
    public boolean onCreate() {
        // Initialise the database.

        Context context = getContext();
        mDatabaseHelper = new DatabaseHelper(context);
        return true;
    }

    private static final UriMatcher sUriMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Define directory matcher
        uriMatcher.addURI(AUTHORITY, PATH_MOVIES, MOVIES);

        // Define single item matcher
        uriMatcher.addURI(AUTHORITY, PATH_MOVIES + "/#", MOVIES_WITH_ID);
        return uriMatcher;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor retCursor;
        switch (match) {

            case MOVIES:

                retCursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case MOVIES_WITH_ID:
                // Get the id from the URI
                String id = uri.getPathSegments().get(1);

                String mSelection = "id=?";
                String[] mSelectionArgs = new String[]{id};
                retCursor = db.query(TABLE_NAME, null, mSelection, mSelectionArgs, null, null, null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor
        if (getContext() != null) {
            retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case MOVIES:
                long recordId = db.insert(TABLE_NAME, null, contentValues);

                if (recordId > 0) {
                    returnUri = ContentUris.withAppendedId(uri, recordId);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }


                break;
            default:
                throw new UnsupportedOperationException("Uri: " + uri);
        }

        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {

        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        int matcher = sUriMatcher.match(uri);

        int deletedTasks;


        switch (matcher) {
            case MOVIES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String whereClause = "id=?";
                String[] selectionArgs = {id};
                deletedTasks = db.delete(TABLE_NAME, whereClause, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported uri: " + uri);
        }

        if (deletedTasks != 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        return deletedTasks;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        //Keep track of if an update occurs
        int tasksUpdated;

        // match code
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES_WITH_ID:
                //update a single task by getting the id
                String id = uri.getPathSegments().get(1);
                //using selections
                tasksUpdated = mDatabaseHelper.getWritableDatabase().update(TABLE_NAME, contentValues, "id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            //set notifications if a task was updated
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
        }

        // return number of tasks updated
        return tasksUpdated;
    }
}
