package com.ksoftsas.poslite.services;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.Base64;
import android.util.Log;
import android.widget.Spinner;

import com.ksoftsas.poslite.helpers.DatabaseManager;
import com.ksoftsas.poslite.utils.AES;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

public class AppServices {

   public String encrypt(String sourceStr){
       String encrypted = "";
       try {
           encrypted = AES.encrypt(sourceStr);
           Log.d("TEST", "encrypted:" + encrypted);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return encrypted;
   }
    public String decrypt(String encrypted){
        String decrypted = "";
        try {
            decrypted = AES.decrypt(encrypted);
            Log.d("TEST", "decrypted:" + decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }
    public String SqlStr(String str){
        return "'" + str.replace("'", "''") + "'";
    }
    public float convertToFloat(String value){
        float f = 0;
        try {
            f = Float.parseFloat(value.replace(",", "").equals("")?"0":value.replace(",", ""));
        } catch (Exception e) {
            f = 0;
        }
        return f;
    }
    public Bitmap circleImage(String base64){

        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader (bitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth()/2, bitmap.getHeight()/2, bitmap.getWidth()/2, paint);
        return circleBitmap;
    }
    public Bitmap imageFromBase64(String base64){

        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }
    public final static String formatDate="yyyy-MM-dd";
    public final static String formatShowDate="dd-MMM-yyyy";
    public final static String formatShowDateTime="dd-MMM-yyyy HH:mm:ss a";
    public final static String formatDateTime="yyyy-MM-ddd HH:mm:ss";
    public String getDateNow() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(formatDate);
        String strDate = sdf.format(new Date());
        return (strDate);
    }
    public String getDateShow(String date) {
        java.text.SimpleDateFormat input = new java.text.SimpleDateFormat(formatDate);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(formatShowDate);
        String strDate = "";
        try {
            strDate = (sdf.format(input.parse(date)));    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (strDate);
    }
    public String getDateTimeShow(String date) {
        java.text.SimpleDateFormat input = new java.text.SimpleDateFormat(formatDateTime);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(formatShowDateTime);
        String strDate = "";
        try {
            strDate = (sdf.format(input.parse(date)));    // format output
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return (strDate);
    }
    public String numberFormat(float number,String format){
        String result = "";
        DecimalFormat formatter;
        String strFormat = "#,###,##0";
        switch (format){
            case "quantity":
                formatter = new DecimalFormat(strFormat);
                result = formatter.format(number);
                break;
            case "price":
                strFormat += ".00";
                formatter = new DecimalFormat(strFormat);
                result = formatter.format(number);
                break;
            case "amount":
                strFormat += ".00";
                formatter = new DecimalFormat(strFormat);
                result = formatter.format(number);
                break;
        }
        return result;
    }
    public int getSpinnerIndex(Spinner spinner, String Values, String tablename, String Field, String Condition){
        int index = 0;
        String desc = "";
        String STR="select " + Field + " from " + tablename + " where "+ Condition + " = " + SqlStr(Values);
        Log.i("",STR);
        desc = getDescription(STR);
        Log.i("",desc);
        for (int i=0;i < spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(desc)){
                index = i;
                break;
            }
        }
        return index;
    }
    public String getDescription(String Str){
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try {
            String result;
            Cursor C = db.rawQuery(Str, null);
            if(C.moveToFirst()){
                if(C.isNull(0))
                    result = "";
                else
                    result = C.getString(0);
                Log.e("result",result);
            }
            else
                result="";
            C.close();
            return result;
        } catch (SQLiteException e) {
            DatabaseManager.getInstance().closeDatabase();
            Log.e("",e.getMessage());
            return "";
        }
    }
}
