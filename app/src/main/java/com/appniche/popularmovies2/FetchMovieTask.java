package com.appniche.popularmovies2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.appniche.popularmovies2.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    MovieGridViewAdapter movieGridViewAdapter;
    Context mContext;
    Movie movie;

    public FetchMovieTask(Context context){
        mContext = context;
    }

    //parsing movie JSON Data
    private void getMoviesDataFromJson(String moviesJsonStr)
            throws JSONException {

        try {
            JSONObject movieJson = new JSONObject(moviesJsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

            //Movie movie[] = new Movie[movieArray.length()];
            final String BASE_URL = "http://image.tmdb.org/t/p/w185/" ;

            //my movie const...Movie(String mTitle, String mPosterUrl, String mOverview, String mReleaseDate, String mRating)

            for(int i=0;i<movieArray.length();i++){
                movie = new Movie(
                        movieArray.getJSONObject(i).getString("id"),
                        movieArray.getJSONObject(i).getString("original_title"),
                        BASE_URL + movieArray.getJSONObject(i).getString("poster_path"),
                        movieArray.getJSONObject(i).getString("overview"),
                        movieArray.getJSONObject(i).getString("release_date"),
                        movieArray.getJSONObject(i).getString("vote_average")
                );

                ContentValues movieValues = new ContentValues();

                //Adding data
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getmId());
                movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getmTitle());
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, movie.getmPosterUrl());
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getmOverview());
                movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getmReleaseDate());
                movieValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, movie.getmRating());

                cVVector.add(movieValues);
            }
            int inserted = 0;
            int deleted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                deleted = mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "FetchMovieTask Complete. "+deleted+" deleted &" + inserted + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d(LOG_TAG,"Background taks started");
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // A String variable @movieJsoStr will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            // Construct the URL for the Movie Database query
            String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

            String sortingPrefferedString = Utility.getPreferredSortingOrder(mContext);
            Log.d(LOG_TAG,"sorting string "+sortingPrefferedString);
            String SORTING_URL = "";
            String POPULARITY_URL = "sort_by=popularity.desc";
            String VOTE_URL = "sort_by=vote_average.desc&vote_count.gte=20";
            if (sortingPrefferedString.equals("vote_average.desc"))
                SORTING_URL = VOTE_URL;
            else
                SORTING_URL = POPULARITY_URL;

            String API_KEY = "&api_key=" + BuildConfig.MOVIE_DB_API_KEY;
            //URL url = new URL(BASE_URL + POPULARITY_URL + API_KEY);
           // URL url = new URL(BASE_URL + API_KEY);
            URL url = new URL(BASE_URL + SORTING_URL + API_KEY);

            //URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=your_api_key");

            Log.d(LOG_TAG,"URL" +url.toString());
            // Create the request to The Movie Db, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }

            movieJsonStr = buffer.toString();
            //Logging the Json movie data
            Log.v(LOG_TAG, "Movie data string: " + movieJsonStr);
            getMoviesDataFromJson(movieJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error! No data from the API. Is the API key for TMDb missing? ", e);
            // If the code didn't successfully get the  data, there's no point in attempting
            // to parse it.
            return null;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            // End connection and close the reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

}
