package com.appniche.popularmovies2;

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

/**
 * Created by JayPrakash on 26-03-2016.
 */
public class FetchReviewsTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    Context rContext;

    public FetchReviewsTask(Context rContext) {
        this.rContext = rContext;
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d(LOG_TAG,"Background taks started");
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // A String variable @movieJsoStr will contain the raw JSON response as a string.
        String reviewJsonStr = null;
        String movieId = params[0];
        try {

            final String BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String TRAILER_URL = movieId + "/reviews?";
            String API_KEY = "api_key=" + BuildConfig.MOVIE_DB_API_KEY;

            URL url = new URL(BASE_URL + TRAILER_URL + API_KEY);

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

            reviewJsonStr = buffer.toString();
            //Logging the Json movie data
            Log.v(LOG_TAG, "Review string: " + reviewJsonStr);
            getReviewDataFromJson(reviewJsonStr, movieId);

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

    private void getReviewDataFromJson(String reviewJsonStr, String movieId) throws JSONException{

        try {

            JSONObject reviewJson = new JSONObject(reviewJsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            ContentValues[] cVVector=new ContentValues[reviewArray.length()];

            for(int i = 0; i < reviewArray.length(); i++) {

//                Review review = new Review(
//                        reviewArray.getJSONObject(i).getString("content"),
//                        reviewArray.getJSONObject(i).getString("author"),
//                        reviewArray.getJSONObject(i).getString("id")
//                );

                JSONObject item = reviewArray.getJSONObject(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, item.getString("author"));
                contentValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, item.getString("content"));
                contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movieId);

                cVVector[i]=contentValues;
            }
            int insertCount =0;
            //int deleteCount=0;
            // add to database
            if ( cVVector.length > 0 ) {

                //deleteCount= rContext.getContentResolver().delete(MovieContract.ReviewEntry.buildReviewUri(Long.parseLong(movieId)),null,null);
                insertCount = rContext.getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, cVVector);
            }
            Log.d(LOG_TAG, "FetchReviewTask Complete." + insertCount + " Inserted");

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
