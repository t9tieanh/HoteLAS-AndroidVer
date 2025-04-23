package com.example.hotelas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hotelas.R;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.model.response.room.RoomTypeResponse;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomTypeAdapter extends RecyclerView.Adapter<RoomTypeAdapter.RoomViewHolder> {

    private final List<RoomTypeResponse> roomList;
    private final Context context;

    public RoomTypeAdapter(List<RoomTypeResponse> roomList, Context context) {
        this.roomList = roomList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room_type, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        RoomTypeResponse room = roomList.get(position);

        holder.roomName.setText(room.getName());
        holder.roomDesc.setText(room.getDescription());

        // Format giá
        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        holder.roomPrice.setText(format.format(room.getPrice()) + " VND");

        // Load ảnh (dùng Glide)
        Glide.with(context)
                .load(room.getAvatar() != null
                        ? FileContant.FILE_API_URL + room.getAvatar()
                        : FileContant.FILE_API_URL + room.getImgRoomUrl().get(0))
                .placeholder(R.drawable.loading)
                .into(holder.roomImage);

        holder.bookBtn.setOnClickListener(v -> {
            Toast.makeText(context, "Đặt phòng: " + room.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {

        ImageView roomImage;
        TextView roomName, roomDesc, roomPrice;
        Button bookBtn;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.roomImage);
            roomName = itemView.findViewById(R.id.roomName);
            roomDesc = itemView.findViewById(R.id.roomDesc);
            roomPrice = itemView.findViewById(R.id.roomPrice);
            bookBtn = itemView.findViewById(R.id.bookBtn);
        }
    }
}
