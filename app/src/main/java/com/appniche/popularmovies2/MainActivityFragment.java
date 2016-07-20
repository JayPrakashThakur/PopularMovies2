package com.appniche.popularmovies2;

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
import android.widget.AdapterView;
import android.widget.GridView;

import com.appniche.popularmovies2.data.MovieContract;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    MovieGridViewAdapter movieGridViewAdapter;

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private  int mPosition;

    // CursorLoader Implementation Step 1, create Loader ID
    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {

            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_POSTER,
            MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_USER_RATING
    };

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    static final int COL_MOVIE_ID = 1;
    static final int COL_TITLE = 2;
    static final int COL_MOVIE_POSTER = 3;
    static final int COL_MOVIE_OVERVIEW = 4;
    static final int COL_RELEASE_DATE = 5;
    static final int COL_USER_RATING = 6;


    private static final String[] FAVOURITE_COLUMNS = {

            MovieContract.FavouriteEntry.TABLE_NAME + "." + MovieContract.FavouriteEntry._ID,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_ID,
            MovieContract.FavouriteEntry.COLUMN_TITLE,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_POSTER,
            MovieContract.FavouriteEntry.COLUMN_MOVIE_OVERVIEW,
            MovieContract.FavouriteEntry.COLUMN_RELEASE_DATE,
            MovieContract.FavouriteEntry.COLUMN_USER_RATING
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        movieGridViewAdapter = new MovieGridViewAdapter(getActivity(),null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);

        gridView.setAdapter(movieGridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if(cursor != null){
                    ((Callback)getActivity()).onItemSelected(cursor.getLong(COL_MOVIE_ID));
                    mPosition = position;
                }

            }
        });
        return rootView;
    }

    public void updateMovies() {

        FetchMovieTask fetchMovieTask = new FetchMovieTask(getActivity());
        fetchMovieTask.execute();

        movieGridViewAdapter.notifyDataSetChanged();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //@sortingPrefferdString stores the user prefferd way of sorting
        String sortingPrefferedString = Utility.getPreferredSortingOrder(getActivity());
        Log.d(LOG_TAG,"sorting string "+sortingPrefferedString);
        //build favourite movie uri

        if (sortingPrefferedString.equals("favourite.desc")){
            Uri favouriteMovieUri = MovieContract.FavouriteEntry.CONTENT_URI;
            return new CursorLoader(
                    getActivity(),
                    favouriteMovieUri,
                    FAVOURITE_COLUMNS,
                    null,
                    null,
                    null
            );
        } else {
            Uri movieURI = MovieContract.MovieEntry.CONTENT_URI;
            return new CursorLoader(
                    getActivity(),
                    movieURI,
                    MOVIE_COLUMNS,
                    null,//"sort_by=",
                    null,//new String[]{sortingPrefferedString},
                    null
            );
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        movieGridViewAdapter.swapCursor(data);
        movieGridViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieGridViewAdapter.swapCursor(null);
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         * @param movieId
         */
        void onItemSelected(long movieId);
    }

    public void onSortingChanged(){
        String sortingPrefferedString = Utility.getPreferredSortingOrder(getActivity());
        Log.d(LOG_TAG,"sorting string "+sortingPrefferedString);
        updateMovies();
        getLoaderManager().restartLoader(MOVIE_LOADER,null,this);
    }

}
