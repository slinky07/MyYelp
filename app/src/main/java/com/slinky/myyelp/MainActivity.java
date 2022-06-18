package com.slinky.myyelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
//    ArrayList<YelpResponse.YelpBusiness> businesses;
    private final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if the network is available
        if (YelpClient.isNetworkAvailable(this)) {
            // get the search query
            String query = getIntent().getStringExtra("query");
            YelpViewModel yelpViewModel = new YelpViewModel(this);
            yelpViewModel.getYelpResponse("burger").observe(this, yelpBusinesses -> {
                if (yelpBusinesses != null && !yelpBusinesses.isEmpty()) {
                    Log.d(TAG, "observe onChanged: " + yelpBusinesses.size());
                }
            });
        } else {
            Toast.makeText(this, "No network available", Toast.LENGTH_LONG).show();
        }
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

