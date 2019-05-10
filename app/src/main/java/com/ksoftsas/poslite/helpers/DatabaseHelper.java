package com.ksoftsas.poslite.helpers;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ksoftsas.poslite.app.App;
import com.ksoftsas.poslite.models.Category;
import com.ksoftsas.poslite.models.Item;
import com.ksoftsas.poslite.models.SalesHeader;
import com.ksoftsas.poslite.models.SalesLine;
import com.ksoftsas.poslite.models.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "pos.db";
    private static final String TAG = DatabaseHelper.class.getSimpleName().toString();

    public DatabaseHelper() {
        super(App.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Category.CREATE_TABLE);
        db.execSQL(Item.CREATE_TABLE);
        db.execSQL(SalesHeader.CREATE_TABLE);
        db.execSQL(SalesLine.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
        // Drop table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + User.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Category.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Item.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SalesHeader.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SalesLine.CREATE_TABLE);
        onCreate(db);
    }
}