package com.popularmovies.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.popularmovies.data.database.ContractMoviesData.*;
import static com.popularmovies.data.database.ContractMoviesData.MovieEntry.SQL_CREATE_MOVIES__TABLE;

/**
 * Created by emil.ivanov on 3/13/18.
 * DatabaseHelper class responsible for the creation of Movie database.
 */

@SuppressWarnings("WeakerAccess")
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "popular_movies.db";
    private static final int DATABASE_VERSION = 1;


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES__TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
