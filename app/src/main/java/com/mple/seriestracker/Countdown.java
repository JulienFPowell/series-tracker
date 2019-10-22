package com.mple.seriestracker;

import com.mple.seriestracker.activity.HomeScreenActivity;
import com.mple.seriestracker.util.CountdownUtil;
import com.mple.seriestracker.util.NotificationGenerator;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZonedDateTime;

public class Countdown {


    private int season;
    private int episode;
    private String name;
    private ZonedDateTime airDate;

    public Countdown(String name, int episode,int season,ZonedDateTime airDate){
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
        //No idea why this returns an absurd number, possibly something wrong with the time conversion
        //So the simple fix is to convert the days into hours, subtract the total hours with the days.
        //This returns the real value, and makes it accurate.
        long hours = duration.toHours()-(days*24);
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
