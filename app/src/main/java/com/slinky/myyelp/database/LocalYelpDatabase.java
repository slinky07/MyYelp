package com.slinky.myyelp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.slinky.myyelp.yelp_api.YelpResponse;

public class LocalYelpDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "my_yelp_db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "favorite_yelp_restaurants";
    public static final String TABLE_PK = "favorite_ID";

    private static LocalYelpDatabase instance;

    private LocalYelpDatabase (Context context ,SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    public static LocalYelpDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new LocalYelpDatabase(context, null);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + TABLE_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "address TEXT, " +
                "phone TEXT, " +
                "price TEXT, " +
                "rating TEXT, " +
                "category TEXT, " +
                "image_url TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //do nothing for now. unimplemented.
    }

    /**
     * get all restaurants from favorite_yelp_restaurants table
     * @return Cursor of all restaurants
     */
    public Cursor getAll() {
        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, null);
    }

    /**
     * drop database table if it exists
     */
    public void drop() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

}
