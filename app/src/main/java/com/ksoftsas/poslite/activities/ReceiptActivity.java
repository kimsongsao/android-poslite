package com.ksoftsas.poslite.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.adapters.ReceiptDetailAdapter;
import com.ksoftsas.poslite.adapters.ReceiptsAdapter;
import com.ksoftsas.poslite.helpers.MyDividerItemDecoration;
import com.ksoftsas.poslite.models.Category;
import com.ksoftsas.poslite.models.DataSpinner;
import com.ksoftsas.poslite.models.Item;
import com.ksoftsas.poslite.models.SalesHeader;
import com.ksoftsas.poslite.models.SalesLine;
import com.ksoftsas.poslite.services.AppServices;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class ReceiptActivity extends AppCompatActivity {
    private String code= "", customer = "", total= "";
    AppServices services;
    RecyclerView recyclerView;
    List<SalesLine> list;
    ReceiptDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        services = new AppServices();
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        code = data.getStringExtra("id");
        customer = data.getStringExtra("customer");
        total = data.getStringExtra("total");
        getSupportActionBar().setTitle("Receipt #" + code);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView txtTotal = findViewById(R.id.txtTotal);
        txtTotal.setText("USD " + total);
        TextView lblCustomer = findViewById(R.id.lblCustomer);
        lblCustomer.setText(lblCustomer.getText() + customer);

        recyclerView = findViewById(R.id.recyclerView);
        fetchItems();
    }
    private void fetchItems() {
        list = new ArrayList<>();
        SalesLine item = new SalesLine();
        list = item.getSalesItemsById(Integer.parseInt(code));
        adapter = new ReceiptDetailAdapter(getApplicationContext(),list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
