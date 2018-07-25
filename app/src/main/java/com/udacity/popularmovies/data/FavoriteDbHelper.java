package com.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.popularmovies.data.FavoriteContract.FavoriteEntry;

public class FavoriteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favoritesDb.db";
    // increment if changing database schema
    private static final int VERSION = 3;

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID                  + " INTEGER PRIMARY KEY, " +
                FavoriteEntry.COLUMN_MOVIE_ID      + " INTEGER NOT NULL, " +
                FavoriteEntry.COLUMN_TITLE         + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_POSTER_PATH   + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_SUMMARY       + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_RELEASE_DATE  + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_VOTE_AVG      + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_IS_FAVORITE   + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
