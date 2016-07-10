package com.appniche.popularmovies2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.appniche.popularmovies2.data.MovieContract.FavouriteEntry;
import com.appniche.popularmovies2.data.MovieContract.MovieEntry;
import com.appniche.popularmovies2.data.MovieContract.ReviewEntry;
import com.appniche.popularmovies2.data.MovieContract.TrailerEntry;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private final String LOG_TAG = MovieDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    //Database name
    static final String DATABASE_NAME = "popular_movie_Data.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //create a table to hold movies
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MovieEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_POSTER+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_OVERVIEW+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_RELEASE_DATE+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_USER_RATING+" REAL NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_ID+ " INTEGER UNIQUE ON CONFLICT REPLACE NOT NULL ON CONFLICT ABORT "
                +
                " );";
        Log.d(LOG_TAG, "create table statement"+SQL_CREATE_MOVIE_TABLE);

        //create a table to hold trailers based on movie id
        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TrailerEntry.COLUMN_MOVIE_ID+ " INTEGER UNIQUE ON CONFLICT REPLACE NOT NULL ON CONFLICT ABORT, " +
                TrailerEntry.COLUMN_KEY+" TEXT NOT NULL, "+
                TrailerEntry.COLUMN_NAME+" TEXT NOT NULL, "+
                TrailerEntry.COLUMN_SITE+" TEXT NOT NULL, "+
                TrailerEntry.COLUMN_SIZE+" TEXT NOT NULL "+
                " );";

        //create a table to hold reviews based on movie id
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ReviewEntry.COLUMN_MOVIE_ID +" INTEGER UNIQUE ON CONFLICT REPLACE NOT NULL ON CONFLICT ABORT, " +
                ReviewEntry.COLUMN_CONTENT+" TEXT NOT NULL, "+
                ReviewEntry.COLUMN_AUTHOR+" TEXT NOT NULL "+
                " );";

        //create a table to hold Favourite movies
        final String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE " + FavouriteEntry.TABLE_NAME + " (" +
                MovieEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                MovieEntry.COLUMN_MOVIE_ID+ " INTEGER UNIQUE ON CONFLICT REPLACE NOT NULL ON CONFLICT ABORT, " +
                MovieEntry.COLUMN_TITLE+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_POSTER+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_MOVIE_OVERVIEW+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_RELEASE_DATE+" TEXT NOT NULL, "+
                MovieEntry.COLUMN_USER_RATING+" REAL NOT NULL "+
                " );";
        Log.d(LOG_TAG, "create favourite table statement "+SQL_CREATE_FAVOURITE_TABLE);

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavouriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
