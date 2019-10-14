package com.mple.seriestracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.mple.seriestracker.ShowInfo;
import com.mple.seriestracker.helpers.DatabaseHelper;
import com.mple.seriestracker.util.Base64Util;

import java.util.ArrayList;
import java.util.List;

public class EpisodeTrackDatabase extends DatabaseHelper{

    public static EpisodeTrackDatabase INSTANCE;

    public static void setInstance(EpisodeTrackDatabase episodeTrackDatabase){
        INSTANCE = episodeTrackDatabase;
    }


    private static final String TABLE_NAME = "Shows";
    private static final String SQL_CREATE_ENTRIES =
          "CREATE TABLE `Shows` (" +
                  "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                  "`name` TEXT NOT NULL, " + //Name of the tv show
                  "`photo` TEXT NOT NULL," + //The photo url
                  "`showID` BIGINT" +
                  ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + "`Shows`";

    SQLiteDatabase db;


    public EpisodeTrackDatabase(Context context) {
        super(context,SQL_CREATE_ENTRIES,SQL_DELETE_ENTRIES);
        db = this.getWritableDatabase();
    }

    //Opens the DB for reading
    public EpisodeTrackDatabase openReadable() throws SQLException {
        db = this.getReadableDatabase();
        return this;
    }

    //Opens the DB for writing
    public EpisodeTrackDatabase openWriteable() throws SQLException {
        db = this.getWritableDatabase();
        return this;
    }

    //gets all show ids from the database
    public long[] getShowIDs(){
        String sql = String.format("SELECT `showID` FROM %s",TABLE_NAME);
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        long[] longArr = new long[cursor.getCount()];
        if(cursor.getCount() > 0){
            for(int i=0;i<cursor.getCount();++i){
                longArr[i] = (cursor.getLong(0));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return longArr;
    }

    //gets all shows from the database
    public ShowInfo[] getAllShows(){
        List<ShowInfo> list = new ArrayList();
        for (long showID : getShowIDs()) {
            ShowInfo showData = findShow(showID);
            if(showData != null){ //If found
                list.add(showData); //Add to list
            }
        }
       return list.stream().toArray(ShowInfo[]::new); //return list of found shows
    }

    //Finds the show requested, returns null if not found
    public ShowInfo findShow(long showID){
        String sql = String.format("SELECT * FROM %s WHERE showID = %d",TABLE_NAME,showID);
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        ShowInfo showInfo = null;
        if(cursor.moveToFirst()){
            showInfo = new ShowInfo();
            showInfo.name = cursor.getString(1);
            showInfo.imagePath= Base64Util.decodeString(cursor.getString(2));
            showInfo.id= Long.parseLong(cursor.getString(3));
        }
        cursor.close();
        db.close();
        return showInfo;
    }

    //Checks whether the show exists in the DB, based on showID
    public boolean showExists(long showID){
        String sql = String.format("SELECT * FROM %s WHERE showID = %d",TABLE_NAME,showID);
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        cursor.close();
        return cursor.getCount() > 0;
    }

    //Adds the show to the database
    //Exits the function early if the show already exists in DB
    public void addShow(String name, String photo,long showID){
        db = getWritableDatabase();
        if(showExists(showID)){
            return;
        }

        String sql = String.format("INSERT INTO %s VALUES(NULL,'%s','%s',%s)",TABLE_NAME,name,Base64Util.encodeString(photo),showID);
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }

    public void deleteShow(long showID){
        String sql = String.format("DELETE FROM %s WHERE showID = %d",TABLE_NAME,showID);
        db = getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        cursor.close();
        db.close();
    }
}
