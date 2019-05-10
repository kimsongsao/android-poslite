package com.ksoftsas.poslite.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ksoftsas.poslite.helpers.DatabaseManager;
import com.ksoftsas.poslite.services.AppServices;

import java.util.ArrayList;

public class SalesLine {
    public static final String TABLE_NAME = "sales_line";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DOCUMENT_ID = "document_id";
    public static final String COLUMN_ITEM_CODE = "item_code";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_UNIT_PRICE = "unit_price";
    public static final String COLUMN_QUANTITY= "quantity";
    public static final String COLUMN_DISCOUNT_PERCENTAGE= "discount_percentage";
    public static final String COLUMN_SUB_TOTAL = "sub_total";
    public static final String COLUMN_GRAND_TOTAL = "grand_total";
    public static final String COLUMN_ITEM_IMAGE = "item_image";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DOCUMENT_ID + " INTEGER,"
                    + COLUMN_ITEM_CODE + " TEXT,"
                    + COLUMN_DESCRIPTION + " TEXT,"
                    + COLUMN_UNIT_PRICE + " REAL,"
                    + COLUMN_QUANTITY + " REAL,"
                    + COLUMN_DISCOUNT_PERCENTAGE + " REAL,"
                    + COLUMN_SUB_TOTAL + " REAL,"
                    + COLUMN_GRAND_TOTAL + " REAL,"
                    + COLUMN_ITEM_IMAGE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    private int id;
    private int document_id;
    private String item_code;
    private String description;
    private float unit_price;
    private float quantity;
    private float discount_percentage;
    private float sub_total;
    private float grand_total;
    private String item_image;
    private AppServices services;

    public SalesLine(){
        services = new AppServices();
    }
    public SalesLine(int document_id, String item_code, String description, float unit_price, float quantity, float discount_percentage, float sub_total, float grand_total) {
        this.document_id = document_id;
        this.item_code = item_code;
        this.description = description;
        this.unit_price = unit_price;
        this.quantity = quantity;
        this.discount_percentage = discount_percentage;
        this.sub_total = sub_total;
        this.grand_total = grand_total;
        services = new AppServices();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumentId() {
        return document_id;
    }

    public void setDocumentId(int document_id) {
        this.document_id = document_id;
    }

    public String getItemCode() {
        return item_code;
    }

    public void setItemCode(String item_code) {
        this.item_code = item_code;
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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getDiscountPercentage() {
        return discount_percentage;
    }

    public void setDiscountPercentage(float discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public float getSub_total() {
        return sub_total;
    }

    public void setSubTotal(float sub_total) {
        this.sub_total = sub_total;
    }

    public float getGrandTotal() {
        return grand_total;
    }

    public void setGrandTotal(float grand_total) {
        this.grand_total = grand_total;
    }

    public String getItemImage() {
        return item_image;
    }

    public void setItemImage(String item_image) {
        this.item_image = item_image;
    }

    public ArrayList<SalesLine> getSalesItems(String status){
        SalesLine item;
        ArrayList<SalesLine> items = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT L." + SalesLine.COLUMN_ID
                + "," + SalesLine.COLUMN_DOCUMENT_ID
                + "," + SalesLine.COLUMN_DESCRIPTION
                + "," + SalesLine.COLUMN_UNIT_PRICE
                + "," + SalesLine.COLUMN_QUANTITY
                + "," + SalesLine.COLUMN_DISCOUNT_PERCENTAGE
                + "," + SalesLine.COLUMN_SUB_TOTAL
                + "," + SalesLine.COLUMN_GRAND_TOTAL
                + "," + SalesLine.COLUMN_ITEM_CODE
                + "," + SalesLine.COLUMN_ITEM_IMAGE
                + " FROM " + SalesLine.TABLE_NAME + " AS L"
                + " INNER JOIN " + SalesHeader.TABLE_NAME + " AS H ON H." + SalesHeader.COLUMN_ID
                + " = L." + SalesLine.COLUMN_DOCUMENT_ID + " WHERE UPPER(H." + SalesHeader.COLUMN_STATUS + ") = " + services.SqlStr(status.toUpperCase());

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                item= new SalesLine();
                item.setId(cursor.getInt(cursor.getColumnIndex(SalesLine.COLUMN_ID)));
                item.setDocumentId(cursor.getInt(cursor.getColumnIndex(SalesLine.COLUMN_DOCUMENT_ID)));
                item.setItemCode(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_ITEM_CODE)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_DESCRIPTION)));
                item.setUnitPrice(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_UNIT_PRICE)));
                item.setQuantity(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_QUANTITY)));
                item.setDiscountPercentage(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_DISCOUNT_PERCENTAGE)));
                item.setSubTotal(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_SUB_TOTAL)));
                item.setGrandTotal(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_GRAND_TOTAL)));
                item.setItemImage(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_ITEM_IMAGE)));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return items;
    }
    public ArrayList<SalesLine> getSalesItemsById(int id){
        SalesLine item;
        ArrayList<SalesLine> items = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT L." + SalesLine.COLUMN_ID
                + "," + SalesLine.COLUMN_DOCUMENT_ID
                + "," + SalesLine.COLUMN_DESCRIPTION
                + "," + SalesLine.COLUMN_UNIT_PRICE
                + "," + SalesLine.COLUMN_QUANTITY
                + "," + SalesLine.COLUMN_DISCOUNT_PERCENTAGE
                + "," + SalesLine.COLUMN_SUB_TOTAL
                + "," + SalesLine.COLUMN_GRAND_TOTAL
                + "," + SalesLine.COLUMN_ITEM_CODE
                + "," + SalesLine.COLUMN_ITEM_IMAGE
                + " FROM " + SalesLine.TABLE_NAME + " AS L"
                + " INNER JOIN " + SalesHeader.TABLE_NAME + " AS H ON H." + SalesHeader.COLUMN_ID
                + " = L." + SalesLine.COLUMN_DOCUMENT_ID
                + " WHERE UPPER(H." + SalesHeader.COLUMN_STATUS + ") = " + services.SqlStr("settled".toUpperCase())
                + " AND H." + SalesHeader.COLUMN_ID + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                item= new SalesLine();
                item.setId(cursor.getInt(cursor.getColumnIndex(SalesLine.COLUMN_ID)));
                item.setDocumentId(cursor.getInt(cursor.getColumnIndex(SalesLine.COLUMN_DOCUMENT_ID)));
                item.setItemCode(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_ITEM_CODE)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_DESCRIPTION)));
                item.setUnitPrice(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_UNIT_PRICE)));
                item.setQuantity(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_QUANTITY)));
                item.setDiscountPercentage(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_DISCOUNT_PERCENTAGE)));
                item.setSubTotal(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_SUB_TOTAL)));
                item.setGrandTotal(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_GRAND_TOTAL)));
                item.setItemImage(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_ITEM_IMAGE)));
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return items;
    }

    public static SalesLine getSalesById(int id){
        SalesLine item = null;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT L." + SalesLine.COLUMN_ID
                + "," + SalesLine.COLUMN_DOCUMENT_ID
                + "," + SalesLine.COLUMN_DESCRIPTION
                + "," + SalesLine.COLUMN_UNIT_PRICE
                + "," + SalesLine.COLUMN_QUANTITY
                + "," + SalesLine.COLUMN_DISCOUNT_PERCENTAGE
                + "," + SalesLine.COLUMN_SUB_TOTAL
                + "," + SalesLine.COLUMN_GRAND_TOTAL
                + "," + SalesLine.COLUMN_ITEM_CODE
                + "," + SalesLine.COLUMN_ITEM_IMAGE
                + " FROM " + SalesLine.TABLE_NAME + " AS L"
                + " INNER JOIN " + SalesHeader.TABLE_NAME + " AS H ON H." + SalesHeader.COLUMN_ID
                + " = L." + SalesLine.COLUMN_DOCUMENT_ID + " WHERE UPPER(H." + SalesHeader.COLUMN_ID + ") = " + id;
        Log.i("SQL",selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {

            item= new SalesLine();
            item.setId(cursor.getInt(cursor.getColumnIndex(SalesLine.COLUMN_ID)));
            item.setDocumentId(cursor.getInt(cursor.getColumnIndex(SalesLine.COLUMN_DOCUMENT_ID)));
            item.setItemCode(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_ITEM_CODE)));
            item.setDescription(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_DESCRIPTION)));
            item.setUnitPrice(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_UNIT_PRICE)));
            item.setQuantity(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_QUANTITY)));
            item.setDiscountPercentage(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_DISCOUNT_PERCENTAGE)));
            item.setSubTotal(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_SUB_TOTAL)));
            item.setGrandTotal(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_GRAND_TOTAL)));
            item.setItemImage(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_ITEM_IMAGE)));
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return item;
    }
    public SalesLine checkExisting(String item_code, float discount,long document_id){
        try{
            SalesLine line = new SalesLine();
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String selectQuery =  " SELECT sales_line." + SalesLine.COLUMN_ID
                    + " , " + SalesLine.COLUMN_QUANTITY
                    + " FROM " + SalesLine.TABLE_NAME
                    + " INNER JOIN " + SalesHeader.TABLE_NAME
                    + " ON sales_line." + SalesLine.COLUMN_DOCUMENT_ID
                    + " = sales_header." + SalesHeader.COLUMN_ID
                    + " WHERE " + SalesLine.COLUMN_ITEM_CODE + " = " + services.SqlStr(item_code)
                    + " AND " + SalesLine.COLUMN_DOCUMENT_ID + " = " + (document_id)
                    + " AND upper(" + SalesHeader.COLUMN_STATUS + ") = " + services.SqlStr("OPEN");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToNext()){
                line.setId(cursor.getInt(cursor.getColumnIndex(SalesLine.COLUMN_ID)));
                line.setQuantity(cursor.getFloat(cursor.getColumnIndex(SalesLine.COLUMN_QUANTITY)));
                DatabaseManager.getInstance().closeDatabase();
                cursor.close();
                return line;
            }
            else{
                cursor.close();
                DatabaseManager.getInstance().closeDatabase();
                return null;
            }

        }
        catch (SQLiteException ex){
            Log.i("myLog",ex.getMessage());
            return null;
        }

    }
}
