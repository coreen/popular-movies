package com.udacity.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;

public class MovieAdapter extends BaseAdapter {
    private final Context mContext;
    private final Movie[] movies;

    public MovieAdapter(Context context, Movie[] movies) {
        mContext = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.length;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = movies[position];
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.linearlayout_movie, null);
        }
        final ImageView mImageView = (ImageView) convertView.findViewById(R.id.iv_movie_thumbnail);
        Picasso.with(mContext)
                .load(movie.getImageUrl())
                .placeholder(R.drawable.movie_placeholder)
                .error(R.drawable.movie_placeholder_error)
                .into(mImageView);
        return convertView;
    }
}
