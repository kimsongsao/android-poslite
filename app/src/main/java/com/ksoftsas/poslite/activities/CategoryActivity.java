package com.ksoftsas.poslite.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.models.Category;


public class CategoryActivity extends AppCompatActivity {
    private String TAG = "ADD";
    private String code= "";
    EditText txtCode, txtDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        TAG = data.getStringExtra("TAG");
        if(TAG.equalsIgnoreCase("ADD")){
            getSupportActionBar().setTitle(R.string.new_category);
        }
        else{
            code = data.getStringExtra("CODE");
            getSupportActionBar().setTitle(R.string.edit_category);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        Button btnSaveNew = findViewById(R.id.btnSaveNew);
        txtCode = findViewById(R.id.txtCode);
        txtDescription = findViewById(R.id.txtDescription);

        if(!TAG.equalsIgnoreCase("ADD")){
            showInformation();
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TAG.equalsIgnoreCase("ADD")){
                    if(TextUtils.isEmpty(txtCode.getText())){
                        Toast.makeText(CategoryActivity.this,"Code cannot empty!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(txtDescription.getText())){
                        Toast.makeText(CategoryActivity.this,"Description cannot empty!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Category category = new Category();
                    if(category.checkExisting(txtCode.getText().toString())){
                        Toast.makeText(CategoryActivity.this,"Code already exist!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    category.setCode(txtCode.getText().toString());
                    category.setDescription(txtDescription.getText().toString());
                    int i  = category.insert(category);
                    if(i == -1){
                        Toast.makeText(CategoryActivity.this,"Save failed!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    Category category = new Category();
                    category.setCode(txtCode.getText().toString());
                    category.setDescription(txtDescription.getText().toString());
                    int i  = category.update(category);
                    if(i == -1){
                        Toast.makeText(CategoryActivity.this,"Save failed!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                setResult(RESULT_OK);
                finish();
            }
        });
        btnSaveNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TAG.equalsIgnoreCase("ADD")){
                    if(TextUtils.isEmpty(txtCode.getText())){
                        Toast.makeText(CategoryActivity.this,"Code cannot empty!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(TextUtils.isEmpty(txtDescription.getText())){
                        Toast.makeText(CategoryActivity.this,"Description cannot empty!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Category category = new Category();
                    if(category.checkExisting(txtCode.getText().toString())){
                        Toast.makeText(CategoryActivity.this,"Code already exist!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    category.setCode(txtCode.getText().toString());
                    category.setDescription(txtDescription.getText().toString());
                    int i  = category.insert(category);
                    if(i == -1){
                        Toast.makeText(CategoryActivity.this,"Save failed!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    Category category = new Category();
                    category.setCode(txtCode.getText().toString());
                    category.setDescription(txtDescription.getText().toString());
                    int i  = category.update(category);
                    if(i == -1){
                        Toast.makeText(CategoryActivity.this,"Save failed!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                txtCode.setText("");
                txtDescription.setText("");
                txtCode.requestFocus();
            }
        });

    }

    private void showInformation() {
        Category category = new Category();
        category = category.getCategoryByCode(code);
        txtCode.setText(category.getCode());
        txtDescription.setText(category.getDescription());
        txtDescription.requestFocus();
        txtCode.setInputType(InputType.TYPE_NULL);
    }
}
