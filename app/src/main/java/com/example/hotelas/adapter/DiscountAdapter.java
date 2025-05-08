package com.example.hotelas.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.R;
import com.example.hotelas.model.common.DiscountDTO;
import com.example.hotelas.model.common.DiscountDTOForDiscountActivity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    private List<DiscountDTOForDiscountActivity> discountList;
    private Context context;

    public DiscountAdapter(Context context, List<DiscountDTOForDiscountActivity> discountList) {
        this.context = context;
        this.discountList = discountList;
    }

    @NonNull
    @Override
    public DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voucher, parent, false);
        return new DiscountViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        DiscountDTOForDiscountActivity discount = discountList.get(position);
        holder.tvName.setText(discount.getName());

        // Phần trăm giảm
        holder.tvDiscount.setText(discount.getDiscountPrecentage() + "% GIẢM");

        // Tính ngày còn lại
        if (discount.getEndDate() != null) {
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), discount.getEndDate());
            if (daysRemaining > 0) {
                holder.tvRemaining.setText(daysRemaining + " ngày còn lại");
            } else {
                holder.tvRemaining.setText("Đã hết hạn");
            }
        } else {
            holder.tvRemaining.setText("Không giới hạn");
        }

        // Điều kiện sử dụng
        String condition = String.format(
                "Tối thiểu: %.0fđ • Giảm tối đa: %.0fđ",
                discount.getMinBookingAmount(),
                discount.getMaxDiscountAmount()
        );
        holder.tvCondition.setText(condition);

        // Khi click vào checkbox
        holder.checkBox.setOnCheckedChangeListener(null); // tránh lỗi flicker do recycle view
        holder.checkBox.setChecked(discount.isSelected());

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            discount.setSelected(isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }


    public List<String> getSelectedDiscounsCode() {
        List<String> selected = new ArrayList<>();
        for (DiscountDTOForDiscountActivity dto : discountList) {
            if (dto.isSelected()) selected.add(dto.getCode());
        }
        return selected;
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDiscount, tvRemaining, tvCondition;
        CheckBox checkBox;

        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDiscount = itemView.findViewById(R.id.tvDiscount);
            tvRemaining = itemView.findViewById(R.id.tvRemaining);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
