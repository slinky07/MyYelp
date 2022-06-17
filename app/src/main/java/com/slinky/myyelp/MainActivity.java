package com.slinky.myyelp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.slinky.myyelp.yelp_api.YelpAPI;
import com.slinky.myyelp.yelp_api.YelpClient;
import com.slinky.myyelp.yelp_api.YelpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-------------------------------

        YelpAPI yelpAPI = new YelpClient().build();
        yelpAPI.getBusinesses("burger", "Montreal", 50, 50).enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                YelpResponse res = response.body();
                Log.d("SlinkyTest", res.toString());
                // make toast getting total from json
                Toast.makeText(getApplicationContext(), "Total: " + res.total, Toast.LENGTH_LONG).show();
                
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {

            }
        });

  }
}

