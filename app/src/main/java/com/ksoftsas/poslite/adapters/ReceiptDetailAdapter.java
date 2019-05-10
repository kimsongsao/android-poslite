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
import com.ksoftsas.poslite.models.SalesLine;
import com.ksoftsas.poslite.services.AppServices;

import java.util.ArrayList;
import java.util.List;


public class ReceiptDetailAdapter extends RecyclerView.Adapter<ReceiptDetailAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<SalesLine> contactList;
    private List<SalesLine> contactListFiltered;
    private ItemAdapterListener listener;
    AppServices services;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, other, code;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            other = view.findViewById(R.id.other);
            code = view.findViewById(R.id.code);
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // send selected contact in callback
//                    listener.onItemSelected(contactListFiltered.get(getAdapterPosition()));
//                }
//            });
        }
    }


    public ReceiptDetailAdapter(Context context, List<SalesLine> contactList) {
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
        final SalesLine item = contactListFiltered.get(position);
        holder.code.setText(services.numberFormat(item.getGrandTotal(),"amount"));
        holder.name.setText(item.getDescription());
        holder.other.setText(services.numberFormat(item.getQuantity(),"quantity") + " x " + services.numberFormat(item.getUnitPrice(),"price"));
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
                    List<SalesLine> filteredList = new ArrayList<>();
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (List<SalesLine>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemAdapterListener {
        void onItemSelected(SalesLine item);
    }
}