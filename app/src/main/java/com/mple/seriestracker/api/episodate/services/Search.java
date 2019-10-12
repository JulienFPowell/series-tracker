package com.mple.seriestracker.api.episodate.services;

import com.mple.seriestracker.api.episodate.entities.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Search {

    @GET("search")
    Call<SearchResult> textQuery(
            @Query("q") String query,
            @Query("page") Integer page
    );
}
