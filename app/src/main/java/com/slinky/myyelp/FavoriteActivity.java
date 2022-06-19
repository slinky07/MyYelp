package com.slinky.myyelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.slinky.myyelp.databinding.ActivityFavoriteBinding;
import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private ActivityFavoriteBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);        //hide the action bar
        Log.d(TAG, "onCreate: ");
        getSupportActionBar().hide();
        setNavigationDrawer();
        showFavouriteList();
    }
    @SuppressLint("NonConstantResourceId")
    private void setNavigationDrawer() {
        binding.navView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.drawer_search:
                    Log.d(TAG, "onNavigationItemSelected: search");
                    Intent searchIntent = new Intent(FavoriteActivity.this, MainActivity.class);
                    startActivity(searchIntent);
                    break;
                case R.id.drawer_favorite:
                    Log.d(TAG, "onNavigationItemSelected: favorite");
                    //close drawer
                    binding.drawerLayoutFavorite.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });
    }

    //create method to show the favourite list from yelpRepository
    private void showFavouriteList() {
        YelpRepo repo = YelpRepo.getInstance(this);
        //check ig there is any favourite
        List<YelpResponse.YelpBusiness> favouriteList = null;
        if (repo.getFavoriteFromDatabase().size() > 0) {
            favouriteList = repo.getFavoriteFromDatabase();
            Log.d(TAG, "showFavouriteList: " + favouriteList.size());
            //set recycle view layout manager
            //TODO this bugs, repair it
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView.setAdapter(new YelpAdapter(this));
        } else {
            Toast.makeText(this, "No favourite yet", Toast.LENGTH_SHORT).show();
        }
    }

}