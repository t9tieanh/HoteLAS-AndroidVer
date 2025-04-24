package com.example.hotelas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelas.R;
import com.example.hotelas.model.response.reservation.common.ReservationDetailResponse;

import java.util.List;

public class RoomSelectedAdapter extends RecyclerView.Adapter<RoomSelectedAdapter.RoomViewHolder> {

    private List<ReservationDetailResponse> roomList;
    private Context context;

    public RoomSelectedAdapter(Context context, List<ReservationDetailResponse> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_selected_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        ReservationDetailResponse room = roomList.get(position);
        holder.roomName.setText(room.getName());
        holder.roomDescription.setText(room.getDescription());
        holder.roomQuantity.setText("Số lượng: " + room.getQuantity());
        holder.roomPrice.setText("Giá: " + String.format("%,.0fđ", room.getPrice()));

        // Load ảnh (nếu có URL) - sử dụng Glide hoặc Picasso
        Glide.with(context)
                .load(room.getImgUrl())
                .placeholder(R.drawable.testhotel)
                .into(holder.roomImage);
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        ImageView roomImage;
        TextView roomName, roomDescription, roomQuantity, roomPrice;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.roomImage);
            roomName = itemView.findViewById(R.id.roomName);
            roomDescription = itemView.findViewById(R.id.roomDescription);
            roomQuantity = itemView.findViewById(R.id.roomQuantity);
            roomPrice = itemView.findViewById(R.id.roomPrice);
        }
    }
}

