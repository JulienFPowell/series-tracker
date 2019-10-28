package com.mple.seriestracker;

import java.util.HashMap;
import java.util.HashSet;

public class ShowTracker {

    public static ShowTracker INSTANCE = new ShowTracker();

    //Responsible for handling all json info for each show, as it will be cached
//    public HashMap<Long, ShowData> showDataCache = new HashMap<>();
    public HashMap<Long,TvShow> tvShowCache = new HashMap<>();

    //Responsible for keeping track of what countdown is populated in the countdowns tab
    public HashSet<Long> calenderCache = new HashSet<>();

    //Responsible for keeping t rack of what is added
//    public HashSet<Long> addedShowsCache = new HashSet<>();

    private ShowTracker(){

    }

    public void addTvShow(TvShow tvShow){
        synchronized (ShowTracker.class){
            tvShowCache.put(tvShow.getId(),tvShow);
        }
    }

    public TvShow getTvShow(long showID){
        synchronized (ShowTracker.class){
            return tvShowCache.get(showID);
        }
    }

}
