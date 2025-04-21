package com.example.hotelas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.R;
import com.example.hotelas.model.common.Address;

import java.util.List;

public class AddressAdapter
        extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    public interface OnAddressClickListener {
        void onAddressClick(Address address);
    }

    private final List<Address> data;
    private final OnAddressClickListener listener;

    public AddressAdapter(List<Address> data, OnAddressClickListener listener) {
        this.data = data;
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
        Address item = data.get(position);
        h.addressTextView.setText(item.getAddress());

        h.selectButton.setOnClickListener(v -> {
            listener.onAddressClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressTextView;
        Button selectButton;
        ViewHolder(View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.addressText);
            selectButton    = itemView.findViewById(R.id.selectButton);
        }
    }
}
