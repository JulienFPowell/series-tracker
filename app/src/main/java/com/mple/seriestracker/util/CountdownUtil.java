package com.mple.seriestracker.util;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;

public class CountdownUtil {

    public static boolean isOlderEpisode(LocalDateTime airDate, LocalDateTime currEpDate){
        return currEpDate.isBefore(airDate);
    }

    public static boolean isOlderEpisode(OffsetDateTime airDate, OffsetDateTime currEpDate){
        return currEpDate.toLocalDate().isBefore(airDate.toLocalDate());
    }
}
