package com.mple.seriestracker.api.episodate.services;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TvShow {
    @GET("show-details")
    Call<com.mple.seriestracker.api.episodate.entities.show.TvShow> textQuery(
            @Query("q") String showID
    );
}
