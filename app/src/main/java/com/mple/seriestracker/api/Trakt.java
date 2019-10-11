package com.mple.seriestracker.api;

import com.uwetrottmann.trakt5.TraktV2;
import com.uwetrottmann.trakt5.services.Search;
import com.uwetrottmann.trakt5.services.Shows;

public class Trakt {

    public static Trakt INSTANCE = new Trakt();
    TraktV2 traktV2 = new TraktV2("09c06e53a115cb917664a391e76da62d1ac8a4729987bb7ec413537ca454f1af");//api key

    public TraktV2 getClient() {
        return traktV2;
    }

    public Shows getShows(){
        return traktV2.shows();
    }

    public Search getSearch(){
        return traktV2.search();
    }

}
