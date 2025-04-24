package com.example.hotelas.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.R;
import com.example.hotelas.model.common.DiscountDTO;

import java.util.List;

public class AppliedDiscountAdapter extends RecyclerView.Adapter<AppliedDiscountAdapter.DiscountViewHolder> {

    private List<DiscountDTO> discounts;

    public AppliedDiscountAdapter(List<DiscountDTO> discounts) {
        this.discounts = discounts;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount_applied, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        DiscountDTO discount = discounts.get(position);
        holder.discountName.setText(discount.getName());

        String summary = "Giảm " + discount.getDiscountPrecentage() + "% Tối đa " +
                String.format("%.0f", discount.getMaxDiscountAmount()) + " VND";
        holder.discountSummary.setText(summary);
    }

    @Override
    public int getItemCount() {
        return discounts.size();
    }

    static class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView discountName, discountSummary;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            discountName = itemView.findViewById(R.id.discountName);
            discountSummary = itemView.findViewById(R.id.discountSummary);
        }
    }
}

