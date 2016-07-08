package com.appniche.popularmovies2;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by JayPrakash on 26-03-2016.
 */
public class ReviewsAdapter extends CursorAdapter {

    private final String LOG_TAG = ReviewsAdapter.class.getSimpleName();

    public ReviewsAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private Review convertCursorRowToUXFormat(Cursor cursor) {
        Review review = new Review(
                cursor.getString(MovieDetailActivityFragment.COL_CONTENT),
                cursor.getString(MovieDetailActivityFragment.COL_AUTHOR)
        );
        return review;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View newView = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);

        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d(LOG_TAG,"in the bind view of REVIEWS adapter");
        DatabaseUtils.dumpCursor(cursor);
        Review review = convertCursorRowToUXFormat(cursor);

        TextView content = (TextView) view.findViewById(R.id.content_textView);
        content.setText(review.getContent());

        TextView author = (TextView) view.findViewById(R.id.author_text_view);
        author.setText(review.getAuthor());
    }
}
