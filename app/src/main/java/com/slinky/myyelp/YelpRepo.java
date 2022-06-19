package com.slinky.myyelp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.slinky.myyelp.yelp_api.YelpAPI;
import com.slinky.myyelp.yelp_api.YelpClient;
import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YelpRepo {
    private final String TAG = getClass().getSimpleName();
    private static YelpRepo instance;
    private final Context context;

    private YelpRepo(Context context) {
        this.context = context;
    }
    // make this class singleton
    public static YelpRepo getInstance(Context context) {
        if (instance == null) {
            instance = new YelpRepo(context);
        }
        return instance;
    }

    public MutableLiveData<List<YelpResponse.YelpBusiness>> getYelpResponse(String query) {
       final  MutableLiveData<List<YelpResponse.YelpBusiness>> yelpResponseLiveData = new MutableLiveData<>();

       YelpAPI yelpAPI = new YelpClient().build();
       yelpAPI.getBusinesses(query, "Montreal").enqueue(new Callback<>() {
           @Override
           public void onResponse(@NonNull Call<YelpResponse> call, @NonNull Response<YelpResponse> response) {
               Log.d(TAG, "onResponse: " + response.body().toString());

               if (response.isSuccessful() && response.body() != null) {
                   yelpResponseLiveData.setValue(response.body().businesses);
                   Log.d(TAG, "onResponse size: " + response.body().toString());
                   if (response.body().total > 0) {
                       Toast.makeText(context, "Total: " + response.body().total, Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(context, "No results found", Toast.LENGTH_LONG).show();
                   }               }
           }

           @Override
           public void onFailure(Call<YelpResponse> call, Throwable t) {
               Log.d("SlinkyTest", "Error: " + t.getMessage());
           }
       });
       return yelpResponseLiveData;
    }
    public MutableLiveData<List<YelpResponse.YelpBusiness>> getYelpResponse(String query, String sortBy) {
       final  MutableLiveData<List<YelpResponse.YelpBusiness>> yelpResponseLiveData = new MutableLiveData<>();

       YelpAPI yelpAPI = new YelpClient().build();
       yelpAPI.getBusinesses(query, "Montreal", "rating").enqueue(new Callback<>() {
           @Override
           public void onResponse(@NonNull Call<YelpResponse> call, @NonNull Response<YelpResponse> response) {
               Log.d(TAG, "onResponse: " + response.body().toString());

               if (response.isSuccessful() && response.body() != null) {
                   yelpResponseLiveData.setValue(response.body().businesses);
                   Log.d(TAG, "onResponse size: " + response.body().toString());
                   if (response.body().total > 0) {
                       Toast.makeText(context, "Total: " + response.body().total, Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(context, "No results found", Toast.LENGTH_LONG).show();
                   }
               }
           }

           @Override
           public void onFailure(Call<YelpResponse> call, Throwable t) {
               Log.d("SlinkyTest", "Error: " + t.getMessage());
           }
       });
       return yelpResponseLiveData;
    }

    public void test(String businessName) {
        Toast.makeText(context, "Clicked: " + businessName, Toast.LENGTH_SHORT).show();
        //TODO implement database saving call here
    }
    // save the list in the cache
    public void saveLastQuery(String query) {

    }
}
