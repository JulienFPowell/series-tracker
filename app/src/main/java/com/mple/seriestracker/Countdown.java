package com.mple.seriestracker;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

public class Countdown {


    private int season;
    private int episode;
    private int name;
    private LocalDateTime airDate;

    public Countdown(LocalDateTime airDate){
        this.airDate = airDate;
    }

    public String getCountdown(){
        Duration duration = Duration.between(LocalDateTime.now(),airDate);
        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes= duration.toMinutes();
        long seconds = duration.getSeconds();
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
        string = x==1? string+"s":string;
        return String.format("%l %s ",x,string);
    }
}
