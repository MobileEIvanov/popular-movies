package com.popularmovies.data.database;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.popularmovies.data.database.ContractMoviesData.*;

/**
 * Created by emil.ivanov on 3/14/18.
 */

public class TestUtil {
    public static void insertFakeData(SQLiteDatabase db) {
        if (db == null) {
            return;
        }
//        //create a list of fake guests
//        List<ContentValues> list = new ArrayList<ContentValues>();
//
//        ContentValues cv = new ContentValues();
//        cv.put(MovieEntry.COLUMN_MOVIE_NAME, "John");
//        cv.put(MovieEntry.COLUMN_IS_FAVORITE, true);
//        list.add(cv);
//
//        cv = new ContentValues();
//        cv.put(MovieEntry.COLUMN_MOVIE_NAME, "John");
//        cv.put(MovieEntry.COLUMN_IS_FAVORITE, false);
//        list.add(cv);
//
//        cv = new ContentValues();
//        cv.put(MovieEntry.COLUMN_MOVIE_NAME, "Mike");
//        cv.put(MovieEntry.COLUMN_IS_FAVORITE, true);
//        list.add(cv);
//
//        cv = new ContentValues();
//        cv.put(MovieEntry.COLUMN_MOVIE_NAME, "Kate");
//        cv.put(MovieEntry.COLUMN_IS_FAVORITE, false);
//        list.add(cv);
//
//        cv = new ContentValues();
//        cv.put(MovieEntry.COLUMN_MOVIE_NAME, "Laty");
//        cv.put(MovieEntry.COLUMN_IS_FAVORITE, true);
//        list.add(cv);
//
//        //insert all guests in one transaction
//        try
//        {
//            db.beginTransaction();
//            //clear the table first
//            db.delete (MovieEntry.TABLE_NAME,null,null);
//            //go through the list and add one by one
//            for(ContentValues c:list){
//                db.insert(MovieEntry.TABLE_NAME, null, c);
//            }
//            db.setTransactionSuccessful();
//        }
//        catch (SQLException e) {
//            //too bad :(
//        }
//        finally
//        {
//            db.endTransaction();
//        }
//
//    }
    }
}
