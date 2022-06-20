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
import com.slinky.myyelp.logic.YelpAdapter;
import com.slinky.myyelp.logic.YelpRepo;
import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private ActivityFavoriteBinding binding;
    private YelpAdapter yelpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_favorite);
        Log.d(TAG, "onCreate: ");

        getSupportActionBar().hide();
        initUI();
        setNavigationDrawer();
        showFavouriteList();
    }

    @SuppressLint("NonConstantResourceId")
    private void setNavigationDrawer() {
        binding.navView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.drawer_search:
                    Log.d(TAG, "onNavigationItemSelected: search");
                    sendToMain();
                    break;
                case R.id.drawer_favorite:
                    Log.d(TAG, "onNavigationItemSelected: favorite");
                    binding.drawerLayoutFavorite.closeDrawer(GravityCompat.START);
                    break;
            }
            return true;
        });
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(FavoriteActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void showFavouriteList() {
        YelpRepo repo = YelpRepo.getInstance(this);
        List<YelpResponse.YelpBusiness> favouriteList;

        if (repo.getFavoriteFromDatabase().size() > 0) {
            favouriteList = repo.getFavoriteFromDatabase();
            Log.d(TAG, "showFavouriteList: " + favouriteList.size());

            setAdapterBool(true);
            yelpAdapter.setBusinessesList(favouriteList);

        } else {
            Toast.makeText(this, "No favourite yet", Toast.LENGTH_SHORT).show();
        }
    }

    private void initUI() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yelpAdapter = new YelpAdapter(this);
        binding.recyclerView.setAdapter(yelpAdapter);
    }

    private void setAdapterBool(boolean bool) {
        yelpAdapter.setIsFavorites(bool);
    }

    @Override
    public void finish() {
        super.finish();
        Log.d(TAG, "finish: set to false " );
        setAdapterBool(false); // set to false to avoid memory leak
    }
}