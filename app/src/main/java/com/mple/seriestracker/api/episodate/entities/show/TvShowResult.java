package com.mple.seriestracker.api.episodate.entities.show;

import java.util.List;

public class TvShowResult {
    public long id;
    public String name;
    public String permaLink;
    public String url;
    public String description;
    public String description_source;
    public String start_date;
    public String end_date;
    public String country;
    public int runtime;
    public String network;
    public String youtube_link;
    public String image_path;
    public String image_thumbnail_path;
    public String rating;
    public String rating_count;

    public List<Episode> episodes;
    public Countdown countdown;


}
