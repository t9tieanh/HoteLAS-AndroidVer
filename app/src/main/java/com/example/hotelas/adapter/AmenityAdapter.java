package com.example.hotelas.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.hotelas.R;
import com.example.hotelas.model.common.AmenityDTO;

import java.util.List;

public class AmenityAdapter extends RecyclerView.Adapter<AmenityAdapter.AmenityViewHolder> {

    private List<AmenityDTO> amenities;

    public AmenityAdapter(List<AmenityDTO> amenities) {
        this.amenities = amenities;
    }

    @NonNull
    @Override
    public AmenityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_amenity, parent, false);
        return new AmenityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmenityViewHolder holder, int position) {
        AmenityDTO amenity = amenities.get(position);
        holder.label.setText(amenity.getName());

        // Load image từ URL với Glide
        Glide.with(holder.icon.getContext())
                .load(amenity.getCategoryUrl().trim())
                .placeholder(R.drawable.baseline_close_24)
                .error(R.drawable.icon_close)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GLIDE", "Load ảnh thất bại: " + e.getMessage());
                        return false; // để Glide vẫn hiển thị .error(...) drawable
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("GLIDE", "Load ảnh thành công");
                        return false; // để Glide tự set resource lên ImageView
                    }
                })
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return amenities != null ? amenities.size() : 0;
    }

    static class AmenityViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView label;

        public AmenityViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.amenityIcon);
            label = itemView.findViewById(R.id.amenityLabel);
        }
    }
}