package com.ksoftsas.poslite.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.models.Item;
import com.ksoftsas.poslite.models.SalesHeader;
import com.ksoftsas.poslite.services.AppServices;

import java.util.ArrayList;
import java.util.List;


public class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<SalesHeader> contactList;
    private List<SalesHeader> contactListFiltered;
    private ItemAdapterListener listener;
    AppServices services;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, other, code;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            other = view.findViewById(R.id.other);
            code = view.findViewById(R.id.code);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onItemSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ReceiptsAdapter(Context context, List<SalesHeader> contactList, ItemAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
        services = new AppServices();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipts_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SalesHeader item = contactListFiltered.get(position);
        holder.code.setText("USD " + services.numberFormat(item.getReceiptTotal(),"amount"));
        holder.name.setText("#"+item.getId());
        holder.other.setText(item.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<SalesHeader> filteredList = new ArrayList<>();
                    for (SalesHeader row : contactList) {
                        String id = row.getId() + "";
                        String total = row.getReceiptTotal() + "";
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getOrderDate().toLowerCase().contains(charString.toLowerCase())
                                || id.toLowerCase().contains(charString.toLowerCase())
                                || total.toLowerCase().contains(charString.toLowerCase())
                                ) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (List<SalesHeader>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemAdapterListener {
        void onItemSelected(SalesHeader item);
    }
}