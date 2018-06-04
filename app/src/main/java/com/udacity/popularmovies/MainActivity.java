package com.udacity.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.utilities.MovieDbJsonUtils;

public class MainActivity extends AppCompatActivity {
    private GridView mGridView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find and setup views
        mGridView = (GridView) findViewById(R.id.gridview_movie);
        MovieAdapter movieAdapter = new MovieAdapter(MainActivity.this, MovieDbJsonUtils.parseApiJson());
        mGridView.setAdapter(movieAdapter);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }
}
