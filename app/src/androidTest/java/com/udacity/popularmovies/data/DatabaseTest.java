package com.udacity.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static com.udacity.popularmovies.data.FavoriteContract.FavoriteEntry;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private final Context mContext = InstrumentationRegistry.getTargetContext();
    private final Class mDbHelperClass = FavoriteDbHelper.class;

    // Used for making sure each test run starts from clean slate
    private void deleteDatabase() {
        try {
            Field f = mDbHelperClass.getDeclaredField("DATABASE_NAME");
            f.setAccessible(true);
            mContext.deleteDatabase((String) f.get(null));
        } catch (NoSuchFieldException ex) {
            fail("Verify you have a member called DATABASE_NAME in FavoriteDbHelper");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    @Before
    public void setup() {
        deleteDatabase();
    }

    @Test
    public void create_database_test() throws Exception {
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) mDbHelperClass
                .getConstructor(Context.class)
                .newInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        assertEquals("Database is expected to be open and is not",
                true, database.isOpen());

        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        FavoriteEntry.TABLE_NAME + "'",
                null);

        assertTrue("Error creating database", tableNameCursor.moveToFirst());
        assertEquals("Error creating expected table",
                FavoriteEntry.TABLE_NAME, tableNameCursor.getString(0));

        tableNameCursor.close();
    }

    @Test
    public void insert_single_record_test() throws Exception {
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) mDbHelperClass
                .getConstructor(Context.class)
                .newInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(FavoriteEntry.COLUMN_MOVIE_ID, "123");
        testValues.put(FavoriteEntry.COLUMN_TITLE, "Mission Impossible");
        testValues.put(FavoriteEntry.COLUMN_BACKDROP_PATH, "/xBKGJQsAIeweesB79KC89FpBrVr.jpg");
        testValues.put(FavoriteEntry.COLUMN_POSTER_PATH, "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg");
        testValues.put(FavoriteEntry.COLUMN_SUMMARY, "Something about the movie.");
        testValues.put(FavoriteEntry.COLUMN_RELEASE_DATE, "2018-07-25");
        testValues.put(FavoriteEntry.COLUMN_VOTE_AVG, "5.6");
        testValues.put(FavoriteEntry.COLUMN_IS_FAVORITE, 1);
        final long firstRowId = database.insert(
                FavoriteEntry.TABLE_NAME,
                null,
                testValues);

        assertNotEquals("Unable to insert into the database", -1, firstRowId);

        Cursor wCursor = database.query(
                /* Name of table on which to perform the query */
                FavoriteEntry.TABLE_NAME,
                /* Columns; leaving this null returns every column in the table */
                null,
                /* Optional specification for columns in the "where" clause above */
                null,
                /* Values for "where" clause */
                null,
                /* Columns to group by */
                null,
                /* Columns to filter by row groups */
                null,
                /* Sort order to return in Cursor */
                null);

        /* Cursor.moveToFirst will return false if there are no records returned from your query */
        assertTrue("No records returned from query error", wCursor.moveToFirst());

        wCursor.close();
        dbHelper.close();
    }

    @Test
    public void autoincrement_test() throws Exception {
        insert_single_record_test();
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) mDbHelperClass
                .getConstructor(Context.class)
                .newInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(FavoriteEntry.COLUMN_MOVIE_ID, "123");
        testValues.put(FavoriteEntry.COLUMN_TITLE, "Mission Impossible");
        testValues.put(FavoriteEntry.COLUMN_BACKDROP_PATH, "/xBKGJQsAIeweesB79KC89FpBrVr.jpg");
        testValues.put(FavoriteEntry.COLUMN_POSTER_PATH, "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg");
        testValues.put(FavoriteEntry.COLUMN_SUMMARY, "Something about the movie.");
        testValues.put(FavoriteEntry.COLUMN_RELEASE_DATE, "2018-07-25");
        testValues.put(FavoriteEntry.COLUMN_VOTE_AVG, "5.6");
        testValues.put(FavoriteEntry.COLUMN_IS_FAVORITE, 1);
        final long firstRowId = database.insert(
                FavoriteEntry.TABLE_NAME,
                null,
                testValues);
        final long secondRowId = database.insert(
                FavoriteEntry.TABLE_NAME,
                null,
                testValues);

        assertEquals("ID autoincrement failed", firstRowId + 1, secondRowId);
    }

    @Test
    public void upgrade_database_test() throws Exception {
        SQLiteOpenHelper dbHelper = (SQLiteOpenHelper) mDbHelperClass
                .getConstructor(Context.class)
                .newInstance(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues testValues = new ContentValues();
        testValues.put(FavoriteEntry.COLUMN_MOVIE_ID, "123");
        testValues.put(FavoriteEntry.COLUMN_TITLE, "Mission Impossible");
        testValues.put(FavoriteEntry.COLUMN_BACKDROP_PATH, "/xBKGJQsAIeweesB79KC89FpBrVr.jpg");
        testValues.put(FavoriteEntry.COLUMN_POSTER_PATH, "/9O7gLzmreU0nGkIB6K3BsJbzvNv.jpg");
        testValues.put(FavoriteEntry.COLUMN_SUMMARY, "Something about the movie.");
        testValues.put(FavoriteEntry.COLUMN_RELEASE_DATE, "2018-07-25");
        testValues.put(FavoriteEntry.COLUMN_VOTE_AVG, "5.6");
        testValues.put(FavoriteEntry.COLUMN_IS_FAVORITE, 1);
        final long firstRowId = database.insert(
                FavoriteEntry.TABLE_NAME,
                null,
                testValues);
        final long secondRowId = database.insert(
                FavoriteEntry.TABLE_NAME,
                null,
                testValues);

        dbHelper.onUpgrade(database, 0, 1);
        database = dbHelper.getReadableDatabase();
        Cursor tableNameCursor = database.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" +
                        FavoriteEntry.TABLE_NAME + "'",
                null);

        assertTrue(tableNameCursor.getCount() == 1);

        Cursor wCursor = database.query(
                FavoriteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertFalse("Database did not drop successfully when upgrading",
                wCursor.moveToFirst());

        tableNameCursor.close();
        database.close();
    }
}
