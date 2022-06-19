package com.slinky.myyelp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.slinky.myyelp.yelp_api.YelpResponse;

import java.util.Comparator;
import java.util.List;

public class YelpViewModel extends ViewModel {

    private YelpRepo yelpRepo;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private MutableLiveData<List<YelpResponse.YelpBusiness>> yelpResponseLiveData;

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

    // return list

    // notify this method is osbeleted

    /**
     * @deprecated  this method is obsolete for the assignment's purpose
     * Yelp Api sorts by their own internal logic, not by actual rating digit.
     * so a lower rating will be higher in the list if it has more reviews, for example.
     * @param query the query string
     * @param sortBy is sort kind.
     * @return list of yelp businesses sorted by the sortBy kind.
     */
    public LiveData<List<YelpResponse.YelpBusiness>> getYelpResponse(String query, String sortBy) {
        if (yelpResponseLiveData == null) {
            yelpResponseLiveData = yelpRepo.getYelpResponse(query, sortBy);
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
    // sort by price logic where some values are null. put null values at the start of the list.
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortByPriceLogic() {
        if (yelpResponseLiveData != null) {
            List<YelpResponse.YelpBusiness> yelpBusinessList = yelpResponseLiveData.getValue();
            if (yelpBusinessList != null) {
                yelpBusinessList.sort(new Comparator<YelpResponse.YelpBusiness>() {
                    @Override
                    public int compare(YelpResponse.YelpBusiness o1, YelpResponse.YelpBusiness o2) {
                        if (o1.price == null && o2.price == null) {
                            return 0;
                        } else if (o1.price == null) {
                            return -1;
                        } else if (o2.price == null) {
                            return 1;
                        } else {
                            return o1.price.compareTo(o2.price);
                        }
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
    // sort list by float rating high to low  where some values are null. put null values at the start of the list.
    @RequiresApi(api = Build.VERSION_CODES.N)
 /*   private void sortByRatingLogic() {
        if (yelpResponseLiveData != null) {
            List<YelpResponse.YelpBusiness> yelpBusinessList = yelpResponseLiveData.getValue();
            if (yelpBusinessList != null) {
                yelpBusinessList.sort(new Comparator<YelpResponse.YelpBusiness>() {
                    @Override
                    public int compare(YelpResponse.YelpBusiness o1, YelpResponse.YelpBusiness o2) {
                        if (o1.rating == null && o2.rating == null) {
                            return 0;
                        } else if (o1.rating == null) {
                            return -1;
                        } else if (o2.rating == null) {
                            return 1;
                        } else {
                            return Float.compare(o2.rating, o1.rating);
                        }
                    }
                });
                yelpResponseLiveData.setValue(yelpBusinessList);
            }
        }
    }
*/
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

    private String getStr(float f) {
        return f + "";
    }

    public void saveLastQuery(String query) {
        yelpRepo.saveLastQuery(query);
    }




}
