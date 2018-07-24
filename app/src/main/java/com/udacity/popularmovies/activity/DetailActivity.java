package com.udacity.popularmovies.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.fragment.ReviewFragment;
import com.udacity.popularmovies.fragment.TrailerFragment;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.JsonUtils;
import com.udacity.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = DetailActivity.class.getSimpleName();

    private static final String MOVIE = "movie";
    private static final String MOVIE_ID_EXTRA = "movieId";

    private ImageView mBackdrop;
    private TextView mTitle;
    private ImageView mPoster;
    private TextView mReleaseDate;
    private TextView mVoteAvg;
    private TextView mMovieSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // find all views of activity
        mBackdrop = findViewById(R.id.iv_backdrop);
        mTitle = findViewById(R.id.tv_title);
        mPoster = findViewById(R.id.iv_poster);
        mReleaseDate = findViewById(R.id.tv_release_date);
        mVoteAvg = findViewById(R.id.tv_vote_avg);
        mMovieSummary = findViewById(R.id.tv_summary);

        Intent intent = getIntent();

        Movie selectedMovie = intent.getParcelableExtra(MOVIE);
        populateUI(selectedMovie);
        Picasso.with(this)
                .load(selectedMovie.getBackdropImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(mBackdrop);
        Picasso.with(this)
                .load(selectedMovie.getPosterImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(mPoster);

        // Grab associated trailer videos and reviews via movieId
        int movieId = selectedMovie.getId();
        Bundle bundle = new Bundle();
        bundle.putInt(MOVIE_ID_EXTRA, movieId);

        Log.d(TAG, "Passing bundle into fragments: " + bundle);

        // Pass in the movieId into Fragments for processing
        ReviewFragment reviewFragment = new ReviewFragment();
        reviewFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.review_fragment_placeholder, reviewFragment)
                .commit();

        TrailerFragment trailerFragment = new TrailerFragment();
        trailerFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.trailer_fragment_placeholder, trailerFragment)
                .commit();
    }

    private void populateUI(Movie selectedMovie) {
        mTitle.setText(selectedMovie.getTitle());
        mReleaseDate.setText("Release Date: " + selectedMovie.getReleaseDate());
        mVoteAvg.setText(selectedMovie.getVoteAvg() + " / 10");
        mMovieSummary.setText(selectedMovie.getSummary());
    }
}
