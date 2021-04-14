package com.example.runningtracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {



    // this is our content provider
    //it will be used by other apps to get data

    static String BASE_PATH_Exercise="exercise";

    static String BASE_PATH_OTHER="others";


    private SQLiteDatabase database;

    final static int EXERCISE_MULTIPLE=1;

    final static int EXERCISE_SINGLE=2;



    final static int Other_MULTIPLE=3;
    final static int Other_SINGLE=4;




    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        uriMatcher.addURI(RunningTrackerContract.AUTHORITY, BASE_PATH_Exercise, EXERCISE_MULTIPLE);
        uriMatcher.addURI(RunningTrackerContract.AUTHORITY, BASE_PATH_Exercise + "/#", EXERCISE_SINGLE);

        uriMatcher.addURI(RunningTrackerContract.AUTHORITY, BASE_PATH_OTHER, Other_MULTIPLE);
        uriMatcher.addURI(RunningTrackerContract.AUTHORITY, BASE_PATH_OTHER + "/#", Other_SINGLE);


    }
    @Override
    public boolean onCreate() {
        DBHelper helper = new DBHelper(getContext());
        database = helper.getReadableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {


        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        int uriType = uriMatcher.match(uri);

        switch (uriType)
        {
            case EXERCISE_SINGLE:
                queryBuilder.setTables("exercise");
                queryBuilder.appendWhere("_id=" + uri.getLastPathSegment());
                break;
            case EXERCISE_MULTIPLE:
                queryBuilder.setTables("exercise");
                break;

            case Other_SINGLE:
                queryBuilder.setTables("others");
                queryBuilder.appendWhere("_id=" + uri.getLastPathSegment());
                break;
            case Other_MULTIPLE:
                queryBuilder.setTables("others");
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(database,
                projection, selection, selectionArgs, null, null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch(uriMatcher.match(uri))
        {

            case EXERCISE_MULTIPLE:
            case Other_MULTIPLE:
                return RunningTrackerContract.CONTENT_TYPE_MULTIPLE;
            case EXERCISE_SINGLE:
            case Other_SINGLE:
                return RunningTrackerContract.CONTENT_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("URI not supported");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
