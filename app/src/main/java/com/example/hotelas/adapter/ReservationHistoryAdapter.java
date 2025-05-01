package com.example.hotelas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelas.R;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.model.response.reservation.history.ReservationHistoryResponse;

import java.util.List;

public class ReservationHistoryAdapter extends RecyclerView.Adapter<ReservationHistoryAdapter.ReservationViewHolder> {

    private final List<ReservationHistoryResponse> reservationList;
    private final Context context;

    public ReservationHistoryAdapter(Context context, List<ReservationHistoryResponse> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        ReservationHistoryResponse item = reservationList.get(position);

        holder.hotelNameTextView.setText(item.getHotelName());
        holder.addressTextView.setText("Địa chỉ: " + item.getAddress().toString()); // customize if needed
        holder.roomTypeTextView.setText("Loại phòng: " + item.getRoomTypeName());
        holder.totalTextView.setText("Tổng tiền: " + String.format("%,.0f VNĐ", item.getTotalPrice()));
        holder.bookingDateTextView.setText("Ngày đặt: " + item.getReservationDate());
        holder.checkinDateTextView.setText("Ngày nhận phòng: " + item.getCheckInDate());
        holder.checkoutDateTextView.setText("Ngày trả phòng: " + item.getCheckOutDate());

        // Load ảnh
        Glide.with(context)
                .load(FileContant.FILE_API_URL + item.getImg())
                .placeholder(R.drawable.testhotel)
                .into(holder.hotelImageView);
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {

        ImageView hotelImageView;
        TextView hotelNameTextView, addressTextView, roomTypeTextView,
                totalTextView, bookingDateTextView, checkinDateTextView, checkoutDateTextView;
        LinearLayout serviceLayout;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            hotelNameTextView = itemView.findViewById(R.id.hotelNameTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            roomTypeTextView = itemView.findViewById(R.id.roomTypeTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            bookingDateTextView = itemView.findViewById(R.id.bookingDateTextView);
            checkinDateTextView = itemView.findViewById(R.id.checkinDateTextView);
            checkoutDateTextView = itemView.findViewById(R.id.checkoutDateTextView);
            serviceLayout = itemView.findViewById(R.id.serviceLayout);
        }
    }
}

