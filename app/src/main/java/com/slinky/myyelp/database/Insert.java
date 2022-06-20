package com.slinky.myyelp.database;

import static com.slinky.myyelp.database.LocalYelpDatabase.TABLE_NAME;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.slinky.myyelp.yelp_api.YelpResponse;

public class Insert implements DatabaseLogic {
    LocalYelpDatabase database;

    public Insert(LocalYelpDatabase database) {
        this.database = database;
    }

    @Override
    public void action(YelpResponse.YelpBusiness business) {
        Log.d(Insert.class.getSimpleName(), "insert: " + business.name);

        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", business.name);
        values.put("address", business.location.toString());
        values.put("phone", business.displayPhone);
        values.put("price", business.price);
        values.put("rating", business.rating);
        values.put("category", business.categoryToString());
        values.put("image_url", business.imageUrl);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}
