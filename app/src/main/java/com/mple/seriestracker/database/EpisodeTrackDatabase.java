package com.mple.seriestracker.database;

import com.mple.seriestracker.helpers.DatabaseHelper;

public class EpisodeTrackDatabase {

    //https://developer.android.com/training/data-storage/room

    DatabaseHelper databaseHelper = new DatabaseHelper(null,SQL_CREATE_ENTRIES,SQL_DELETE_ENTRIES);

    private static final String SQL_CREATE_ENTRIES =
          "CREATE TABLE `Shows` (" +
                  "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                  "`name` TEXT NOT NULL, " + //Name of the tv show
                  "`data` TEXT NOT NULL, " + //Base 64 json file
                  "`time` TEXT NOT NULL, " + //Time added into DB (for caching purposes)
                  "`showID` BIGINT" +
                  ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + "`Shows`";

    public void addShow(String name, String json,long showID){

    }

    public void modifyShow(String data){

    }

    public void deleteShow(long showID){

    }
}
