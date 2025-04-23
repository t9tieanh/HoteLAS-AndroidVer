package com.example.hotelas.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hotelas.PhotoFragment;
import com.example.hotelas.enums.HotelImageEnum;
import com.example.hotelas.model.response.hotel.HotelImageTypeCountReponse;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final List<HotelImageTypeCountReponse> imageTypeList;

    public ViewPagerAdapter(@NonNull FragmentActivity fa, List<HotelImageTypeCountReponse> imageTypeList) {
        super(fa);
        this.imageTypeList = imageTypeList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        HotelImageEnum type = imageTypeList.get(position).getImageEnum();
        return PhotoFragment.newInstance(type.name()); // hoặc truyền description nếu fragment xử lý theo đó
    }

    @Override
    public int getItemCount() {
        return imageTypeList.size();
    }
}

