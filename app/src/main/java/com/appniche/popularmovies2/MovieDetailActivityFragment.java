package com.appniche.popularmovies2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.appniche.popularmovies2.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class MovieDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();

    private Movie movie;
    long movieId;
    View rootView;
    TrailersAdapter trailersAdapter;
    ReviewsAdapter reviewsAdapter;
    boolean isFavourite = false;

    private static final int MOVIE_DETAIL_LOADER = 0;
    private static final int TRAILER_LOADER = 1;
    private static final int REVIEW_LOADER = 2;
    private static final int FAVOURITE_LOADER = 3;

    private static final String[] MOVIE_COLUMNS = {

            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_USER_RATING,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these must change.
    static final int COL_TITLE = 1;
    static final int COL_MOVIE_POSTER = 2;
    static final int COL_MOVIE_OVERVIEW = 3;
    static final int COL_RELEASE_DATE = 4;
    static final int COL_USER_RATING = 5;
    static final int COL_MOVIE_ID = 6;

    private static final String[] TRAILER_COLUMNS = {
         MovieContract.TrailerEntry.TABLE_NAME + "." + MovieContract.TrailerEntry._ID,
         MovieContract.TrailerEntry.COLUMN_KEY,
         MovieContract.TrailerEntry.COLUMN_NAME,
         MovieContract.TrailerEntry.COLUMN_SITE,
         MovieContract.TrailerEntry.COLUMN_SIZE
    };

    static final int COL_KEY = 1;
    static final int COL_NAME = 2;
    static final int COL_SITE = 3;
    static final int COL_SIZE = 4;

    private static final String[] REVIEW_COLUMNS = {
        MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID,
        MovieContract.ReviewEntry.COLUMN_CONTENT,
        MovieContract.ReviewEntry.COLUMN_AUTHOR
    };

    static final int COL_CONTENT = 1;
    static final int COL_AUTHOR = 2;

    private static final String[] FAVOURITE_COLUMNS = {

            MovieContract.FavouriteEntry.TABLE_NAME + "." + MovieContract.FavouriteEntry._ID,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_ID,
            MovieContract.FavouriteEntry.COLUMN_TITLE,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.FavouriteEntry.COLUMN_RELEASE_DATE,
            MovieContract.FavouriteEntry.COLUMN_USER_RATING
    };

    // These indices are tied to FAVOURITE_COLUMNS.  If FAVOURITE_COLUMNS changes, these must change.
    static final int COL_FAV_TITLE = 2;
    static final int COL_FAV_MOVIE_POSTER = 2;
    static final int COL_FAV_MOVIE_OVERVIEW = 4;
    static final int COL_FAV_RELEASE_DATE = 5;
    static final int COL_FAV_USER_RATING = 6;
/*public MovieDetailActivityFragment() {
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView Called");
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            movieId = intent.getLongExtra(Intent.EXTRA_TEXT, 0);
            Log.d(LOG_TAG, "Movie id " + movieId);
        }
        Bundle arguments = getArguments();
        if (arguments != null){
            movieId = arguments.getLong(Intent.EXTRA_TEXT);
            Log.d(LOG_TAG,"Movie id "+movieId);
        }

        if (movieId != 0){

            FetchTrailersTask getTrailersTask = new FetchTrailersTask(getContext());
            getTrailersTask.execute(String.valueOf(movieId));

            FetchReviewsTask getReviewsTask = new FetchReviewsTask(getContext());
            getReviewsTask.execute(String.valueOf(movieId));

        }

        trailersAdapter = new TrailersAdapter(getActivity(), null, 0);
        ListView trailersListView = (ListView) rootView.findViewById(R.id.trailer_list_view);
        trailersListView.setAdapter(trailersAdapter);

        reviewsAdapter = new ReviewsAdapter(getActivity(), null, 0);
        ListView reviewsListView = (ListView) rootView.findViewById(R.id.review_list_view);
        reviewsListView.setAdapter(reviewsAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER,null,this);
        getLoaderManager().initLoader(TRAILER_LOADER,null,this);
        getLoaderManager().initLoader(REVIEW_LOADER,null,this);
        getLoaderManager().initLoader(FAVOURITE_LOADER, null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG,"In onCreateLoader");
        Log.v(LOG_TAG,"In onCreateLoader Movie Id "+movieId);
        /*Uri uri = MovieContract.MovieEntry.buildMovieUri(movieId);
        Log.v(LOG_TAG,"Uri :"+uri);
        return new CursorLoader(
                getActivity(),
                uri,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );*/

        switch (id){
            case MOVIE_DETAIL_LOADER:{
                Log.v(LOG_TAG,"In Movie Detail Loader");
                Uri uri = MovieContract.MovieEntry.buildMovieUri(movieId);
                return new CursorLoader(
                        getActivity(),
                        uri,
                        MOVIE_COLUMNS,
                        null,
                        null,
                        null
                );
            }
            case TRAILER_LOADER:{
                Log.v(LOG_TAG,"In Trailer Loader");
                Uri uri = MovieContract.TrailerEntry.buildTrailerUri(movieId);
                return new CursorLoader(
                        getActivity(),
                        uri,
                        TRAILER_COLUMNS,
                        null,
                        null,
                        null
                );
            }
            case REVIEW_LOADER:{
                Log.v(LOG_TAG,"In Review Loader");
                Uri uri = MovieContract.ReviewEntry.buildReviewUri(movieId);
                return new CursorLoader(
                        getActivity(),
                        uri,
                        REVIEW_COLUMNS,
                        null,
                        null,
                        null
                );
            }
            case FAVOURITE_LOADER:{
                Log.v(LOG_TAG,"In Review Loader");
                Uri uri = MovieContract.FavouriteEntry.buildFavouriteUri();
                return new CursorLoader(
                        getActivity(),
                        uri,
                        FAVOURITE_COLUMNS,
                        null,
                        null,
                        null
                );
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG,"In onLoadFinished");
        if (!data.moveToFirst()) {
            return;
        }

        switch (loader.getId()){

            case MOVIE_DETAIL_LOADER:{
                Log.v(LOG_TAG,"In onLoadFinished Movie Detail Loader");
                TextView originalTitle = (TextView)rootView.findViewById(R.id.original_title);
                originalTitle.setText(data.getString(COL_TITLE));

                ImageView imageView = (ImageView)rootView.findViewById(R.id.movie_thumbnail);
                String url = data.getString(COL_MOVIE_POSTER);
                Picasso
                        .with(getActivity())
                        .load(url)
                        .into(imageView);

                TextView overview = (TextView)rootView.findViewById(R.id.overview_textView);
                overview.setText(data.getString(COL_MOVIE_OVERVIEW));

                TextView releaseDate = (TextView)rootView.findViewById(R.id.releaseDate_textView);
                releaseDate.setText(data.getString(COL_RELEASE_DATE));

                TextView userRating = (TextView)rootView.findViewById(R.id.userRating_textView);
                userRating.setText(data.getString(COL_USER_RATING)+ "/10");

                final String movieId = data.getString(COL_MOVIE_ID);
                final String title = data.getString(COL_TITLE);
                final String poster = data.getString(COL_MOVIE_POSTER);
                final String overvie = data.getString(COL_MOVIE_OVERVIEW);
                final String release = data.getString(COL_RELEASE_DATE);
                final String rating = data.getString(COL_USER_RATING);
                Log.d(LOG_TAG,"at 236 values "+movieId+" "+title+" "+poster+" "+overvie+" "+release+" "+rating);
                final Button favButton = (Button) rootView.findViewById(R.id.favButton);
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //inserting values into favourite table
                        if (isFavourite == false){

                            ContentValues favMovieValues = new ContentValues();
                            Log.e(LOG_TAG,"at 245 values "+movieId+" "+title+" "+poster+" "+overvie+" "+release+" "+rating);
                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_ID, movieId);
                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_TITLE, title);
                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER, poster);
                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW, overvie);
                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_RELEASE_DATE, release);
                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_USER_RATING, rating);

                            getContext().getContentResolver().insert(MovieContract.FavouriteEntry.CONTENT_URI, favMovieValues);
                            Log.d(LOG_TAG,"favourite movie inserted");
                            favButton.setBackgroundColor(getResources().getColor(R.color.favButtonColor));
                            favButton.setText("Favourite");
                            isFavourite = true;
                        }
                    }
                });

            }

            case TRAILER_LOADER:{
                Log.v(LOG_TAG,"In onLoadFinished Movie Trailer Loader");
                if (data != null){
                    trailersAdapter.swapCursor(data);
                }

            }

            case REVIEW_LOADER:{
                Log.v(LOG_TAG,"In onLoadFinished Movie Review Loader");
                if (data != null){
                    reviewsAdapter.swapCursor(data);
                }

            }

            case FAVOURITE_LOADER:{
//                Button favButton = (Button) rootView.findViewById(R.id.favButton);
//                if (data != null){
//                    favButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //inserting values into favourite table
//                            if (isFavourite == false){
//
//                                ContentValues favMovieValues = new ContentValues();
//                                Log.d(LOG_TAG,"movie id value : "+movie.getmTitle());
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_ID, movie.getmId());
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_TITLE, movie.getmTitle());
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER, movie.getmPosterUrl());
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW, movie.getmOverview());
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_RELEASE_DATE, movie.getmReleaseDate());
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_USER_RATING, movie.getmRating());

                                //String overview = MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW;

//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_ID, COL_MOVIE_ID);
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_TITLE, COL_FAV_TITLE);
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER, COL_FAV_MOVIE_POSTER);
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW, COL_FAV_MOVIE_OVERVIEW);
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_RELEASE_DATE, COL_FAV_RELEASE_DATE);
//                            favMovieValues.put(MovieContract.FavouriteEntry.COLUMN_USER_RATING, COL_FAV_USER_RATING);

//                                getContext().getContentResolver().insert(MovieContract.FavouriteEntry.CONTENT_URI, favMovieValues);
//                                Log.d(LOG_TAG,"favourite movie inserted");
//                                isFavourite = true;
//                            }
//                        }
//                    });
//
//                }

            }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public void onSortingChanged(){
        getLoaderManager().initLoader(MOVIE_DETAIL_LOADER,null,this);
        getLoaderManager().initLoader(TRAILER_LOADER,null,this);
        getLoaderManager().initLoader(REVIEW_LOADER,null,this);
        getLoaderManager().initLoader(FAVOURITE_LOADER, null,this);
    }

}