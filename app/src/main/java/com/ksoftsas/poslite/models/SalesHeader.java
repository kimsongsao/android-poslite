package com.ksoftsas.poslite.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ksoftsas.poslite.helpers.DatabaseManager;
import com.ksoftsas.poslite.services.AppServices;

import java.util.ArrayList;

public class SalesHeader {
    public static final String TABLE_NAME = "sales_header";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DOCUMENT_TYPE = "document_type";
    public static final String COLUMN_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_STATUS = "status"; // open, settled
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_DOCUMENT_TYPE + " TEXT,"
                    + COLUMN_CUSTOMER_NAME + " TEXT,"
                    + COLUMN_ORDER_DATE + " TEXT,"
                    + COLUMN_STATUS + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    int id;
    String document_type;
    String customer_name;
    String order_date;
    String status;
    String timestamp;
    private AppServices services;
    public SalesHeader() {
        services = new AppServices();
    }

    public SalesHeader(int id, String document_type, String customer_name, String order_date, String status) {
        this.id = id;
        this.document_type = document_type;
        this.customer_name = customer_name;
        this.order_date = order_date;
        this.status = status;
        services = new AppServices();
    }

    public String getDocumentType() {
        return document_type;
    }

    public void setDocumentType(String document_type) {
        this.document_type = document_type;
    }

    public String getCustomerName() {
        return customer_name;
    }

    public void setCustomerName(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getOrderDate() {
        return order_date;
    }

    public void setOrderDate(String order_date) {
        this.order_date = order_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getReceiptTotal(){
        float total = 0;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String sql = "select sum(grand_total) as total from sales_line where document_id = " + this.getId();
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToNext()) {
            total = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        DatabaseManager.getInstance().closeDatabase();
        return total;
    }
    public ArrayList<SalesHeader> getReceipts(String status){
        SalesHeader item;
        ArrayList<SalesHeader> items = new ArrayList<>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT " + SalesHeader.COLUMN_ID
                + "," + SalesHeader.COLUMN_DOCUMENT_TYPE
                + "," + SalesHeader.COLUMN_CUSTOMER_NAME
                + "," + SalesHeader.COLUMN_ORDER_DATE
                + "," + SalesHeader.COLUMN_TIMESTAMP
                + "," + SalesHeader.COLUMN_STATUS
                + " FROM " + SalesHeader.TABLE_NAME
                + " WHERE UPPER(" + SalesHeader.COLUMN_STATUS + ") = " + services.SqlStr(status.toUpperCase());
//                + " ORDER BY " + SalesHeader.COLUMN_TIMESTAMP + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                item= new SalesHeader();
                item.setId(cursor.getInt(cursor.getColumnIndex(SalesHeader.COLUMN_ID)));
                item.setDocumentType(cursor.getString(cursor.getColumnIndex(SalesHeader.COLUMN_DOCUMENT_TYPE)));
                item.setCustomerName(cursor.getString(cursor.getColumnIndex(SalesHeader.COLUMN_CUSTOMER_NAME)));
                item.setOrderDate(services.getDateShow(cursor.getString(cursor.getColumnIndex(SalesHeader.COLUMN_ORDER_DATE))));
                item.setTimestamp(services.getDateTimeShow(cursor.getString(cursor.getColumnIndex(SalesLine.COLUMN_TIMESTAMP))));
                items.add(item);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return items;
    }
}
