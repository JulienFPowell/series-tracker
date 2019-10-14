package com.mple.seriestracker.api.episodate;

import androidx.annotation.Nullable;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.mple.seriestracker.api.episodate.services.Search;
import com.mple.seriestracker.api.episodate.services.TvShow;
import com.uwetrottmann.trakt5.TraktV2Helper;
import com.uwetrottmann.trakt5.enums.ListPrivacy;
import com.uwetrottmann.trakt5.enums.Rating;
import com.uwetrottmann.trakt5.enums.SortBy;
import com.uwetrottmann.trakt5.enums.SortHow;
import com.uwetrottmann.trakt5.enums.Status;

import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Episodate {

    public static final String API_HOST = "episodate.com/api";

    public static final String SITE_URL = "https://www.episodate.com/";
    public static final String API_URL = "https://" + API_HOST + "/";

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_JSON = "application/json";

    public static Episodate INSTANCE = new Episodate();

    @Nullable
    private OkHttpClient okHttpClient;
    @Nullable private Retrofit retrofit;

    protected Retrofit.Builder retrofitBuilder() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient());
    }


    protected Retrofit retrofit() {
        if (retrofit == null) {
            retrofit = retrofitBuilder().build();
        }
        return retrofit;
    }

    protected synchronized OkHttpClient okHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    public Search search(){
        return retrofit().create(Search.class);
    }

    public TvShow show() {return retrofit().create(TvShow.class);}

}
