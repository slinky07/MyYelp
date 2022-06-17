package com.slinky.myyelp.yelp_api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * YelpResponse is  class that represents the JSON response from the aYelp API.
 */
public class YelpResponse {

    @SerializedName("businesses")
    public ArrayList<YelpBusiness> businesses;

    @SerializedName("total") // at bottom of JSON response
    public int total;

    class YelpBusiness {

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

        class YelpLocation {
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
        }

        @SerializedName("categories")
        public ArrayList<YelpCategory> categories;

        class YelpCategory {
            @SerializedName("title")
            public String title;
        }

        @SerializedName("display_phone")
        public String displayPhone;
    }

}
