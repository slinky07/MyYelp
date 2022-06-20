package com.slinky.myyelp.database;

import com.slinky.myyelp.yelp_api.YelpResponse;

public interface DatabaseLogic {
    /**
     * Action to be performed on the database
     * @param business the business to be inserted or deleted
     */
    void action(YelpResponse.YelpBusiness business);
}
