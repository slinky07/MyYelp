package com.slinky.myyelp.yelp_api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpClient {
    private final String KEY = YelpApiKey.getKey();

    public YelpAPI build() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> chain.proceed(chain
                        .request()
                        .newBuilder()
                        .addHeader("Authorization", KEY)
                        .build())).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.yelp.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return retrofit.create(YelpAPI.class);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
