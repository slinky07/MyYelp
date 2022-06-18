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

    public MutableLiveData<List<YelpResponse.YelpBusiness>> getYelpResponse(Context context , String query) {
       final  MutableLiveData<List<YelpResponse.YelpBusiness>> yelpResponseLiveData = new MutableLiveData<>();

       YelpAPI yelpAPI = new YelpClient().build();
       yelpAPI.getBusinesses(query, "Montreal", 50, 50).enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(@NonNull Call<YelpResponse> call, @NonNull Response<YelpResponse> response) {
                Log.d(TAG, "onResponse: " + response.body().toString());

                if (response.isSuccessful() && response.body() != null) {
                    yelpResponseLiveData.setValue(response.body().businesses);
                    Log.d(TAG, "onResponse size: " + response.body().toString());
                    Toast.makeText(context, "Total: " + response.body().total, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {
                Log.d("SlinkyTest", "Error: " + t.getMessage());
            }
       });
       return yelpResponseLiveData;
    }
}
