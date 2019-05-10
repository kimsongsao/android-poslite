package com.ksoftsas.poslite.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.adapters.RecyclerCardViewAdapter;
import com.ksoftsas.poslite.helpers.AutoFitGridLayoutManager;
import com.ksoftsas.poslite.helpers.SessionManager;
import com.ksoftsas.poslite.models.DataModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerCardViewAdapter.ItemListener {

    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<DataModel> arrayList;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        arrayList.add(new DataModel("Sales", R.drawable.ic_sales, "#ffffff"));
        arrayList.add(new DataModel("Receipts", R.drawable.ic_receipts, "#ffffff"));
        arrayList.add(new DataModel("Items", R.drawable.ic_items, "#ffffff"));
        arrayList.add(new DataModel("Categories", R.drawable.ic_category, "#ffffff"));
//        arrayList.add(new DataModel("Item 5", R.drawable.three_d, "#F94336"));
//        arrayList.add(new DataModel("Item 6", R.drawable.terraria, "#0A9B88"));
        RecyclerCardViewAdapter adapter = new RecyclerCardViewAdapter(this, arrayList, this);
        recyclerView.setAdapter(adapter);


        /**
         AutoFitGridLayoutManager that auto fits the cells by the column width defined.
         **/

        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 500);
        recyclerView.setLayoutManager(layoutManager);


        View adContainer = findViewById(R.id.adMobView);

//        adView = new AdView(this);
//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId(getString(R.string.banner_home_footer));
//        ((RelativeLayout)adContainer).addView(adView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("6C62424781C013C129C4B234E7D62042")
//                .build();
//        adView.loadAd(adRequest);
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//            }
//
//            @Override
//            public void onAdClosed() {
//                Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//        });
//
//        adView.loadAd(adRequest);
    }

    @Override
    public void onItemClick(DataModel item) {
        Intent intent;
        switch (item.text){
            case "Categories":
                intent = new Intent(MainActivity.this,CategoriesActivity.class);
                startActivity(intent);
                break;
            case "Items":
                intent = new Intent(MainActivity.this,ItemsActivity.class);
                startActivity(intent);
                break;
            case "Sales":
                intent = new Intent(MainActivity.this,SalesActivity.class);
                startActivity(intent);
                break;
            case "Receipts":
                intent = new Intent(MainActivity.this,ReceiptsActivity.class);
                startActivity(intent);
                break;
        }
//        Toast.makeText(getApplicationContext(), item.text + " is clicked", Toast.LENGTH_SHORT).show();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_about) {
            showAbout();
            return true;
        } else if (id == R.id.action_sign_out)  {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage("Do you really want to logout?")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            SessionManager session = new SessionManager(getApplicationContext());
                            session.logoutUser();
                            finish();
                        }})
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }
    protected void showAbout() {
        View messageView = getLayoutInflater().inflate(R.layout.about, null, false);
//        TextView textView = (TextView) messageView.findViewById(R.id.about_credits);
//        int defaultColor = textView.getTextColors().getDefaultColor();
//        textView.setTextColor(defaultColor);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.app_name);
        builder.setView(messageView);
        builder.create();
        builder.show();
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
