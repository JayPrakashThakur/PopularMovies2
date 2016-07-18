package com.appniche.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback{

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String MOVIEDETAILACTIVITYFRAGMENT_TAG = "MDAFTAG";
    private boolean mTwoPane;
    private String mSorting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSorting = Utility.getPreferredSortingOrder(this);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_fragment) != null){

            mTwoPane = true;
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_fragment, new MovieDetailActivityFragment(), MOVIEDETAILACTIVITYFRAGMENT_TAG)
                        .commit();
            }else {
                mTwoPane = false;
            }

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(long movieId) {
        if(mTwoPane){
            Log.d(LOG_TAG, "tablet layout found Movie id"+movieId);
            Bundle bundle = new Bundle();
            bundle.putLong(Intent.EXTRA_TEXT, movieId);

            MovieDetailActivityFragment detailActivityFragment = new MovieDetailActivityFragment();
            detailActivityFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_fragment, detailActivityFragment)
                    .commit();

        }else{
            Intent intent = new Intent(this, MovieDetailActivity.class);
                    //.putExtra(Intent.EXTRA_TEXT, movieId);
            Bundle bundle = new Bundle();
            bundle.putLong(Intent.EXTRA_TEXT, movieId);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String sortingPrefferedString = Utility.getPreferredSortingOrder(this);
        Log.d(LOG_TAG,"sorting string "+sortingPrefferedString);

        if (sortingPrefferedString != null & sortingPrefferedString.equals(mSorting)){

            MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (null != mainActivityFragment){
                mainActivityFragment.onSortingChanged();
            }

            MovieDetailActivityFragment detailActivityFragment = (MovieDetailActivityFragment) getSupportFragmentManager().findFragmentByTag(MOVIEDETAILACTIVITYFRAGMENT_TAG);
            if (null != detailActivityFragment){
                detailActivityFragment.onSortingChanged();
            }

        }
    }
}
