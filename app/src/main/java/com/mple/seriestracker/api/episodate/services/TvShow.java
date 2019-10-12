package com.mple.seriestracker.api.episodate.services;

import com.mple.seriestracker.api.episodate.entities.show.TvShowResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TvShow {
    @GET("show-details")
    Call<TvShowResult> textQuery(
            @Query("q") String showID
    );
}
