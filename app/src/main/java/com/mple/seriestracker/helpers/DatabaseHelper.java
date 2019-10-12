package com.mple.seriestracker.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SeriesTracker.db";

    String SQL_Create_Entries;
    String SQL_Delete_Entries;

    public DatabaseHelper(Context context,String sqlCreate,String sqlDelete) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.SQL_Create_Entries = sqlCreate;
        this.SQL_Delete_Entries = sqlDelete;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_Create_Entries);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_Delete_Entries);
        onCreate(sqLiteDatabase);
    }
}
