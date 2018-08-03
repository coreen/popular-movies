package com.udacity.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.R;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.DataUtils;

import static com.udacity.popularmovies.data.FavoriteContract.FavoriteEntry;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteAdapterViewHolder> {
    private static final String TAG = FavoriteAdapter.class.getSimpleName();

    private FavoriteAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private Cursor mCursor;

    public interface FavoriteAdapterOnClickHandler {
        void onClick(Movie selectedMovie);
    }

    public FavoriteAdapter(Context mContext, FavoriteAdapterOnClickHandler mClickHandler) {
        Log.d(TAG, "creating FavoriteAdapter");
        this.mContext = mContext;
        this.mClickHandler = mClickHandler;
    }

    public class FavoriteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;
        public final TextView mMovieTitle;
        public final ImageView mFavorite;

        public FavoriteAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = view.findViewById(R.id.iv_movie_thumbnail);
            mMovieTitle = view.findViewById(R.id.tv_movie_title);
            mFavorite = view.findViewById(R.id.iv_favorite);

            mFavorite.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        final int adapterPosition = getAdapterPosition();
                        Movie selectedMovie = getMovieFromCursor(adapterPosition);
                        DataUtils.toggleIsFavorite(mContext, selectedMovie);
                        Log.d(TAG, "Tagging movieId " + selectedMovie.getMovieId() +
                                " as favorite: " + selectedMovie.getIsFavorite(mContext));
                        notifyItemChanged(adapterPosition);
                    }
                    return true;
                }
            });

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int adapterPosition = getAdapterPosition();
            Movie selectedMovie = getMovieFromCursor(adapterPosition);
            mClickHandler.onClick(selectedMovie);
        }
    }

    @NonNull
    @Override
    public FavoriteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.movie_list_item, parent, false);
        return new FavoriteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapterViewHolder holder, int position) {
        Movie selectedMovie = getMovieFromCursor(position);
        Picasso.with(mContext)
                .load(selectedMovie.getPosterImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(holder.mMovieImageView);
        holder.mMovieTitle.setText(selectedMovie.getTitle());
        holder.mFavorite.setImageResource(selectedMovie.getIsFavorite(mContext) ?
                R.drawable.enabled_star :
                R.drawable.disabled_star);
    }

    private Movie getMovieFromCursor(int position) {
        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(mCursor.getColumnIndex(FavoriteEntry.COLUMN_MOVIE_ID));
        final String title = mCursor.getString(mCursor.getColumnIndex(FavoriteEntry.COLUMN_TITLE));
        final String backdropPath = mCursor.getString(mCursor.getColumnIndex(FavoriteEntry.COLUMN_BACKDROP_PATH));
        final String posterPath = mCursor.getString(mCursor.getColumnIndex(FavoriteEntry.COLUMN_POSTER_PATH));
        final String summary = mCursor.getString(mCursor.getColumnIndex(FavoriteEntry.COLUMN_SUMMARY));
        final String releaseDate = mCursor.getString(mCursor.getColumnIndex(FavoriteEntry.COLUMN_RELEASE_DATE));
        final String voteAvg = mCursor.getString(mCursor.getColumnIndex(FavoriteEntry.COLUMN_VOTE_AVG));

        return new Movie(id, title, backdropPath, posterPath, summary, releaseDate, voteAvg);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }
}
