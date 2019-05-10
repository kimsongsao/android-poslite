package com.ksoftsas.poslite.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.ksoftsas.poslite.helpers.DatabaseManager;
import com.ksoftsas.poslite.services.AppServices;

public class User {
    public static final String TABLE_NAME = "user";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USERNAME + " TEXT,"
                    + COLUMN_EMAIL + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT,"
                    + COLUMN_FIRST_NAME + " TEXT,"
                    + COLUMN_LAST_NAME + " TEXT,"
                    + COLUMN_GENDER + " TEXT,"
                    + COLUMN_IMAGE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    int id;
    String username;
    String email;
    String password;
    String first_name;
    String last_name;
    String gender;
    String image;
    private AppServices services;
    public User() {
        services = new AppServices();
    }

    public User(int id, String username, String email, String password, String first_name, String last_name, String gender, String image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.image = image;
    }
    public int getUserId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int insert(User user) {
        int courseId = -1;
        try{
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            ContentValues values = new ContentValues();
            values.put(User.COLUMN_USERNAME, user.getUserName());
            values.put(User.COLUMN_EMAIL, user.getEmail());
            values.put(User.COLUMN_FIRST_NAME, user.getFirstName());
            values.put(User.COLUMN_LAST_NAME, user.getLastName());
            values.put(User.COLUMN_GENDER, user.getGender());
            values.put(User.COLUMN_IMAGE, user.getImage());
            values.put(User.COLUMN_PASSWORD, user.getPassword());
            // Inserting Row
            courseId=(int)db.insert(User.TABLE_NAME, null, values);
            DatabaseManager.getInstance().closeDatabase();
        }
        catch (SQLiteException ex){
            courseId = -1;
        }
        return courseId;
    }
    public User getUserByEmailOrUsername(String username){
        User user = new User();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        String selectQuery =  " SELECT " + User.COLUMN_ID
                + "," + User.COLUMN_EMAIL
                + "," + User.COLUMN_USERNAME
                + "," + User.COLUMN_PASSWORD
                + " FROM " + User.TABLE_NAME
                + " WHERE (" + User.COLUMN_EMAIL + " = " + services.SqlStr(username)
                + " OR " + User.COLUMN_USERNAME + " = " + services.SqlStr(username) + ")"
                ;

        Log.i("SQL", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToNext()) {
            user.setUserName(cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)));
            user.setId(cursor.getInt(cursor.getColumnIndex(User.COLUMN_ID)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(User.COLUMN_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD)));
            user.setUserName(cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)));
            user.setUserName(cursor.getString(cursor.getColumnIndex(User.COLUMN_USERNAME)));
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        return user;
    }
}
