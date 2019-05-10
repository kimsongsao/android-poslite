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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ksoftsas.poslite.R;
import com.ksoftsas.poslite.models.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravi on 16/11/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Category> contactList;
    private List<Category> contactListFiltered;
    private CategoryAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, other;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            other = view.findViewById(R.id.other);
            imageView = view.findViewById(R.id.imageView);
            imageView.setVisibility(View.GONE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onCategorySelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onCategoryLongSelected(contactListFiltered.get(getAdapterPosition()));
                    return false;
                }
            });
        }
    }


    public CategoryAdapter(Context context, List<Category> contactList, CategoryAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Category category = contactListFiltered.get(position);
        holder.name.setText(category.getDescription());
        holder.other.setText(category.getCountItems(category.getCode()) + " Items" );
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
                    List<Category> filteredList = new ArrayList<>();
                    for (Category row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getDescription().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (List<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CategoryAdapterListener {
        void onCategorySelected(Category contact);
        void onCategoryLongSelected(Category contact);
    }
}