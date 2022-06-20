package com.slinky.myyelp.yelp_api;

import android.annotation.SuppressLint;
import android.database.Cursor;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * YelpResponse is  class that represents the JSON response from the aYelp API.
 */
public class YelpResponse {

    @SerializedName("businesses")
    public List<YelpBusiness> businesses;

    @SerializedName("total") // at bottom of JSON response
    public int total;

    /**
     * YelpBusiness is a class that represents a single business returned from the Yelp API.
     */
    public static class YelpBusiness {

        @SerializedName("id")
        public String id;
        @SerializedName("name")
        public String name;
        @SerializedName("image_url")
        public String imageUrl;
        @SerializedName("url")
        public String url;
        @SerializedName("rating")
        public float rating;
        @SerializedName("price")
        public String price;
        @SerializedName("location")
        public YelpLocation location;
        @SerializedName("display_phone")
        public String displayPhone;
        @SerializedName("categories")
        public ArrayList<YelpCategory> categories;

        public String customCategory; // custom category for this business for favorites

        public boolean isFavorite; // is this business a favorite?

        /**
         * constructor for YelpBusiness class. only called when creating a new favourite
         * @param favoritesCursor cursor that contains the favorite data
         */
          @SuppressLint("Range")
          public YelpBusiness(Cursor favoritesCursor) {
                isFavorite = true;

                name = favoritesCursor.getString(favoritesCursor.getColumnIndex("name"));
                location = new YelpLocation(favoritesCursor.getString(favoritesCursor.getColumnIndex("address")));
                displayPhone = favoritesCursor.getString(favoritesCursor.getColumnIndex("phone"));
                price = favoritesCursor.getString(favoritesCursor.getColumnIndex("price"));
                rating = favoritesCursor.getFloat(favoritesCursor.getColumnIndex("rating"));
                customCategory = favoritesCursor.getString(favoritesCursor.getColumnIndex("category"));
                imageUrl = favoritesCursor.getString(favoritesCursor.getColumnIndex("image_url"));
          }

        /**
         * translate category names to readable strings by overriding toString() with declarative logic
         * @return String representation of category
         */
        @NonNull
        public String categoryToString() {
            StringBuilder sb = new StringBuilder();
            sb.append("• ");
            for (YelpCategory category : categories) {
                sb.append(category.title).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            return sb.toString();
        }

        /**
         * yelp categories obj
         */
        public static class YelpCategory {
            @SerializedName("title")
            public String title;
        }

        /**
         * yelp location obj
         */
        public static class YelpLocation {
            @SerializedName("address1")
            public String address1;
            @SerializedName("city")
            public String city;
            @SerializedName("state")
            public String state;
            @SerializedName("zip_code")
            public String zipCode;
            @SerializedName("country")
            public String country;
            @SerializedName("State")
            public String State;
            @SerializedName("display_address")
            public ArrayList<String> displayAddress;

            public String customAddress; // custom address for this business for favorites

            /**
             * constructor for YelpLocation class. only called when creating a new favourite
             * @param address address of business
             */
            public YelpLocation(String address) {
                customAddress = address;
            }

            /**
             * translate address to readable strings by overriding toString() with declarative logic
              * @return String representation of address
             */
            @NonNull
            @Override
            public String toString() {
                return address1 +
                        ", " +
                        city +
                        ", " +
                        state +
                        " " +
                        zipCode +
                        ", " +
                        country;
            }
        }
    }
}
