package com.slinky.myyelp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.slinky.myyelp.databinding.ActivityMainBinding;
import com.slinky.myyelp.logic.YelpAdapter;
import com.slinky.myyelp.logic.YelpViewModel;
import com.slinky.myyelp.yelp_api.YelpClient;

public class MainActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private SearchView searchView;
    private Boolean isDefault = true;
    private String lastQuery = "";
    private String defaultQuery = "";
    private ActivityMainBinding binding;
    private YelpAdapter yelpAdapter;
    YelpViewModel yelpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        getSupportActionBar().hide();
        searchViewListener();
        initUI();
        spinner();
        defaultQuery();
        setNavigationDrawer();

   }

    private void initUI() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        yelpAdapter = new YelpAdapter(MainActivity.this);
        binding.recyclerView.setAdapter(yelpAdapter);

    }


    /**
     * This method is used to set the searchView listener
     */
    private void searchViewListener() {
       // initialize the searchView
        searchView = binding.searchView;
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

    /**
     * main request to Yelp API
     * @param query search query
     */
    private void requestYelpResponse(String query) {
        if (!query.equalsIgnoreCase(defaultQuery)) {
            isDefault = false;
        }
        lastQuery = query;
        if (YelpClient.isNetworkAvailable(MainActivity.this)) {
          yelpViewModel = new YelpViewModel(MainActivity.this);

          yelpViewModel.getYelpResponse(query).observe(MainActivity.this, yelpBusinesses -> {
              if (yelpBusinesses != null && !yelpBusinesses.isEmpty()) {
                  Log.d(TAG, "observe onChanged: " + yelpBusinesses.size());

                    yelpAdapter.setBusinessesList(yelpBusinesses);
                    yelpAdapter.notifyDataSetChanged(); //TODO improve this
              }
          });
        } else {
          Toast.makeText(MainActivity.this, "No network available", Toast.LENGTH_LONG).show();
    }
}


    /**
     * default query for the yelp api
     */
    private void defaultQuery() {
        defaultQuery = getDefaultQuery();
        Log.d(TAG, "defaultQuery: " + defaultQuery);
        requestYelpResponse(defaultQuery);
        isDefault = true;
    }

    /**
     * get random string from defaultFoods array
     * @return random string
     */
    private String getDefaultQuery() {
        return getResources().
                getStringArray(R.array.defaultFoods)
                [(int) (Math.random() * getResources().
                                getStringArray(R.array.defaultFoods).
                                length)];
    }

    /**
     * Spinner for sorting the results
     */
    private void spinner() {
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = parent.getItemAtPosition(position).toString();
                switch (query.toLowerCase()) {
                    case "rating":
                        Log.d(TAG, "onItemSelected: rating");
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
                Log.d(TAG, "onNothingSelected: ");
            }
        });
    }

    private void sortByPrice() {
        yelpViewModel.sortByPrice();
    }

    private void sortByRating() {
        yelpViewModel.sortByRating();
    }

    @SuppressLint("NonConstantResourceId")
    private void setNavigationDrawer() {
        binding.navView.setNavigationItemSelectedListener(menuItem -> {

            switch (menuItem.getItemId()) {
                case R.id.drawer_search:
                    Log.d(TAG, "onNavigationItemSelected: search");
                    //close drawer
                    binding.drawerLayoutMain.closeDrawer(GravityCompat.START);
                    break;
                case R.id.drawer_favorite:
                    Log.d(TAG, "onNavigationItemSelected: favorite");
                    //launch favorite activity
                    startFavoriteActivity();
                    break;
            }
            return true;
        });
    }

    private void startFavoriteActivity() {
        Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
        startActivity(intent);
    }

    @Override
        public void onRestart() {
            super.onRestart();
            Log.d(TAG, "onRestart"); // for logging
        }
    }

