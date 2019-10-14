package com.mple.seriestracker;

import com.mple.seriestracker.util.CountdownUtil;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;

public class Countdown {


    private int season;
    private int episode;
    private String name;
    private LocalDateTime airDate;

    public Countdown(String name, int episode,int season,LocalDateTime airDate){
        this.airDate = airDate;
        this.name = name;
        this.episode = episode;
        this.season = season;
    }


    public Countdown(com.mple.seriestracker.api.episodate.entities.show.Countdown countdown){
        this.airDate = CountdownUtil.parseToLocal(countdown.air_date);
        this.name = countdown.name;
        this.episode = countdown.episode;
        this.season = countdown.season;
    }

    public String getCountdownFormat(){
        Duration duration = Duration.between(LocalDateTime.now(),airDate);
        long days = duration.toDays();
        long hours = duration.toHours(); //No idea why this returns an absurd number, possibly something wrong with the time conversion (can fix later)
        int minutes = (int) ((duration.getSeconds() % (60 * 60)) / 60);
        int seconds = (int) (duration.getSeconds() % 60);
        String timeString = "";
        if(days > 0){
            timeString+=formatDay(days);
        }
        if(hours > 0){
            timeString+=formatHour(hours);
        }

        if(minutes > 0){
            timeString+= formatMinutes(minutes);
        }

        if(seconds > 0){
            timeString += formatSeconds(seconds);
        }

        return timeString;
    }

    public String getName() {
        return name;
    }

    public int getEpisode() {
        return episode;
    }

    public int getSeason() {
        return season;
    }

    private String formatDay(long days){
        return format(days,"day");
    }

    private String formatHour(long hours){
        return format(hours,"hour");
    }

    private String formatMinutes(long minutes){
        return format(minutes,"minute");
    }

    private String formatSeconds(long seconds){
        return format(seconds,"second");
    }

    private String format(long x,String nonPlural){
        //Checks whether or not a plural should be added
        String string = nonPlural;
        if(x > 1)
            string+="s";

        return String.format("%s %s ",x,string);
    }
}
