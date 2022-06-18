package com.slinky.myyelp;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;

public class YelpViewModel extends ViewModel {

    private YelpRepo yelpRepo;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private MutableLiveData<List<YelpResponse.YelpBusiness>> yelpResponseLiveData;

    public YelpViewModel(Context context) {
        yelpRepo = new YelpRepo();
        this.context = context.getApplicationContext();
    }

    public LiveData<List<YelpResponse.YelpBusiness>> getYelpResponse(String query) {
        if (yelpResponseLiveData == null) {
            yelpResponseLiveData = yelpRepo.getYelpResponse(context, query);
        }
        return yelpResponseLiveData;
    }
}
