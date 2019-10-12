package com.mple.seriestracker.util;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

public class CountdownUtil {

    //All air dates are formatted in this format
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    public LocalDateTime parseToLocal(String s){
        return LocalDateTime.parse(s,DATE_TIME_FORMATTER);
    }

    public static boolean isOlderEpisode(LocalDateTime airDate, LocalDateTime currEpDate){
        return currEpDate.isBefore(airDate);
    }

    public static boolean isOlderEpisode(OffsetDateTime airDate, OffsetDateTime currEpDate){
        return currEpDate.toLocalDate().isBefore(airDate.toLocalDate());
    }
}
