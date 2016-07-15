package com.appniche.popularmovies2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.appniche.popularmovies2";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_FAVOURITE = "favourite";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long releaseDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(releaseDate);
        int julianDay = Time.getJulianDay(releaseDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        //CONTENT_URI represents the base locations of Movie Table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        //table name to store movie
        public static final String  TABLE_NAME = "movie";
        //Columns
        //public static final String  COLUMN_ID = "id";//movie no 1,2 ...
        public static final String COLUMN_MOVIE_ID = "movie_id";//Foreign Key
        public static final String  COLUMN_TITLE = "title";
        public static final String  COLUMN_MOVIE_POSTER = "poster";
        public static final String  COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String  COLUMN_RELEASE_DATE = "release_date";
        public static final String  COLUMN_USER_RATING = "user_rating";
        //public static final String  COLUMN_VIDEO= "Video";

        // for building URIs on insertion
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri getMovieIdFromUri(String movieId) {
            //return Long.parseLong(uri.getPathSegments().get(1));
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

    }

    /* Inner class that defines the table contents of the trailer table */
    public static final class TrailerEntry implements BaseColumns{

        //CONTENT_URI represents the base locations of Trailer Table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        //table name to store trailers
        public static final String TABLE_NAME = "trailer";

        //Columns
        //public static final String COLUMN_TRAILER_ID = "trailer_id"; //trailer no 1,2 ,....
        public static final String COLUMN_MOVIE_ID = "movie_id";//Foreign Key
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        //public static final String COLUMN_TYPE = "type";

        // for building URIs on insertion
        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTrailerUriWithMovieId(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }
    }

    /* Inner class that defines the table contents of the review table */
    public static final class ReviewEntry implements BaseColumns{

        //CONTENT_URI represents the base locations of Review Table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        //table name to store reviews
        public static final String TABLE_NAME = "review";

        //Columns
        //public static final String COLUMN_REVIEW_ID = "review_id";//review no 1, 2 ,...
        public static final String COLUMN_MOVIE_ID = "movie_id";//Foreign Key
        public static final String COLUMN_CONTENT = "review_content";
        public static final String COLUMN_AUTHOR = "review_author";


        // for building URIs on insertion
        public static Uri buildReviewUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildReviewUriWithMovieId(String movieId) {
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

    }

    /* Inner class that defines the table contents of the movie table */
    public static final class FavouriteEntry implements BaseColumns {

        //CONTENT_URI represents the base locations of Favourite Table
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITE;

        //table name to store movie
        public static final String  TABLE_NAME = "favourite";
        //Columns
        public static final String COLUMN_MOVIE_ID = "movie_id";//Foreign Key
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_MOVIE_POSTER = "poster";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_USER_RATING = "user_rating";


        // for building URIs on insertion
        public static Uri buildFavouriteUri(){
            return CONTENT_URI.buildUpon().build();
        }

        public static Uri buildFavouriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri getMovieIdFromUri(String movieId) {
            //return Long.parseLong(uri.getPathSegments().get(1));
            return CONTENT_URI.buildUpon().appendPath(movieId).build();
        }

    }

}

