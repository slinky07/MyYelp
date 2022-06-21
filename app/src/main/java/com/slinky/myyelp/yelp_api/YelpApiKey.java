package com.slinky.myyelp.yelp_api;

import com.slinky.myyelp.BuildConfig;

public class YelpApiKey {
    public static String getKey() {
        return BuildConfig.KEY;
    }
}
