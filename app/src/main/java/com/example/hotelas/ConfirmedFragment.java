package com.example.hotelas;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hotelas.databinding.FragmentConfirmedBinding;

public class ConfirmedFragment extends Fragment {

    private FragmentConfirmedBinding binding;
    private String reservationId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentConfirmedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set reservation id
        reservationId = ((PaymentActivity)requireActivity()).reservationId;
        binding.reservationIdText.setText("Mã đặt phòng của quý khách là: " + reservationId);

        //load gif
        Glide.with(this)
                .asGif()
                .load(R.drawable.ic_success)
                .into(binding.successIcon);



        setupThankYouText();
        setupButton();
    }

    private void setupThankYouText() {
        String fullText = "Cảm ơn bạn đã đặt phòng với HotelAS";
        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf("HotelAS");
        int end = start + "HotelAS".length();

        // Tô màu chữ 'H' màu đỏ
        spannableString.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red)),
                start,
                start + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        // Tô màu các chữ còn lại màu xanh
        spannableString.setSpan(
                new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.md_theme_primary)),
                start + 1,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        binding.thankYouText.setText(spannableString);
    }

    private void setupButton() {
        binding.backHomeBtn.setOnClickListener(v -> {
            // Xử lý quay về trang chủ (em có thể navigate hoặc popBackStack tùy flow app em)
            requireActivity().onBackPressed();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
