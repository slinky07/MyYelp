package com.slinky.myyelp.yelp_api;

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

      public class YelpBusiness {

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

        // translate category names to readable strings by overriding toString() with declarative logic

        @NonNull
        public String categoryToString() {
            StringBuilder sb = new StringBuilder();
            sb.append("• ");
            for (YelpCategory category : categories) {
                sb.append(category.title).append(", ");
            }
            //delete last comma
            sb.delete(sb.length() - 2, sb.length());
            return sb.toString();
        }
        public class YelpCategory {
            @SerializedName("title")
            public String title;
        }

        public class YelpLocation {
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

            // translate display_address to a single string by overwriting toString()
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

    @NonNull
    @Override
    public String toString() {
        return "YelpResponse{" +
                " [...]" +
                ", total=" + total +
                '}';
    }

}
