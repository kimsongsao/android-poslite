package com.ksoftsas.poslite.app;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;
import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.helpers.DatabaseHelper;
import com.ksoftsas.poslite.helpers.DatabaseManager;

public class  App extends Application {
    private static Context context;
    private static DatabaseHelper dbHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        context = this.getApplicationContext();
        dbHelper = new DatabaseHelper();
        DatabaseManager.initializeInstance(dbHelper);




    }
    public static Context getContext(){
        return context;
    }

}
