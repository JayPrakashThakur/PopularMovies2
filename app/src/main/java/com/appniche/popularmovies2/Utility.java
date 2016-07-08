package com.appniche.popularmovies2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by JayPrakash on 25-03-2016.
 */
public class Utility {

    private String LOG_TAG = Utility.class.getSimpleName();

    public static String getPreferredSortingOrder(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(context.getString(R.string.pref_sorting_key),context.getString(R.string.pref_sorting_default));
    }

}

