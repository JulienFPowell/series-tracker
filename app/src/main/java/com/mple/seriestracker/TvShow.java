package com.mple.seriestracker;

import com.mple.seriestracker.api.episodate.entities.show.Episode;
import com.mple.seriestracker.api.episodate.entities.show.TvShowResult;

public class TvShow {

    private String name;
    private String imageUrl;
    private long id;

    Episode[] episodes;
    Countdown countdown;

    public TvShow(TvShowResult tvShowResult){
        this.name = tvShowResult.name;
        this.id = tvShowResult.id;
        this.imageUrl = tvShowResult.image_thumbnail_path;
        this.episodes = tvShowResult.episodes.stream().toArray(Episode[]::new);
        if(tvShowResult.countdown != null)
            this.countdown = new Countdown(tvShowResult.countdown);
    }

    public Countdown getCountdown() {
        return countdown;
    }

    public Episode[] getEpisodes() {
        return episodes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCountdown(Countdown countdown) {
        this.countdown = countdown;
    }

    public void setEpisodes(Episode[] episodes) {
        this.episodes = episodes;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getId() {
        return id;
    }
}
