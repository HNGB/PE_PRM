package com.example.se150260_pe02;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ClockContentProvider extends ContentProvider {
    private Database db;
    public static String AUTHOR = "com.example.se150260_pe02.ClockContentProvider";
    public static final String CLOCK_TB = "Clock";
    public static String URI_CARS = "content://" + AUTHOR + "/" + CLOCK_TB;
    public static UriMatcher uriMatcher;
    Cursor cursor;

    @Override
    public boolean onCreate() {
        db = new Database(getContext());
        db.getWritableDatabase();
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHOR, CLOCK_TB, 1);
        uriMatcher.addURI(AUTHOR, CLOCK_TB + "/#", 2);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase d = db.getReadableDatabase();
        int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case 1:
                cursor = d.query(CLOCK_TB, null, null, null, null, null, null);
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase d = db.getWritableDatabase();
        int matcher = uriMatcher.match(uri);
        long rowId;
        switch (matcher) {
            case 1:
                rowId = d.insert(CLOCK_TB, null, values);
                break;
            default:
                throw new IllegalArgumentException("Invalid URI");
        }
        if (rowId > 0) {
            Uri clockUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(clockUri, null);
            return clockUri;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase d = db.getWritableDatabase();
        int matcher = uriMatcher.match(uri);
        int count = 0;
        switch (matcher) {
            case 2:
                count = d.delete(CLOCK_TB, selection, selectionArgs);
                break;
        }
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase d = db.getWritableDatabase();
        int matcher = uriMatcher.match(uri);
        int count = 0;
        switch (matcher) {
            case 2:
                count = d.update(CLOCK_TB, values, selection, selectionArgs);
                break;
        }
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }
}
