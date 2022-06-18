package com.slinky.myyelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.slinky.myyelp.yelp_api.YelpAPI;
import com.slinky.myyelp.yelp_api.YelpClient;
import com.slinky.myyelp.yelp_api.YelpResponse;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
//    ArrayList<YelpResponse.YelpBusiness> businesses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MutableLiveData<ArrayList<YelpResponse.YelpBusiness>> businesses = new MutableLiveData<>();

        //-------------------------------

        YelpAPI yelpAPI = new YelpClient().build();
        yelpAPI.getBusinesses("burger", "Montreal", 50, 50).enqueue(new Callback<YelpResponse>() {
            @Override
            public void onResponse(Call<YelpResponse> call, Response<YelpResponse> response) {
                YelpResponse res = response.body();
                Log.d("SlinkyTest", res.toString());
                Toast.makeText(getApplicationContext(), "Total: " + res.total, Toast.LENGTH_LONG).show();
                businesses.setValue(res.businesses);
            }

            @Override
            public void onFailure(Call<YelpResponse> call, Throwable t) {

            }
        });

  }

  @SuppressLint("UseCompatLoadingForDrawables")
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);

      MenuItem searchItem = menu.findItem(R.id.search_bar_ID);
      SearchView searchView = (SearchView) searchItem.getActionView();
      // set hint string from search_bar_hint ID
//      searchView.setIconifiedByDefault(false);
      searchView.setQueryHint(getString(R.string.search_bar_hint));
      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
          @Override
          public boolean onQueryTextSubmit(String query) {
              searchView.clearFocus();
              Toast.makeText(getApplicationContext(), "Searching for: " + query, Toast.LENGTH_LONG).show();
              return false;
          }

          @Override
          public boolean onQueryTextChange(String newText) {
              return false;
          }
      });
    return super.onCreateOptionsMenu(menu);
  }
}

