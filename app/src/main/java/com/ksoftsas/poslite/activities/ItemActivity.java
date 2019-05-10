package com.ksoftsas.poslite.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.models.Category;
import com.ksoftsas.poslite.models.DataSpinner;
import com.ksoftsas.poslite.models.Item;
import com.ksoftsas.poslite.services.AppServices;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager.LoaderCallbacks;


public class ItemActivity extends AppCompatActivity {
    private String TAG = "ADD";
    private String code= "";
    EditText txtCode, txtDescription,txtUnitPrice;
    Spinner spinCategory;
    ImageView imageView;
    AppServices services;
    int SDCARD_REQUEST = 1, CAMERA_REQUEST = 2;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    int REQUEST_SCAN_BARCODE = 3;
    private Class<?> mClss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        services = new AppServices();
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        TAG = data.getStringExtra("TAG");
        if(TAG.equalsIgnoreCase("ADD")){
            getSupportActionBar().setTitle(R.string.new_item);
        }
        else{
            code = data.getStringExtra("CODE");
            getSupportActionBar().setTitle(R.string.edit_item);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        Button btnSaveNew = findViewById(R.id.btnSaveNew);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnChoose = findViewById(R.id.btnChoose);
        Button btnScanCode = findViewById(R.id.btnBarCode);
        imageView = findViewById(R.id.imageView);
        txtCode = findViewById(R.id.txtCode);
        txtDescription = findViewById(R.id.txtDescription);
        txtUnitPrice = findViewById(R.id.txtUnitPrice);
        spinCategory = findViewById(R.id.spinCategory);
        fetchItemCategory();
        if(!TAG.equalsIgnoreCase("ADD")){
            showInformation();
        }
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Log.i("photo", "" + intent);
                startActivityForResult(intent, SDCARD_REQUEST);

            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        btnScanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivityWithResult(ScannerActivity.class);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveOrUpdate();

                setResult(RESULT_OK);
                finish();
            }
        });
        btnSaveNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrUpdate();
                txtCode.setText("");
                txtDescription.setText("");
                txtCode.requestFocus();
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
    public void saveOrUpdate(){
        if(TextUtils.isEmpty(txtCode.getText()) && TAG.equalsIgnoreCase("ADD")){
            Toast.makeText(ItemActivity.this,"Code cannot empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(txtDescription.getText())){
            Toast.makeText(ItemActivity.this,"Description cannot empty!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(txtUnitPrice.getText())){
            Toast.makeText(ItemActivity.this,"Price cannot empty!",Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();
        String encodedImage = "";
        if(bitmap == null){
            encodedImage = "";
        }else{
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);
            byte[] byteArray = stream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        Item item = new Item();
        item.setDescription(txtDescription.getText().toString());
        item.setUnitPrice(services.convertToFloat(txtUnitPrice.getText().toString()));
        item.setItemCategoryCode(((DataSpinner) spinCategory.getSelectedItem()).getValue().toString());
        item.setImage(encodedImage);
        int i = -1;
        if(TAG.equalsIgnoreCase("ADD")){
            item.setCode(txtCode.getText().toString());
            if(item.checkExisting(txtCode.getText().toString())){
                Toast.makeText(ItemActivity.this,"Code already exist!",Toast.LENGTH_SHORT).show();
                return;
            }

            i  = item.insert(item);
        }else{
            item.setCode(txtCode.getText().toString());
            i  = item.update(item);
        }
        if(i == -1){
            Toast.makeText(ItemActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void fetchItemCategory() {
        Category category = new Category();
        List<Category> categories = category.getCategories();
        ArrayList<DataSpinner> labels = new ArrayList<>();
        labels.add(new DataSpinner("","Select one category"));
        for (Category cate :categories) {
            labels.add(new DataSpinner(cate.getCode(), cate.getDescription()));
        }
        ArrayAdapter<DataSpinner> dataAdapter = new ArrayAdapter<DataSpinner>(this,android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCategory.setAdapter(dataAdapter);
    }
    private void showInformation() {
        Item item = new Item();
        item = item.getItemByCode(code);
        txtCode.setText(item.getCode());
        txtDescription.setText(item.getDescription());
        txtUnitPrice.setText("" + item.getUnitPrice());
        imageView.setImageBitmap(services.imageFromBase64(item.getImage()));
        txtDescription.requestFocus();
        txtCode.setInputType(InputType.TYPE_NULL);
        spinCategory.setSelection(services.getSpinnerIndex(spinCategory,item.getItemCategoryCode(),"item_category","description","code"));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
        else if (requestCode == SDCARD_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo;
            try {
                Uri targetUri = data.getData();
                photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imageView.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else if (requestCode == REQUEST_SCAN_BARCODE && resultCode == RESULT_OK) {
           txtCode.setText(data.getStringExtra("barcode"));
           txtDescription.requestFocus();

        }
    }
}
