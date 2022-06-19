package com.slinky.myyelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.slinky.myyelp.databinding.ActivityMainBinding;
import com.slinky.myyelp.yelp_api.YelpClient;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private SearchView searchView;
    private Boolean isDefault = true;
    // access MainActivity Binder
    private ActivityMainBinding binding;
    private YelpAdapter yelpAdapter;
    private String lastQuery = "";
    YelpViewModel yelpViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initUI();
        spinner();
        defaultQuery();
  }

    private void initUI() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yelpAdapter = new YelpAdapter(this);
        binding.recyclerView.setAdapter(yelpAdapter);
    }

  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);

    setSearchView(menu);
    searchViewListener();
    return super.onCreateOptionsMenu(menu);
  }

  private void setSearchView(Menu menu) {
      MenuItem searchItem = menu.findItem(R.id.search_bar_ID);

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
        /*if (query.equals(lastQuery)) {
            return;
        }*/
        if (!query.equalsIgnoreCase("poutine")) {
            isDefault = false;
        }
        lastQuery = query;
      if (YelpClient.isNetworkAvailable(this)) {
          yelpViewModel = new YelpViewModel(this);

          yelpViewModel.getYelpResponse(query).observe(this, yelpBusinesses -> {
              if (yelpBusinesses != null && !yelpBusinesses.isEmpty()) {
                  Log.d(TAG, "observe onChanged: " + yelpBusinesses.size());

                    yelpAdapter.setBusinessesList(yelpBusinesses);
                    yelpAdapter.notifyDataSetChanged(); //TODO improve this
              }
          });
      } else {
          Toast.makeText(this, "No network available", Toast.LENGTH_LONG).show();
      }
  }
    private void requestYelpResponse(String query, String sortBy) {
        if (!query.equalsIgnoreCase("poutine")) {
            isDefault = false;
        }
      if (YelpClient.isNetworkAvailable(this)) {
          yelpViewModel = new YelpViewModel(this);

          yelpViewModel.getYelpResponse(query, sortBy).observe(this, yelpBusinesses -> {
              if (yelpBusinesses != null && !yelpBusinesses.isEmpty()) {
                  Log.d(TAG, "observe onChanged: " + yelpBusinesses.size());

                    yelpAdapter.setBusinessesList(yelpBusinesses);
                    yelpAdapter.notifyDataSetChanged(); //TODO improve this
              }
          });
      } else {
          Toast.makeText(this, "No network available", Toast.LENGTH_LONG).show();
      }
  }

  //create a default query if no query is provided with the search "poutine
    private void defaultQuery() {
        requestYelpResponse(lastQuery);
        Log.d(TAG, "defaultQuery");
        isDefault = true;
    }

    //set spinner listener onSelectItem
    private void spinner() {
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = parent.getItemAtPosition(position).toString();
                switch (query.toLowerCase()) {
                    case "rating":
                        Log.d(TAG, "onItemSelected: rating");
//                        requestYelpResponse(lastQuery, "rating");
                        sortByRating();
                        break;
                    case "price":
                        Log.d(TAG, "onItemSelected: price");
                        sortByPrice();
                        break;
                    default:
                        Log.d(TAG, "onItemSelected: default");
                        if (!isDefault) {
                            defaultQuery();
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void sortByPrice() {
        yelpViewModel.sortByPrice(getApplicationContext());
    }
    private void sortByRating() {
        yelpViewModel.sortByRating();
    }

}

