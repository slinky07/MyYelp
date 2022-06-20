package com.slinky.myyelp.logic;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.slinky.myyelp.database.DatabaseLogic;
import com.slinky.myyelp.database.LocalYelpDatabase;
import com.slinky.myyelp.yelp_api.YelpAPI;
import com.slinky.myyelp.yelp_api.YelpClient;
import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YelpRepo {
    private final String TAG = getClass().getSimpleName();
    private static YelpRepo instance;
    private Context context;
    LocalYelpDatabase database;

    private YelpRepo(Context context) {
        this.context = context;
        this.database = LocalYelpDatabase.getInstance(context);
    }
    // singleton
    public static YelpRepo getInstance(Context context) {
        if (instance == null) {
            instance = new YelpRepo(context);
        }
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Get the list of businesses from the API
     * @param query
     * @return
     */
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
               Log.d(TAG, "Error: " + t.getMessage());
           }
       });
       return yelpResponseLiveData;
    }

    /**
     * Get all favorites from database
     * @return List of YelpBusiness
     */
    public List<YelpResponse.YelpBusiness> getFavoriteFromDatabase() {
        // create a new array list to store the favorite yelp businesses
        Cursor favoriteYelpBusinessesCursor = database.getAll();
        List<YelpResponse.YelpBusiness> favoriteYelpBusinesses = new ArrayList<>();
        while (favoriteYelpBusinessesCursor.moveToNext()) {
            favoriteYelpBusinesses.add(new YelpResponse.YelpBusiness(favoriteYelpBusinessesCursor));
        }
        return favoriteYelpBusinesses;
    }

    /**
     * create an alert to ask the user if they want to insert this yelp business into the database
     * @param yelpBusiness the yelp business to insert
     * @return an alert dialog
     */
    public AlertDialog.Builder askUserIfAddToDatabase(YelpResponse.YelpBusiness yelpBusiness, DatabaseLogic dbl) {
        Log.d(TAG, "askUserIfAddToDatabase: " + yelpBusiness.name);
        return buildDialog(null, yelpBusiness,-1 , dbl);
    }

    /**
     * create an alert to ask the user if they want to delete this yelp business from their favorites
     * @param adapter position of the yelp business in the adapter
     * @param yelpBusiness the yelp business to delete
     * @param position the position of the yelp business in the adapter
     * @param dbl    the database logic
     * @return an alert dialog
     */
    public AlertDialog.Builder askRemoveFromDB(YelpAdapter adapter, YelpResponse.YelpBusiness yelpBusiness, int position, DatabaseLogic dbl) {
        Log.d(TAG, "askUserIfRemoveFromDatabase: " + yelpBusiness.name);
        return buildDialog(adapter, yelpBusiness, position, dbl);
    }

    /**
     * generic alert dialog builder
     * @param adapter the adapter to ask to refresh. can be null if you are not removing.
     * @param yelpBusiness the yelp business to insert or delete
     * @param position the position of the yelpBusiness in the adapter. can be null if you are not removing.
     * @param dbl the database logic
     * @return an alert dialog
     */
    private AlertDialog.Builder buildDialog(YelpAdapter adapter,YelpResponse.YelpBusiness yelpBusiness, int position, DatabaseLogic dbl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Remove from favorites?");
        builder.setMessage(yelpBusiness.name);
        builder.setPositiveButton("Yes", (dialog, which) -> {
            databaseLogic(dbl, yelpBusiness);
            if (adapter != null) {
                adapter.notifyItemRemoved(position);
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        return builder;
    }

    private void databaseLogic(DatabaseLogic databaseLogic, YelpResponse.YelpBusiness yelpBusiness) {
        databaseLogic.action(yelpBusiness);
    }
}
