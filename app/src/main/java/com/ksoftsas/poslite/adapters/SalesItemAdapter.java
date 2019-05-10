package com.ksoftsas.poslite.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.helpers.DatabaseManager;
import com.ksoftsas.poslite.models.SalesLine;
import com.ksoftsas.poslite.services.AppServices;

import java.util.ArrayList;
import java.util.List;

public class SalesItemAdapter extends RecyclerView.Adapter<SalesItemAdapter.MyViewHolder> implements View.OnClickListener {
    private Context context;
    private List<SalesLine> cartList;
    private TextView txtGrandTotal;
    private Spinner customerSpinner;
    private AppServices services = new AppServices();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description, price, unit_of_measure,discount;
        public Button clickQty, clickMinus,clickAdd;
        public ImageView delete,thumbnail;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;

        public MyViewHolder(View view) {
            super(view);
            description = view.findViewById(R.id.description);
            price = view.findViewById(R.id.price);
            thumbnail = view.findViewById(R.id.thumbnail);
            unit_of_measure = view.findViewById(R.id.unit_of_measure);
            discount = view.findViewById(R.id.discount);
            clickQty = view.findViewById(R.id.clickQty);
            clickMinus = view.findViewById(R.id.clickMinus);
            clickAdd = view.findViewById(R.id.clickAdd);
            delete = view.findViewById(R.id.delete_icon);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }
    public SalesItemAdapter(Context context, List<SalesLine> cartList, TextView txtGrandTotal) {
        this.context = context;
        this.cartList = cartList;
        this.txtGrandTotal = txtGrandTotal;
        this.customerSpinner = customerSpinner;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_item_row, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SalesLine item = cartList.get(position);
        holder.description.setText(item.getDescription());
        holder.price.setText(services.numberFormat(item.getUnitPrice(),"price") + "");
        holder.clickQty.setText(services.numberFormat(item.getQuantity(),"quantity"));
        //============= setOnClickListener
        holder.clickMinus.setTag(position);
        holder.clickMinus.setOnClickListener(this);
        holder.clickQty.setOnClickListener(this);
        holder.clickQty.setTag(position);
        holder.clickAdd.setOnClickListener(this);
        holder.clickAdd.setTag(position);
        holder.thumbnail.setImageBitmap(services.circleImage(item.getItemImage()));

    }
    @Override
    public int getItemCount() {
        return cartList.size();
    }
    public void removeItem(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
    }
    public void removeItemById(String value) {
        List<SalesLine> remove = new ArrayList<>();
        cartList.removeAll(remove);
        notifyDataSetChanged();
    }
    public void updateList(List<SalesLine> list){
        cartList = list;
        notifyDataSetChanged();
    }
    public void restoreItem(SalesLine item, int position) {
        cartList.add(position, item);
        notifyItemInserted(position);
    }
    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    public void calculationTotal(){
        float grandTotal = 0;
        for(SalesLine item: cartList){
            grandTotal += item.getGrandTotal();
        }
        txtGrandTotal.setText("Total: USD "+ services.numberFormat(grandTotal,"amount"));
    }
    public void onClick(View view) {

        SalesLine item;
        int id;
        double quantity;

        switch(view.getId()){
            case R.id.clickMinus :
                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                item = cartList.get((Integer) view.getTag());
                id =  item.getId();
                quantity = item.getQuantity();
                quantity = quantity - 1;
                Log.i("QTY",quantity + "");
                if(quantity > 0){
                    String selectQuery = "update sales_line set quantity = " + quantity
                            + ",sub_total = " + quantity * item.getUnitPrice()
                            + ",grand_total = " + quantity * item.getUnitPrice()
                            + " where id = " + id;
                    db.execSQL(selectQuery);

                    item.setQuantity(Float.parseFloat(quantity + ""));
                    item.setSubTotal(Float.parseFloat(quantity * item.getUnitPrice() + ""));
                    item.setGrandTotal(Float.parseFloat(quantity * item.getUnitPrice() + ""));
                    notifyItemChanged((Integer) view.getTag(), item);


                    calculationTotal();
                }
                else{
                    db.execSQL("delete from sales_line where id = " + id);
                    removeItem((Integer) view.getTag());
                }
                DatabaseManager.getInstance().closeDatabase();
                break;
            case R.id.clickAdd :
                db = DatabaseManager.getInstance().openDatabase();
                item = cartList.get((Integer) view.getTag());

                id =  item.getId();
                quantity = item.getQuantity();
                quantity = quantity + 1;
                Log.i("QTY",quantity + "");
                if(quantity > 0){
                    String selectQuery = "update sales_line set quantity = " + quantity
                        + ",sub_total = " + quantity * item.getUnitPrice()
                        + ",grand_total = " + quantity * item.getUnitPrice()
                        + " where id = " + id;
                    db.execSQL(selectQuery);

                    item.setQuantity(Float.parseFloat(quantity + ""));
                    item.setSubTotal(Float.parseFloat(quantity * item.getUnitPrice() + ""));
                    item.setGrandTotal(Float.parseFloat(quantity * item.getUnitPrice() + ""));
                    notifyItemChanged((Integer) view.getTag(), item);
                    calculationTotal();
                }
                DatabaseManager.getInstance().closeDatabase();
                break;
            default:
                break;
        }

    }
}
