package com.slinky.myyelp.yelp_api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YelpClient {

    public YelpAPI build() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {

                        return chain.proceed(chain
                                .request()
                                .newBuilder()
                                .addHeader("Authorization", "Bearer e7UrZfz1QkSdg454zNKDGmAJdSFyqxqmfZH5z35xW5SRSJRvizfZtqTtYjt-DkdOhny_-_SFRC6vFGW0GWezCp-mSfgC_Yhh-x33FnuCVMiTDmFuEjO4bgHV9KmjYnYx")
                                .build());
                    }
                }).build();
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
