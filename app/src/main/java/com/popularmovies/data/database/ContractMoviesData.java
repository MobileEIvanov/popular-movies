package com.popularmovies.data.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by emil.ivanov on 3/13/18.
 */

public class ContractMoviesData {


    public static final String AUTHORITY = "com.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        static final String TABLE_NAME = "movies";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI
                        .buildUpon()
                        .appendPath(PATH_MOVIES).build();



        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_IS_FAVORITE = "isFavorite";
        public static final String COLUMN_TIMESTAMP = "timestamp";


        static final String SQL_CREATE_MOVIES__TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_IS_FAVORITE + " BOOLEAN NOT NULL, " +
                MovieEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";
    }

}
