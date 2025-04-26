package com.example.hotelas.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hotelas.ConfirmedFragment;
import com.example.hotelas.CustomerInfoFragment;
import com.example.hotelas.PaymentActivity;
import com.example.hotelas.PaymentDetailFragment;

public class PaymentPagerAdapter extends FragmentStateAdapter {

    public PaymentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CustomerInfoFragment();
            case 1:
                return new PaymentDetailFragment();
            case 2:
                return new ConfirmedFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

