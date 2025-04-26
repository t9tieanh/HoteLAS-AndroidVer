package com.example.hotelas.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.R;
import com.example.hotelas.model.common.BillingItem;

import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.BillingViewHolder> {

    private List<BillingItem> itemList;

    public BillingAdapter(List<BillingItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public BillingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billing_detail, parent, false);
        return new BillingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillingViewHolder holder, int position) {
        BillingItem item = itemList.get(position);
        holder.label.setText(item.getLabel());
        holder.value.setText(item.getValue());

        // Áp dụng màu cho mã giảm giá
        if (item.getType() == BillingItem.Type.DISCOUNT) {
            holder.label.setTextColor(Color.parseColor("#388E3C")); // xanh lá
            holder.value.setTextColor(Color.parseColor("#388E3C"));
        } else {
            holder.label.setTextColor(Color.BLACK);
            holder.value.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class BillingViewHolder extends RecyclerView.ViewHolder {
        TextView label, value;

        BillingViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.labelText);
            value = itemView.findViewById(R.id.valueText);
        }
    }
}

