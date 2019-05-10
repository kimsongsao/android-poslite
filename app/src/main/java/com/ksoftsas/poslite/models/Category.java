package com.ksoftsas.poslite.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ksoftsas.poslite.helpers.DatabaseManager;
import com.ksoftsas.poslite.services.AppServices;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public static final String TABLE_NAME = "item_category";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CODE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    int id;
    String code;
    String description;

    private AppServices services;
    public Category() {
        services = new AppServices();
    }

    public Category(int id, String code, String description) {
        this.id = id;
        this.code = code;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int insert(Category category) {
        int courseId = -1;
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Category.COLUMN_CODE, category.getCode());
            values.put(Category.COLUMN_DESCRIPTION, category.getDescription());
            // Inserting Row
            courseId=(int)db.insert(Category.TABLE_NAME, null, values);
            DatabaseManager.getInstance().closeDatabase();
        }
        catch (SQLiteException ex){
            courseId = -1;
        }
        return courseId;
    }
    public int update(Category category) {
        int courseId = -1;
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Category.COLUMN_DESCRIPTION, category.getDescription());
            courseId = db.update(TABLE_NAME, values, "code = ? ", new String[]{(category.getCode())});
            DatabaseManager.getInstance().closeDatabase();
        }
        catch (SQLiteException ex){
            courseId = -1;
        }
        return courseId;
    }
    public boolean checkExisting(String code){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT " + Category.COLUMN_ID
                + "," + Category.COLUMN_CODE
                + "," + Category.COLUMN_DESCRIPTION
                + " FROM " + Category.TABLE_NAME
                + " WHERE " + Category.COLUMN_CODE + " = " + services.SqlStr(code);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }
    public Category getCategoryByCode(String code){
        Category category = new Category();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT " + Category.COLUMN_ID
                + "," + Category.COLUMN_CODE
                + "," + Category.COLUMN_DESCRIPTION
                + " FROM " + Category.TABLE_NAME
                + " WHERE " + Category.COLUMN_CODE + " = " + services.SqlStr(code);

        Log.i("SQL", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            category.setCode(cursor.getString(cursor.getColumnIndex(Category.COLUMN_CODE)));
            category.setDescription(cursor.getString(cursor.getColumnIndex(Category.COLUMN_DESCRIPTION)));
            category.setId(cursor.getInt(cursor.getColumnIndex(Category.COLUMN_ID)));
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return category;
    }
    public ArrayList<Category> getCategories(){
        Category category = new Category();
        ArrayList<Category> categories = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT " + Category.COLUMN_ID
                + "," + Category.COLUMN_CODE
                + "," + Category.COLUMN_DESCRIPTION
                + " FROM " + Category.TABLE_NAME;

        Log.i("SQL", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                category= new Category();
                category.setCode(cursor.getString(cursor.getColumnIndex(Category.COLUMN_CODE)));
                category.setDescription(cursor.getString(cursor.getColumnIndex(Category.COLUMN_DESCRIPTION)));
                category.setId(cursor.getInt(cursor.getColumnIndex(Category.COLUMN_ID)));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return categories;
    }
    public int delete(String code){
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.delete(Category.TABLE_NAME,COLUMN_CODE + "='" + code + "'",null);
            DatabaseManager.getInstance().closeDatabase();
        }
        catch (SQLiteException ex){
            return -1;
        }
        return 1;
    }
    public int getCountItems(String code){
        int items = 0;
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String selectQuery =  " SELECT * FROM item where item_category_code = " + services.SqlStr(code);
            Log.i("SQL", selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            items = cursor.getCount();
        }
        catch(SQLiteException ex) {
            items = 0;
        }

        return items;
    }
}
