package com.appniche.popularmovies2;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
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

/**
 * Created by JayPrakash on 26-03-2016.
 */
public class FetchTrailersTask extends AsyncTask<String, Void, Void>{

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    Context tContext;

    public FetchTrailersTask(Context tContext) {
        this.tContext = tContext;
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d(LOG_TAG,"Background taks started");
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // A String variable @movieJsoStr will contain the raw JSON response as a string.
        String trailerJsonStr = null;
        String movieId = params[0];
        try {
            final String BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String TRAILER_URL = movieId + "/videos?";
            String API_KEY = "api_key=" + BuildConfig.MOVIE_DB_API_KEY;

            URL url = new URL(BASE_URL + TRAILER_URL + API_KEY);

//            Uri builtUri = Uri.parse(BASE_URL + mMovieId + "/videos").buildUpon()
//                    .appendQueryParameter(API_KEY, BuildConfig.OPEN_MOVIE_API_KEY).build();


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

            trailerJsonStr = buffer.toString();
            //Logging the Json movie data
            Log.v(LOG_TAG, "Trailer string: " + trailerJsonStr);
            getTrailerDataFromJson(trailerJsonStr, movieId);

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

    private void getTrailerDataFromJson(String trailerJsonStr, String movieId) throws JSONException{

        try {

            JSONObject trailerJson = new JSONObject(trailerJsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            ContentValues[] cVVector=new ContentValues[trailerArray.length()];

            for(int i = 0; i < trailerArray.length(); i++) {

                JSONObject item = trailerArray.getJSONObject(i);

                ContentValues contentValues = new ContentValues();

                contentValues.put(MovieContract.TrailerEntry.COLUMN_KEY, item.getString("key"));
                contentValues.put(MovieContract.TrailerEntry.COLUMN_NAME, item.getString("name"));
                contentValues.put(MovieContract.TrailerEntry.COLUMN_SITE, item.getString("site"));
                contentValues.put(MovieContract.TrailerEntry.COLUMN_SIZE, item.getString("size"));
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movieId);

                cVVector[i]=contentValues;
            }
            int insertCount =0;
            //int deleteCount=0;
            // add to database
            if ( cVVector.length > 0 ) {
                //deleteCount= tContext.getContentResolver().delete(MovieContract.TrailerEntry.buildTrailerUri(Long.parseLong(movieId)),null,null);
                insertCount = tContext.getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, cVVector);
            }
            Log.d(LOG_TAG, "FetchTrailerTask Complete." + insertCount + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }
}
