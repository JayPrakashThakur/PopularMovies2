package com.appniche.popularmovies2;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class MovieGridViewAdapter extends CursorAdapter {
    private final String LOG_TAG = MovieGridViewAdapter.class.getSimpleName();

    public MovieGridViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private Movie convertCursorRowToUXFormat(Cursor cursor) {
        //Log.d(LOG_TAG,"Convert cursor Row to UX Format called");
        // get row indices for our cursor
        Movie movie= new Movie(
                cursor.getString(MainActivityFragment.COL_TITLE),
                cursor.getString(MainActivityFragment.COL_MOVIE_POSTER),
                cursor.getString(MainActivityFragment.COL_MOVIE_OVERVIEW),
                cursor.getString(MainActivityFragment.COL_RELEASE_DATE),
                cursor.getString(MainActivityFragment.COL_USER_RATING),
                cursor.getString(MainActivityFragment.COL_MOVIE_ID)
        );
        return movie;
    }

    // these views are reused as needed.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d(LOG_TAG,"new view called");
        View newView = LayoutInflater.from(context).inflate(R.layout.grid_item_image, parent, false);
        Log.d(LOG_TAG,"new view inflated");
        return newView;
    }

    //This is where we fill-in the views with the contents of the cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d(LOG_TAG,"Bind view called");
        DatabaseUtils.dumpCursor(cursor);
        Movie movie = convertCursorRowToUXFormat(cursor);
        ImageView poster = (ImageView) view.findViewById(R.id.movie_image);
        poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (poster != null){
            //String uri = cursor.getString(MainActivityFragment.COL_MOVIE_POSTER);
            Uri uri = Uri.parse("http://image.tmdb.org/t/p/w185/" +movie.getmPosterUrl());
            Picasso.with(context)
                    .load(uri)
                    .into(poster);
            //poster.setAdjustViewBounds(true);
        }
    }
}
