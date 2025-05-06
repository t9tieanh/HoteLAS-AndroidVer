package com.example.hotelas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelas.R;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.model.response.hotel.HotelResultResponse;

import java.util.List;

public class SearchHotelResultAdapter extends RecyclerView.Adapter<SearchHotelResultAdapter.HotelViewHolder> {
    private List<HotelResultResponse> hotelList;
    private Context context;
    private OnHotelClickListener listener;

    public interface OnHotelClickListener {
        void onHotelClick(HotelResultResponse hotel);
    }

    public SearchHotelResultAdapter(List<HotelResultResponse> hotelList, Context context, OnHotelClickListener listener) {
        this.hotelList = hotelList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_searchresult_hotel, parent, false);
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        HotelResultResponse hotel = hotelList.get(position);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onHotelClick(hotel);
            }
        });

        holder.addressText.setText(hotel.getName());
        if (hotel.getAddress() != null) {
            holder.regionNameTextView.setText(hotel.getAddress().toString());
        }

        holder.reviewScoreTextView.setText(hotel.getRating() + " / 10");
        holder.originalPriceTextView.setText(String.valueOf(hotel.getOriginalPrice()));
        holder.priceInfoTextView.setText(String.valueOf(hotel.getMinRoomPrice()));

        // Load ảnh
        if (hotel.getAvatar() != null && !hotel.getAvatar().isEmpty()) {
            Glide.with(context)
                    .load(FileContant.FILE_API_URL + hotel.getAvatar())
                    .placeholder(R.drawable.testhotel)
                    .into(holder.hotelImageView);
        } else {
            holder.hotelImageView.setImageResource(R.drawable.testhotel);
        }
    }

    @Override
    public int getItemCount() {
        return hotelList != null ? hotelList.size() : 0;
    }

    public static class HotelViewHolder extends RecyclerView.ViewHolder {

        ImageView hotelImageView;
        TextView addressText, regionNameTextView, reviewScoreTextView,
                originalPriceTextView, priceInfoTextView, priceInfo;
        ImageButton iconRating;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            hotelImageView = itemView.findViewById(R.id.hotelImageView);
            addressText = itemView.findViewById(R.id.addressText);
            regionNameTextView = itemView.findViewById(R.id.regionNameTextView);
            reviewScoreTextView = itemView.findViewById(R.id.reviewScoreTextView);
            originalPriceTextView = itemView.findViewById(R.id.originalPriceTextView);
            priceInfoTextView = itemView.findViewById(R.id.priceInfoTextView);
            priceInfo = itemView.findViewById(R.id.priceInfo);
            iconRating = itemView.findViewById(R.id.icon_rating);
        }
    }
}
