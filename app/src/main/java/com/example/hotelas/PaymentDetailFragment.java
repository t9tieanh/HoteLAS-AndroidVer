package com.example.hotelas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PaymentDetailFragment extends Fragment {
    private CheckBox checkboxEWallet, checkboxOnArrival, checkboxPolicy;
    private LinearLayout layoutEWalletDesc, layoutOnArrivalDesc;

    public void PaymentMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view
        checkboxEWallet = view.findViewById(R.id.checkbox_e_wallet);
        checkboxOnArrival = view.findViewById(R.id.checkbox_on_arrival);
        checkboxPolicy = view.findViewById(R.id.checkbox_policy);

        layoutEWalletDesc = view.findViewById(R.id.tv_e_wallet_desc).getParent() instanceof LinearLayout ?
                (LinearLayout) view.findViewById(R.id.tv_e_wallet_desc).getParent() : null;
        layoutOnArrivalDesc = view.findViewById(R.id.ll_on_arrival_desc);

        // Xử lý sự kiện chọn checkbox ví điện tử
        checkboxEWallet.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxOnArrival.setChecked(false);
                if (layoutEWalletDesc != null) layoutEWalletDesc.setVisibility(View.VISIBLE);
                layoutOnArrivalDesc.setVisibility(View.GONE);
            } else {
                if (layoutEWalletDesc != null) layoutEWalletDesc.setVisibility(View.GONE);
            }
        });

        // Xử lý sự kiện chọn checkbox thanh toán khi nhận phòng
        checkboxOnArrival.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkboxEWallet.setChecked(false);
                layoutOnArrivalDesc.setVisibility(View.VISIBLE);
                if (layoutEWalletDesc != null) layoutEWalletDesc.setVisibility(View.GONE);
            } else {
                layoutOnArrivalDesc.setVisibility(View.GONE);
            }
        });
    }
}
