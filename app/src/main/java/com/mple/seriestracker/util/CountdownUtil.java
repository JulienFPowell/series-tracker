package com.mple.seriestracker.util;

import com.mple.seriestracker.Countdown;
import com.mple.seriestracker.api.episodate.entities.show.Episode;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

public class CountdownUtil {

    //All air dates are formatted in this format
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    public static ZonedDateTime parseToLocal(String s){
        if(s == null) return null;
        return LocalDateTime.parse(s, DATE_TIME_FORMATTER)
                .atOffset(ZoneOffset.UTC)
                .atZoneSameInstant(ZoneId.systemDefault());
    }

    public static boolean isOlderEpisode(LocalDateTime airDate, LocalDateTime currEpDate){
        return currEpDate.isBefore(airDate);
    }

    public static boolean isOlderEpisode(OffsetDateTime airDate, OffsetDateTime currEpDate){
        return currEpDate.toLocalDate().isBefore(airDate.toLocalDate());
    }

    //Responsible for finding a certain episode
    public static Countdown getUpcomingAiringEp(Episode[] episodes, int episode, int season) {
        if (episodes == null) {
            return null;
        }

        //Loop in reverse, since the episodes are ordered from start to finish
        //So looping from reverse will start with the newer shows first
        for (int i = (episodes.length - 1); i >= 0; i--) {
            Episode newEpisode = episodes[i];


            if (newEpisode.air_date != null && newEpisode.season == season && newEpisode.episode == episode) {
                return new Countdown(newEpisode.name,newEpisode.episode, newEpisode.season, parseToLocal(newEpisode.air_date));
            }

            if(newEpisode.season <= (newEpisode.season - 1)) {
                break;
            }
        }

        return null;
    }
}
