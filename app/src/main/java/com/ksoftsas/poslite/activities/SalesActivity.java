package com.ksoftsas.poslite.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.adapters.SalesItemAdapter;
import com.ksoftsas.poslite.helpers.DatabaseManager;
import com.ksoftsas.poslite.helpers.MyDividerItemDecoration;
import com.ksoftsas.poslite.helpers.RecyclerItemTouchHelper;
import com.ksoftsas.poslite.models.Item;
import com.ksoftsas.poslite.models.SalesHeader;
import com.ksoftsas.poslite.models.SalesLine;
import com.ksoftsas.poslite.services.AppServices;

import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    SalesItemAdapter adapter;
    List<SalesLine> list;
    EditText txtCode;
    TextView txtGrandTotal;
    RecyclerView recyclerView;
    private RelativeLayout mainLayout;
    private Class<?> mClss;
    Button btnScanCode, btnSettle;

    private static final int ZXING_CAMERA_PERMISSION = 1;
    int REQUEST_SCAN_BARCODE = 2;
    AppServices services;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        services = new AppServices();
        txtCode = findViewById(R.id.txtCode);
        txtCode.requestFocus();
        txtGrandTotal = findViewById(R.id.grandTotal);
        btnScanCode = findViewById(R.id.btnBarCode);
        btnSettle = findViewById(R.id.btnSubmit);
        mainLayout = findViewById(R.id.mainLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//         toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.sales);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        //fetching
        fetchSalesLine();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        btnScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivityWithResult(ScannerActivity.class);
            }
        });
        txtCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 try{
                     if ((actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) && txtCode.getText().toString().trim().length() > 0) {

                         addItemToList(txtCode.getText().toString());
                     }

                 }catch (Exception ex){

                 }
                 return false;
             }
         });
        txtCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                try{
                    if (keyCode == EditorInfo.IME_FLAG_NO_ENTER_ACTION && txtCode.getText().toString().trim().length() > 0) {
                        addItemToList(txtCode.getText().toString());
                    }
                }
                catch (Exception ex){

                }

                return false;
            }

        });
        btnSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size() <= 0){
                    Snackbar.make(mainLayout,"Nothing to settle!", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                try{
                    SalesHeader header;
                    String selectQuery =  " SELECT * FROM " + SalesHeader.TABLE_NAME
                            + " WHERE upper(" + SalesHeader.COLUMN_STATUS + ") = " + services.SqlStr("OPEN");
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if(cursor.moveToNext()){
                        if(cursor.getString(cursor.getColumnIndex("status")).equalsIgnoreCase("OPEN")){
                            ContentValues values = new ContentValues();
                            values.put(SalesHeader.COLUMN_ORDER_DATE, services.getDateNow());
                            values.put(SalesHeader.COLUMN_STATUS, "settled");
                            db.update(SalesHeader.TABLE_NAME,values," id = " + cursor.getInt(cursor.getColumnIndex("id")),null);

                        }
                        fetchSalesLine();
                    }
                }
                catch (Exception ex){
                    DatabaseManager.getInstance().closeDatabase();
                }
            }
        });
    }
    public void launchActivityWithResult(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivityForResult(intent, REQUEST_SCAN_BARCODE);
        }
    }
    private void fetchSalesLine(){
        list = new ArrayList<>();
        SalesLine item = new SalesLine();
        list = item.getSalesItems("OPEN");
        adapter = new SalesItemAdapter(this, list, txtGrandTotal);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 0));
        recyclerView.setAdapter(adapter);
        calculationTotal();
    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof SalesItemAdapter.MyViewHolder) {
            String name = list.get(viewHolder.getAdapterPosition()).getDescription();
            // backup of removed item for undo purpose
            final SalesLine item = list.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.execSQL("delete from sales_line where id = " + item.getId());
            DatabaseManager.getInstance().closeDatabase();
            adapter.removeItem(deletedIndex);
            calculationTotal();
            Snackbar snackbar = Snackbar.make(mainLayout, name + " removed from cart!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }
    public void calculationTotal(){
        float grandTotal = 0;
        for(SalesLine item: list){
            grandTotal += item.getGrandTotal();
        }
        txtGrandTotal.setText("Total : USD " + services.numberFormat(grandTotal,"amount"));
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.sales_menu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_search) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SCAN_BARCODE && resultCode == RESULT_OK){
            addItemToList(data.getStringExtra("barcode"));
        }
    }
    private void addItemToList(String barcode){
        try{
            Item item = new Item();
            item = item.getItemByCode(barcode);
            if(item == null){
                Snackbar snackbar = Snackbar.make(mainLayout, "Item not found!!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return;
            }
            long document_id = 0;
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            String selectQuery =  " SELECT " + SalesHeader.COLUMN_ID  + " FROM " + SalesHeader.TABLE_NAME + " WHERE upper(" + SalesHeader.COLUMN_STATUS + ") = " + services.SqlStr("OPEN");
            Log.i("SQL",selectQuery);
            Cursor cursor = db.rawQuery(selectQuery, null);
            if(cursor.moveToNext()){
                document_id = cursor.getInt(cursor.getColumnIndexOrThrow(SalesHeader.COLUMN_ID));
//                Toast.makeText(getApplicationContext(),document_id + "hhh",Toast.LENGTH_LONG).show();
            }
//            Toast.makeText(getApplicationContext(),document_id + ":1",Toast.LENGTH_LONG).show();
            if(document_id == 0){
                ContentValues values = new ContentValues();
                values.put(SalesHeader.COLUMN_DOCUMENT_TYPE, "Invoice");
                values.put(SalesHeader.COLUMN_CUSTOMER_NAME, "General");
                values.put(SalesHeader.COLUMN_STATUS, "Open");
                document_id = db.insert(SalesHeader.TABLE_NAME, null, values);
                // Inserting Row
                if(document_id == -1){
                    Snackbar snackbar = Snackbar.make(mainLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
            }
            ContentValues values = new ContentValues();
            values.put(SalesLine.COLUMN_DOCUMENT_ID, document_id);
            values.put(SalesLine.COLUMN_ITEM_CODE, barcode);
            values.put(SalesLine.COLUMN_DESCRIPTION, item.getDescription());
            values.put(SalesLine.COLUMN_UNIT_PRICE, item.getUnitPrice());
            values.put(SalesLine.COLUMN_DISCOUNT_PERCENTAGE, 0);
            values.put(SalesLine.COLUMN_ITEM_IMAGE,item.getImage());
            Log.i("SQL","Saving...");
            SalesLine line = new SalesLine();
            line = line.checkExisting(barcode,0, document_id);
            Log.i("SQL","Saving...1");
            if(line == null){
                Log.i("SQL","Save New");
                Log.i("SQL",document_id + "");
                values.put(SalesLine.COLUMN_QUANTITY, 1);
                values.put(SalesLine.COLUMN_SUB_TOTAL,item.getUnitPrice());
                values.put(SalesLine.COLUMN_GRAND_TOTAL,item.getUnitPrice());
                long i = db.insert(SalesLine.TABLE_NAME, null, values);
                // Inserting Row
                if(i == -1){
                    Snackbar snackbar = Snackbar.make(mainLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
            }
            else{
                Log.i("SQL","Update");
                Log.i("SQL",document_id + "");
                values.put(SalesLine.COLUMN_QUANTITY, line.getQuantity() + 1);
                values.put(SalesLine.COLUMN_SUB_TOTAL, (line.getQuantity() + 1) * item.getUnitPrice());
                values.put(SalesLine.COLUMN_GRAND_TOTAL, (line.getQuantity() + 1) * item.getUnitPrice());
                long i = db.update(SalesLine.TABLE_NAME, values, "id = " + line.getId(), null);
                // Inserting Row
                if(i == -1){
                    Snackbar snackbar = Snackbar.make(mainLayout, "Something went wrong!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
            }
            DatabaseManager.getInstance().closeDatabase();
            adapter.notifyDataSetChanged();
            fetchSalesLine();
            txtCode.setText("");
            txtCode.requestFocus();
        }
        catch (SQLiteException ex){
            Snackbar snackbar = Snackbar.make(mainLayout, ex.getMessage(), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

}
