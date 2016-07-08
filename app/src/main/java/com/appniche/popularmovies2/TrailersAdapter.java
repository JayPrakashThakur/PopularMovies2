package com.appniche.popularmovies2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appniche.popularmovies2.data.MovieContract;

/**
 * Created by JayPrakash on 26-03-2016.
 */
public class TrailersAdapter extends CursorAdapter {

    private final String LOG_TAG = TrailersAdapter.class.getSimpleName();

    public TrailersAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private Trailer convertCursorRowToUXFormat(Cursor cursor) {
        Trailer trailer = new Trailer(
                cursor.getString(MovieDetailActivityFragment.COL_KEY),
                cursor.getString(MovieDetailActivityFragment.COL_NAME),
                cursor.getString(MovieDetailActivityFragment.COL_SITE),
                cursor.getString(MovieDetailActivityFragment.COL_SIZE)
        );
        return trailer;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View newView = LayoutInflater.from(context).inflate(R.layout.trailer_item, parent, false);

        return newView;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        Log.d(LOG_TAG,"in the bind view of trailer adapter");
        DatabaseUtils.dumpCursor(cursor);
        Trailer trailer = convertCursorRowToUXFormat(cursor);

        ImageView playTrailer = (ImageView) view.findViewById(R.id.play_trailer);
        final String trailerUrl = "https://www.youtube.com/watch?v=" +trailer.getKey();

        playTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                context.startActivity(intent);
            }
        });

        TextView trailerTitle = (TextView) view.findViewById(R.id.trailer_title);
        trailerTitle.setText(trailer.getName());
    }
}
