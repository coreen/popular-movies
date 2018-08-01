package com.udacity.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.AutofitRecyclerView;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.adapter.MovieAdapter;
import com.udacity.popularmovies.fragment.FavoriteFragment;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.SortBy;
import com.udacity.popularmovies.utilities.JsonUtils;
import com.udacity.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;

public class MainActivity
        extends AppCompatActivity
        implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String> {

    public static final String MOVIE = "movie";
    public static final String SORT_BY_EXTRA = "sortBy";
    public static final String SCROLL_POSITION_EXTRA = "scrollPosition";

    private static final int MOVIE_SORT_LOADER = 22;

    // Default to most popular sort
    private SortBy mSort = SortBy.MOST_POPULAR;

    private AutofitRecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private Parcelable mListState;

    private TextView mErrorMessage;
    private ProgressBar mLoadingIndicator;

    private FavoriteFragment mFavoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find and setup views
        setupRecyclerView();
        mErrorMessage = findViewById(R.id.tv_error_message);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState != null) {
            loadMovieData((SortBy) savedInstanceState.getSerializable(SORT_BY_EXTRA));
        } else {
            loadMovieData(mSort);
        }

        // Initialize loader
        getSupportLoaderManager().initLoader(MOVIE_SORT_LOADER, null, this);
    }

    private void setupRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview_movie);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(MOVIE, selectedMovie);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovieDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        if (mFavoriteFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .hide(mFavoriteFragment)
                    .commit();
        }
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showFavoritesDataView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Resource: https://stackoverflow.com/questions/14347588/show-hide-fragment-in-android
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mFavoriteFragment == null) {
            mFavoriteFragment = new FavoriteFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.favorites_fragment_placeholder, mFavoriteFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .show(mFavoriteFragment)
                    .commit();
        }
    }

    private void showErrorMessage(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        if (mFavoriteFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .hide(mFavoriteFragment)
                    .commit();
        }
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loadMovieData(SortBy sort) {
        if (sort == SortBy.FAVORITES) {
            showFavoritesDataView();
            return;
        }

        showMovieDataView();

        // Check if have internet connection before kicking off AsyncTask
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            Bundle sortBundle = new Bundle();
            sortBundle.putSerializable(SORT_BY_EXTRA, sort);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> movieSortLoader = loaderManager.getLoader(MOVIE_SORT_LOADER);
            if (movieSortLoader == null) {
                loaderManager.initLoader(MOVIE_SORT_LOADER, sortBundle, this);
            } else {
                loaderManager.restartLoader(MOVIE_SORT_LOADER, sortBundle, this);
            }
        } else {
            showErrorMessage();
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                SortBy sort = (SortBy) args.get(SORT_BY_EXTRA);
                if (sort == null) {
                    return null;
                }
                URL movieRequestUrl = NetworkUtils.buildUrl(sort);
                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    return jsonMovieResponse;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            try {
                showMovieDataView();
                Movie[] movieData = JsonUtils.getMoviesFromJsonString(data);
                mMovieAdapter.setMovieData(movieData);
            } catch (JSONException e) {
                e.printStackTrace();
                showErrorMessage();
            }
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}

    // Display menu in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_by, menu);
        return true;
    }

    // Resource: https://stackoverflow.com/questions/6150080/set-a-menu-item-as-checked-from-code
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (mSort) {
            case MOST_POPULAR:
                menu.findItem(R.id.action_most_popular_sort).setChecked(true);
                break;
            case TOP_RATED:
                menu.findItem(R.id.action_top_rated_sort).setChecked(true);
                break;
            case FAVORITES:
                menu.findItem(R.id.action_favorites_sort).setChecked(true);
                break;
            default:
                menu.findItem(R.id.action_most_popular_sort).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular_sort:
                loadMovieData(SortBy.MOST_POPULAR);
                item.setChecked(!item.isChecked());
                mSort = SortBy.MOST_POPULAR;
                return true;
            case R.id.action_top_rated_sort:
                loadMovieData(SortBy.TOP_RATED);
                item.setChecked(!item.isChecked());
                mSort = SortBy.TOP_RATED;
                return true;
            case R.id.action_favorites_sort:
                loadMovieData(SortBy.FAVORITES);
                item.setChecked(!item.isChecked());
                mSort = SortBy.FAVORITES;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the sort based on what was selected in the menu
        outState.putSerializable(SORT_BY_EXTRA, mSort);
        // Save the scroll position in the AutofitRecyclerView
        // Resource: https://stackoverflow.com/questions/28236390/recyclerview-store-restore-state-between-activities
        Parcelable mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SCROLL_POSITION_EXTRA, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(SCROLL_POSITION_EXTRA);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListState != null) {
            mRecyclerView
                    .getLayoutManager()
                    .onRestoreInstanceState(mListState);
        }
    }
}
