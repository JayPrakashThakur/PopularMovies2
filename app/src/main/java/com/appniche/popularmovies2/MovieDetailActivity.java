package com.appniche.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


/**
 * Created by JayPrakash on 25-03-2016.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    long movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        movieId = intent.getLongExtra(Intent.EXTRA_TEXT, 0);

        Bundle bundle = new Bundle();
        bundle.putLong(Intent.EXTRA_TEXT, movieId);

        MovieDetailActivityFragment detailActivityFragment = new MovieDetailActivityFragment();
        detailActivityFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_detail_fragment, detailActivityFragment)
                .commit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
