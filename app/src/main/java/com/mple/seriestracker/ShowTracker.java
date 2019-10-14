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
    public HashSet<Long> addedShowsCache = new HashSet<>();

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

    //Returns null if it can't get the show
//    public TvShow getTvShow(long showID){
//        ShowData showData = EpisodeTrackDatabase.INSTANCE.findShow(showID);
//        if(showData == null) return null;//Nothing from cache found, return null.
//        String json = Base64Util.decodeString(showData.data);
//        if(json == null) return null; //Something went wrong with decoding the string.
//        Gson gson = new Gson();
//        //Json to object conversion should never fail, as the layout will always be the same.
//        TvShowResult tvShowObject = gson.fromJson(json,TvShowResult.class);
//        TvShow tvShow = new TvShow(tvShowObject.id,tvShowObject.name);
//        com.mple.seriestracker.api.episodate.entities.show.Countdown countdown = tvShowObject.countdown;
//        tvShow.setCountdown(new Countdown(countdown.name,countdown.episode,countdown.season, CountdownUtil.parseToLocal(countdown.air_date)));
//        tvShow.setEpisodes(tvShowObject.episodes.toArray(new Episode[tvShowObject.episodes.size()]));
//        return tvShow;
//    }
//
//    //Returns null if it can't get the show
//    public TvShow getTvShow(ShowData showData){
//        if(showData == null) return null;
//        String json = Base64Util.decodeString(showData.data);
//        if(json == null) return null; //Something went wrong with decoding the string.
//        Gson gson = new Gson();
//        //Json to object conversion should never fail, as the layout will always be the same.
//        TvShowResult tvShowObject = gson.fromJson(json,TvShowResult.class);
//        TvShow tvShow = new TvShow(tvShowObject.id,tvShowObject.name);
//        com.mple.seriestracker.api.episodate.entities.show.Countdown countdown = tvShowObject.countdown;
//        tvShow.setCountdown(new Countdown(countdown.name,countdown.episode,countdown.season, CountdownUtil.parseToLocal(countdown.air_date)));
//        tvShow.setEpisodes(tvShowObject.episodes.toArray(new Episode[tvShowObject.episodes.size()]));
//        return tvShow;
//    }

}
