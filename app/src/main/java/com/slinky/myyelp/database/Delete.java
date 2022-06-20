package com.slinky.myyelp.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.slinky.myyelp.yelp_api.YelpResponse;

public class Delete implements DatabaseLogic {
    LocalYelpDatabase database;

    public Delete(LocalYelpDatabase database) {
        this.database = database;
    }

    @Override
    public void action(YelpResponse.YelpBusiness business) {
        Log.d(Delete.class.getSimpleName(), "delete: " + business.name);

        SQLiteDatabase db = database.getWritableDatabase();

        db.delete(LocalYelpDatabase.TABLE_NAME, "name = ?", new String[] {String.valueOf(business.name)});
        db.close();
    }
}
