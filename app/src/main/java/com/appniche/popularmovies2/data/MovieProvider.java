package com.appniche.popularmovies2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class MovieProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper movieDbHelper;

    static final int MOVIE = 100;
    static final int MOVIE_BY_ID = 101;
    static final int TRAILER = 200;
    static final int TRAILER_BY_MOVIEID = 201;
    static final int REVIEW = 300;
    static final int REVIEW_BY_MOVIEID = 301;
    static final int FAVOURITE = 400;
    static final int FAVOURITE_BY_MOVIEID = 401;

    private static final SQLiteQueryBuilder movieQueryBuilder;
    private static final SQLiteQueryBuilder trailerQueryBuilder;
    private static final SQLiteQueryBuilder reviewQueryBuilder;
    private static final SQLiteQueryBuilder favouriteQueryBuilder;

    static {
        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        movieQueryBuilder = new SQLiteQueryBuilder();
        trailerQueryBuilder = new SQLiteQueryBuilder();
        reviewQueryBuilder = new SQLiteQueryBuilder();
        favouriteQueryBuilder = new SQLiteQueryBuilder();
    }

    private static UriMatcher buildUriMatcher() {
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE+"/#",MOVIE_BY_ID);
        uriMatcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        uriMatcher.addURI(authority, MovieContract.PATH_TRAILER+"/#", TRAILER_BY_MOVIEID);
        uriMatcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        uriMatcher.addURI(authority, MovieContract.PATH_REVIEW+"/#", REVIEW_BY_MOVIEID);
        uriMatcher.addURI(authority, MovieContract.PATH_FAVOURITE, FAVOURITE);
        uriMatcher.addURI(authority, MovieContract.PATH_FAVOURITE+"/#", FAVOURITE_BY_MOVIEID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor cursor;

        switch(sUriMatcher.match(uri)){
            case MOVIE:{
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIE_BY_ID:{
                //long movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);
                cursor =  movieDbHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        //new String[]{ Long.toString(movieId)},
                        null,
                        null,
                        sortOrder
                );

                break;
            }
            case TRAILER:{
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILER_BY_MOVIEID:{
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        MovieContract.TrailerEntry.COLUMN_MOVIE_ID  + "= ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEW:{
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case REVIEW_BY_MOVIEID:{
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        MovieContract.ReviewEntry.COLUMN_MOVIE_ID + "= ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVOURITE:{
                cursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case FAVOURITE_BY_MOVIEID:{
                //long movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);
                cursor =  movieDbHelper.getReadableDatabase().query(
                        MovieContract.FavouriteEntry.TABLE_NAME,
                        projection,
                        MovieContract.FavouriteEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        //new String[]{ Long.toString(movieId)},
                        null,
                        null,
                        sortOrder
                );

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_BY_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case TRAILER_BY_MOVIEID:
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case REVIEW_BY_MOVIEID:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case FAVOURITE:
                return MovieContract.FavouriteEntry.CONTENT_TYPE;
            case FAVOURITE_BY_MOVIEID:
                return MovieContract.FavouriteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){

            case MOVIE:{
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case TRAILER:{
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case REVIEW:{
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case FAVOURITE:{
                long _id = db.insert(MovieContract.FavouriteEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.FavouriteEntry.buildFavouriteUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection)
            selection = "1";

        switch (match){

            case MOVIE:{
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case TRAILER_BY_MOVIEID:{
                rowsDeleted = db.delete(
                        MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case REVIEW_BY_MOVIEID:{
                rowsDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
            }

            case FAVOURITE_BY_MOVIEID:{
                rowsDeleted = db.delete(
                        MovieContract.FavouriteEntry.TABLE_NAME, selection, selectionArgs);
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted!=0)
            getContext().getContentResolver().notifyChange(uri,null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE: {
                rowsUpdated = db.update(
                        MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }

            case TRAILER_BY_MOVIEID: {
                rowsUpdated = db.update(
                        MovieContract.TrailerEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }

            case REVIEW_BY_MOVIEID: {
                rowsUpdated = db.update(
                        MovieContract.ReviewEntry.TABLE_NAME, values, selection,
                        selectionArgs);
            }

            case FAVOURITE_BY_MOVIEID: {
                rowsUpdated = db.update(
                        MovieContract.FavouriteEntry.TABLE_NAME, values, selection,
                        selectionArgs);
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    private void normalizeDate(ContentValues values) {
        // normalize the date value
        if (values.containsKey(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)) {
            long dateValue = values.getAsLong(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, MovieContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        switch (match) {
            case MOVIE:{
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //  normalizeDate(value);

                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            case TRAILER:  {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //  normalizeDate(value);
                        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            case REVIEW:  {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //  normalizeDate(value);
                        long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            case FAVOURITE:  {
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //  normalizeDate(value);
                        long _id = db.insert(MovieContract.FavouriteEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }

            default:
                return super.bulkInsert(uri, values);
        }
    }
}

