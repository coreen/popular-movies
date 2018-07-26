package com.udacity.popularmovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.AutofitRecyclerView;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.activity.DetailActivity;
import com.udacity.popularmovies.adapter.FavoriteAdapter;
import com.udacity.popularmovies.model.Movie;

import static com.udacity.popularmovies.data.FavoriteContract.FavoriteEntry;

public class FavoriteFragment
        extends Fragment
        implements FavoriteAdapter.FavoriteAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = FavoriteFragment.class.getSimpleName();

    public static final String MOVIE = "movie";

    private static final int MOVIE_FAVORITE_LOADER = 26;

    private Context mContext;

    private AutofitRecyclerView mRecyclerView;
    private FavoriteAdapter mAdapter;

    private TextView mNoFavoritesMessage;
    private ProgressBar mLoadingIndicator;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favorite_fragment, container,  false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recyclerview_favorite);
        mNoFavoritesMessage = view.findViewById(R.id.tv_no_favorites);
        mLoadingIndicator = view.findViewById(R.id.pb_favorite_loading_indicator);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        mAdapter = new FavoriteAdapter(mContext, this);
        mRecyclerView.setAdapter(mAdapter);

        LoaderManager loaderManager = getLoaderManager();
        Loader<String> movieReviewLoader = loaderManager.getLoader(MOVIE_FAVORITE_LOADER);
        if (movieReviewLoader == null) {
            loaderManager.initLoader(MOVIE_FAVORITE_LOADER, null, this);
        } else {
            loaderManager.restartLoader(MOVIE_FAVORITE_LOADER, null, this);
        }
    }

    @Override
    public void onClick(Movie selectedMovie) {
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(mContext, destinationClass);
        intentToStartDetailActivity.putExtra(MOVIE, selectedMovie);
        startActivity(intentToStartDetailActivity);
    }

    private void showFavoritesDataView() {
        mNoFavoritesMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showNoFavoritesMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mNoFavoritesMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> temp = new CursorLoader(mContext,
                FavoriteEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        Log.d(TAG, "onCreateLoader cursorLoader: " + temp);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        return temp;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mAdapter.swapCursor(data);
        if (data.getCount() == 0) {
            showNoFavoritesMessage();
        } else {
            showFavoritesDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
