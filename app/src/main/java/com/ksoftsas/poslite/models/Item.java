package com.ksoftsas.poslite.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ksoftsas.poslite.helpers.DatabaseManager;
import com.ksoftsas.poslite.services.AppServices;

import java.util.ArrayList;

public class Item {
    public static final String TABLE_NAME = "item";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_UNIT_PRICE = "unit_price";
    public static final String COLUMN_ITEM_CATEGORY_CODE = "item_category_code";
    public static final String COLUMN_UOM = "unit_of_measure_code";
    public static final String COLUMN_TRACK_STOCK = "tracking_stock";
    public static final String COLUMN_STOCK_QTY = "stock_qty";
    public static final String COLUMN_IMAGE= "image";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CODE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_UNIT_PRICE + " REAL,"
                    + COLUMN_ITEM_CATEGORY_CODE + " TEXT,"
                    + COLUMN_UOM + " TEXT,"
                    + COLUMN_TRACK_STOCK + " INTEGER,"
                    + COLUMN_STOCK_QTY + " REAL,"
                    + COLUMN_IMAGE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    int id;
    String code;
    String description;
    float unit_price;
    String item_category_code;
    String unit_of_measure_code;
    int track_stock;
    float stock_qty;
    String image;

    private AppServices services;
    public Item() {
        services = new AppServices();
    }

    public Item(int id, String code, String description, float unit_price) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.unit_price = unit_price;
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

    public float getUnitPrice() {
        return unit_price;
    }

    public void setUnitPrice(float unit_price) {
        this.unit_price = unit_price;
    }

    public String getItemCategoryCode() {
        return item_category_code;
    }

    public void setItemCategoryCode(String item_category_code) {
        this.item_category_code = item_category_code;
    }

    public String getUnitOfMeasureCode() {
        return unit_of_measure_code;
    }

    public void setUnitOfMeasureCode(String unit_of_measure_code) {
        this.unit_of_measure_code = unit_of_measure_code;
    }

    public int getTrackStock() {
        return track_stock;
    }

    public void setTrackStock(int track_stock) {
        this.track_stock = track_stock;
    }

    public float getStockQty() {
        return stock_qty;
    }

    public void setStockQty(float stock_qty) {
        this.stock_qty = stock_qty;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int insert(Item item) {
        int courseId = -1;
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Item.COLUMN_CODE, item.getCode());
            values.put(Item.COLUMN_DESCRIPTION, item.getDescription());
            values.put(Item.COLUMN_UNIT_PRICE, item.getUnitPrice());
            values.put(Item.COLUMN_UOM, item.getUnitOfMeasureCode());
            values.put(Item.COLUMN_ITEM_CATEGORY_CODE, item.getItemCategoryCode());
            values.put(Item.COLUMN_STOCK_QTY, item.getStockQty());
            values.put(Item.COLUMN_TRACK_STOCK, item.getTrackStock());
            values.put(Item.COLUMN_IMAGE, item.getImage());
            // Inserting Row
            courseId=(int)db.insert(Item.TABLE_NAME, null, values);
            DatabaseManager.getInstance().closeDatabase();
        }
        catch (SQLiteException ex){
            courseId = -1;
        }
        return courseId;
    }
    public int update(Item item) {
        int courseId = -1;
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(Item.COLUMN_DESCRIPTION, item.getDescription());
            values.put(Item.COLUMN_UNIT_PRICE, item.getUnitPrice());
            values.put(Item.COLUMN_UOM, item.getUnitOfMeasureCode());
            values.put(Item.COLUMN_ITEM_CATEGORY_CODE, item.getItemCategoryCode());
            values.put(Item.COLUMN_STOCK_QTY, item.getStockQty());
            values.put(Item.COLUMN_TRACK_STOCK, item.getTrackStock());
            values.put(Item.COLUMN_IMAGE, item.getImage());
            courseId = db.update(TABLE_NAME, values, "code = ? ", new String[]{(item.getCode())});
            DatabaseManager.getInstance().closeDatabase();
        }
        catch (SQLiteException ex){
            courseId = -1;
        }
        return courseId;
    }
    public boolean checkExisting(String code){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT " + Item.COLUMN_ID
                + " FROM " + Item.TABLE_NAME
                + " WHERE " + Item.COLUMN_CODE + " = " + services.SqlStr(code);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }
    public Item getItemByCode(String code){
        Item item = null;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT " + Item.COLUMN_ID
                + "," + Item.COLUMN_CODE
                + "," + Item.COLUMN_DESCRIPTION
                + "," + Item.COLUMN_UNIT_PRICE
                + "," + Item.COLUMN_ITEM_CATEGORY_CODE
                + "," + Item.COLUMN_UOM
                + "," + Item.COLUMN_TRACK_STOCK
                + "," + Item.COLUMN_STOCK_QTY
                + "," + Item.COLUMN_IMAGE
                + " FROM " + Item.TABLE_NAME
                + " WHERE upper(" + Item.COLUMN_CODE + ") = " + services.SqlStr(code.toUpperCase());

        Log.i("SQL", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            item = new Item();
            item.setCode(cursor.getString(cursor.getColumnIndex(Item.COLUMN_CODE)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(Item.COLUMN_DESCRIPTION)));
            item.setUnitPrice(cursor.getFloat(cursor.getColumnIndex(Item.COLUMN_UNIT_PRICE)));
            item.setItemCategoryCode(cursor.getString(cursor.getColumnIndex(Item.COLUMN_ITEM_CATEGORY_CODE)));
            item.setUnitOfMeasureCode(cursor.getString(cursor.getColumnIndex(Item.COLUMN_UOM)));
            item.setTrackStock(cursor.getInt(cursor.getColumnIndex(Item.COLUMN_TRACK_STOCK)));
            item.setStockQty(cursor.getFloat(cursor.getColumnIndex(Item.COLUMN_STOCK_QTY)));
            item.setImage(cursor.getString(cursor.getColumnIndex(Item.COLUMN_IMAGE)));
            item.setId(cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)));
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return item;
    }
    public ArrayList<Item> getItems(){
        Item item = new Item();
        ArrayList<Item> items = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT " + Item.COLUMN_ID
                + "," + Item.COLUMN_CODE
                + "," + Item.COLUMN_DESCRIPTION
                + "," + Item.COLUMN_UNIT_PRICE
                + "," + Item.COLUMN_ITEM_CATEGORY_CODE
                + "," + Item.COLUMN_UOM
                + "," + Item.COLUMN_TRACK_STOCK
                + "," + Item.COLUMN_STOCK_QTY
                + "," + Item.COLUMN_IMAGE
                + " FROM " + Item.TABLE_NAME;

        Log.i("SQL", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                item= new Item();
                item.setCode(cursor.getString(cursor.getColumnIndex(Item.COLUMN_CODE)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(Item.COLUMN_DESCRIPTION)));
                item.setUnitPrice(cursor.getFloat(cursor.getColumnIndex(Item.COLUMN_UNIT_PRICE)));
                item.setImage(cursor.getString(cursor.getColumnIndex(Item.COLUMN_IMAGE)));
                item.setId(cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return items;
    }
    public int delete(String code){
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.delete(Item.TABLE_NAME,COLUMN_CODE + "='" + code + "'",null);
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
