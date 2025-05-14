package com.example.hotelas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.R;
import com.example.hotelas.model.common.Address;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> implements Filterable {

    public interface OnAddressClickListener {
        void onAddressClick(Address address);
    }

    private final List<Address> originalList; // Danh sách gốc
    private final List<Address> filteredList; // Danh sách hiển thị
    private final OnAddressClickListener listener;

    public AddressAdapter(List<Address> data, OnAddressClickListener listener) {
        this.originalList = new ArrayList<>(data);
        this.filteredList = new ArrayList<>(data);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Address item = filteredList.get(position);
        h.addressTextView.setText(item.getAddress());

        h.selectButton.setOnClickListener(v -> {
            listener.onAddressClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView;
        ImageButton selectButton;

        ViewHolder(View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.addressText);
            selectButton = itemView.findViewById(R.id.selectButton);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Address> filtered = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filtered.addAll(originalList);
                } else {
                    String keyword = constraint.toString().toLowerCase().trim();
                    for (Address addr : originalList) {
                        if (addr.getAddress().toLowerCase().contains(keyword)) {
                            filtered.add(addr);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList.clear();
                filteredList.addAll((List<Address>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}

