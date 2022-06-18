package com.slinky.myyelp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.slinky.myyelp.yelp_api.YelpClient;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  }

  @SuppressLint("UseCompatLoadingForDrawables")
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);

    setSearchView(menu);
    searchViewListener();
    return super.onCreateOptionsMenu(menu);
  }

  private void setSearchView(Menu menu) {
      MenuItem searchItem = menu.findItem(R.id.search_bar_ID); //TODO implement binder
      searchView = (SearchView) searchItem.getActionView();
      searchView.setQueryHint(getString(R.string.search_bar_hint));
  }

  private void searchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();

                requestYelpResponse(query);

                Log.d(TAG, "onQueryTextSubmit: " + query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "onQueryTextChange: " + newText);
                return false;
            }
        });
  }

  private void requestYelpResponse(String query) {
      if (YelpClient.isNetworkAvailable(this)) {
          YelpViewModel yelpViewModel = new YelpViewModel(this);

          yelpViewModel.getYelpResponse(query).observe(this, yelpBusinesses -> {
              if (yelpBusinesses != null && !yelpBusinesses.isEmpty()) {
                  Log.d(TAG, "observe onChanged: " + yelpBusinesses.size());
              }
          });
      } else {
          Toast.makeText(this, "No network available", Toast.LENGTH_LONG).show();
      }
  }

}

