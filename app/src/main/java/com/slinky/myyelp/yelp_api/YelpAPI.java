package com.slinky.myyelp.yelp_api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YelpAPI {
    /**
     * standard call method
     * @param term
     * @param location
     * @return
     */
    @GET("businesses/search")
    Call<YelpResponse> getBusinesses(@Query("term") String term,
                                     @Query("location") String location);

    /**
     * @deprecated USE yelpRepo.sortByRating() or yelpRepo.sortByPrice() instead
     * @param term search term
     * @param location location
     * @param sortBy sort by
     * @return yelp response
     */
    @GET("businesses/search")
    Call<YelpResponse> getBusinesses(@Query("term") String term,
                                     @Query("location") String location,
                                     @Query("sort_by") String sortBy);

}
