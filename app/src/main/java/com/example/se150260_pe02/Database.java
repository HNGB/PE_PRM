package com.example.se150260_pe02;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "ClockSQLite.db,", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTB ="CREATE TABLE Clock (" +
                " id INTEGER NOT NULL PRIMARY KEY" +
                " AUTOINCREMENT," +
                " name TEXT NOT NULL," +
                " price INTEGER NOT NULL)";
        db.execSQL(createTB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
