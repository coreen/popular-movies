package com.udacity.popularmovies.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.R;
import com.udacity.popularmovies.utilities.JsonUtils;
import com.udacity.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.net.URL;
import java.util.Arrays;

public class TrailerFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = TrailerFragment.class.getSimpleName();

    private static final String MOVIE_ID_EXTRA = "movieId";

    private static final int MOVIE_TRAILER_LOADER = 16;

    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private Context mContext;

    private TextView mNoTrailersMessage;
    private ProgressBar mLoadingIndicator;

    private String[] mTrailers;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trailer_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = view.findViewById(R.id.listview_trailer);
        mNoTrailersMessage = view.findViewById(R.id.tv_no_trailers);
        mLoadingIndicator = view.findViewById(R.id.pb_trailer_loading_indicator);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
        mAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchVideoPlayer(position, getActivity().getIntent());
            }
        });

        Bundle bundle = getArguments();
        Log.d(TAG, "received bundle in TrailerFragment: " + bundle);
        if (bundle != null) {
            loadTrailerData(bundle);
        }

        getLoaderManager().initLoader(MOVIE_TRAILER_LOADER, null, this);
    }

    public void launchVideoPlayer(int position, Intent intent) {
        String videoKey = mTrailers[position];
        // TODO(coreeny) add popup menu with options to launch in web browser or YouTube app
        // Web Browser launch
        Intent videoIntent = new Intent(
                Intent.ACTION_VIEW ,
                NetworkUtils.buildDetailVideoUri(videoKey));
        // Youtube launch
        intent.setComponent(new ComponentName(
                "com.google.android.youtube",
                "com.google.android.youtube.PlayerActivity"));

        if (videoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(videoIntent);
        }
    }

    private void showTrailerDataView() {
        mNoTrailersMessage.setVisibility(View.INVISIBLE);
        mListView.setVisibility(View.VISIBLE);
    }

    private void showNoTrailersMessage() {
        mListView.setVisibility(View.INVISIBLE);
        mNoTrailersMessage.setVisibility(View.VISIBLE);
    }

    private void loadTrailerData(Bundle bundle) {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            Loader<String> movieTrailerLoader = loaderManager.getLoader(MOVIE_TRAILER_LOADER);
            if (movieTrailerLoader == null) {
                loaderManager.initLoader(MOVIE_TRAILER_LOADER, bundle, this);
            } else {
                loaderManager.restartLoader(MOVIE_TRAILER_LOADER, bundle, this);
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        Log.d(TAG, "onCreateLoader called with id: " + id + " and args: " + args);
        return new AsyncTaskLoader<String>(mContext) {
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
                int movieId = args.getInt(MOVIE_ID_EXTRA);
                URL trailerRequestUrl = NetworkUtils.buildVideoUrl(movieId);
                try {
                    String jsonReviewResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);
                    return jsonReviewResponse;
                } catch (Exception e) {
                    // error
                    Log.e(TAG, "Failed to retrieve trailers for movieId: " + movieId);
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
                showTrailerDataView();
                Log.d(TAG, "TrailerFragment onLoadFinished data: " + data);
                String[] trailers = JsonUtils.parseVideosFromJsonString(data);
                Log.d(TAG, "trailers to display: " + Arrays.toString(trailers));

                mTrailers = trailers;
                mAdapter.addAll(trailers);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                showNoTrailersMessage();
            }
        } else {
            showNoTrailersMessage();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {}
}
