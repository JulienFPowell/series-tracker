package com.mple.seriestracker.api.episodate.entities;

import java.util.List;

public class SearchResult {
    public String total;
    public int page;
    public int pages;

    public List<ShowSearchResult> tv_shows;
}
