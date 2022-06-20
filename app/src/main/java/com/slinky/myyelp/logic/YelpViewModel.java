package com.slinky.myyelp.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.List;

public class YelpViewModel extends ViewModel {

    private YelpRepo yelpRepo;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private MutableLiveData<List<YelpResponse.YelpBusiness>> yelpResponseLiveData; // live data to hold the yelp response

    public YelpViewModel(Context context) {
        yelpRepo = YelpRepo.getInstance(context);
        this.context = context.getApplicationContext();
    }

    public LiveData<List<YelpResponse.YelpBusiness>> getYelpResponse(String query) {
        if (yelpResponseLiveData == null) {
            yelpResponseLiveData = yelpRepo.getYelpResponse(query);
        }
        return yelpResponseLiveData;
    }

    public void sortByPrice(Context applicationContext) {
        context = applicationContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sortByPriceLogic();
        } else {
            Toast.makeText(context, "Api level under required level", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * sort by price logic where some values are null. put null values at the start of the list.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByPriceLogic() {
        if (yelpResponseLiveData != null) {
            List<YelpResponse.YelpBusiness> yelpBusinessList = yelpResponseLiveData.getValue();
            if (yelpBusinessList != null) {
                yelpBusinessList.sort((o1, o2) -> {
                    if (o1.price == null && o2.price == null) {
                        return 0;
                    } else if (o1.price == null) {
                        return -1;
                    } else if (o2.price == null) {
                        return 1;
                    } else {
                        return o1.price.compareTo(o2.price);
                    }
                });
                yelpResponseLiveData.setValue(yelpBusinessList);
            }
        }

    }

    public void sortByRating() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sortByRatingLogic();
        } else {
            Toast.makeText(context, "Api level under required level", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * sort list by float rating high to low  where some values are null. put null values at the start of the list.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
     private void sortByRatingLogic() {
        List<YelpResponse.YelpBusiness> yelpBusinessList = yelpResponseLiveData.getValue();
        if (yelpBusinessList != null) {
            for (int i = 1; i < yelpBusinessList.size(); i++) {
                YelpResponse.YelpBusiness temp = yelpBusinessList.get(i);
                int j = i;
                while (j > 0 && yelpBusinessList.get(j - 1).rating < temp.rating) {
                    yelpBusinessList.set(j, yelpBusinessList.get(j - 1));
                    j--;
                }
                yelpBusinessList.set(j, temp);
            }
            yelpResponseLiveData.setValue(yelpBusinessList);
        }
    }

    /***
     * get business of the list from the position
     * @param position the position of the list
     * @return  the business at the position
     */
    private YelpResponse.YelpBusiness getYelpBusiness(int position) {
        if (yelpResponseLiveData != null) {
            List<YelpResponse.YelpBusiness> yelpBusinessList = yelpResponseLiveData.getValue();
            if (yelpBusinessList != null) {
                return yelpBusinessList.get(position);
            }
        }
        return null;
    }
}
