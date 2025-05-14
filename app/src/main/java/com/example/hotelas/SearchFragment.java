package com.example.hotelas;
import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.hotelas.adapter.ImagesViewPageAdapter;
import com.example.hotelas.databinding.FragmentSearchBinding;
import com.example.hotelas.dialog.GuestPickerDialog;
import com.example.hotelas.dialog.LocationDialogActivity;
import com.example.hotelas.model.common.Images;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.relex.circleindicator.CircleIndicator;

public class SearchFragment extends Fragment implements GuestPickerDialog.GuestPickerListener {

    private FragmentSearchBinding binding;
    private MaterialButton locationButton;
    private MaterialButton datepickerButton;
    private MaterialButton guestpickerButton;
    private MaterialButton searchButton;
    private HorizontalScrollView domesticView;
    private HorizontalScrollView internationalView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private List<Images> imagesList;
    private String checkInDate;
    private String checkOutDate;
    private int adultsCount = 1;
    private int roomCount = 1;
    private int checkInDay = 0;
    private int checkInMonth = 0;
    private int checkInYear = 0;
    private int checkOutDay = 0;
    private int checkOutMonth = 0;
    private int checkOutYear = 0;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager.getCurrentItem() == imagesList.size() - 1) {
                viewPager.setCurrentItem(0); // Quay lại trang đầu tiên nếu đang ở trang cuối
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1); // Chuyển sang trang kế tiếp
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        requireActivity().setTitle("Search");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationButton = binding.locationButton;
        datepickerButton = binding.datepickerButton;
        guestpickerButton = binding.guestpickerButton;
        searchButton = binding.searchButton;
        tabLayout = binding.hotDealFragment.findViewById(R.id.tabLayout);
        domesticView = binding.hotDealFragment.findViewById(R.id.domesticView);
        internationalView = binding.hotDealFragment.findViewById(R.id.internationalView);

        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LocationDialogActivity.class);
            startActivityForResult(intent, LOCATION_DIALOG_REQUEST_CODE);
        });

        datepickerButton.setOnClickListener(v -> showDatePickerDialog());

        guestpickerButton.setOnClickListener(v -> {
            GuestPickerDialog guestPickerDialog = new GuestPickerDialog();
            guestPickerDialog.setGuestPickerListener(this);
            guestPickerDialog.show(getParentFragmentManager(), "GuestPickerDialog");
        });

        searchButton.setOnClickListener(v -> {

            if (checkInDate == null || checkOutDate == null) {
                Toast.makeText(requireContext(), "Vui lòng chọn ngày checkIn và checkOut", Toast.LENGTH_SHORT).show();
                return;
            }

            String location = locationButton.getText().toString();
            Intent intent = new Intent(requireContext(), SearchResultActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("adultsCount", adultsCount);
            intent.putExtra("roomCount", roomCount);
            intent.putExtra("checkInDay", checkInDay);
            intent.putExtra("checkInMonth", checkInMonth);
            intent.putExtra("checkInYear", checkInYear);
            intent.putExtra("checkOutDay", checkOutDay);
            intent.putExtra("checkOutMonth", checkOutMonth);
            intent.putExtra("checkOutYear", checkOutYear);
            intent.putExtra("checkInDate", checkInDate);
            intent.putExtra("checkOutDate", checkOutDate);
            startActivity(intent);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    domesticView.setVisibility(View.VISIBLE);
                    internationalView.setVisibility(View.GONE);
                } else {
                    domesticView.setVisibility(View.GONE);
                    internationalView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        loadImage();

        loadTab(tabLayout);
        loadDomesticCard();
        loadInternationCard();

        loadDiscountPicture();
    }

    private void loadImage() {


        viewPager = binding.viewpage;
        circleIndicator = binding.circleIndicator;

        imagesList = getListImages();
        ImagesViewPageAdapter adapter = new ImagesViewPageAdapter(imagesList);
        viewPager.setAdapter(adapter);

        // Liên kết viewpager và indicator
        circleIndicator.setViewPager(viewPager);

        handler.postDelayed(runnable, 3000);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 3000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000); // delayMillis: 3000
    }

    private List<Images> getListImages() {
        List<Images> list = new ArrayList<>();
        list.add(new Images(R.drawable.banner01));
        list.add(new Images(R.drawable.banner02));
        list.add(new Images(R.drawable.banner03));
        list.add(new Images(R.drawable.banner04));
        return list;
    }

    private void loadDiscountPicture() {
        int[] cardIds = { R.id.picdiscount1, R.id.picdiscount2, R.id.picdiscount3 };
        int[] imageRes = { R.drawable.picdiscount3, R.drawable.picdiscount1, R.drawable.picdiscount2 };

        for (int i = 0; i < cardIds.length; i++) {
            CardView card = binding.discountFragment.findViewById(cardIds[i]);
            ImageView imageView = card.findViewById(R.id.imageDiscount);

            imageView.setImageResource(imageRes[i]);
        }
    }
    private void loadTab(TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText("Trong nước"));

        TabLayout.Tab tabQuocTe = tabLayout.newTab().setText("Quốc tế");
        tabLayout.addTab(tabQuocTe);

        tabLayout.post(() -> {
            View tabView = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getTabCount() - 1);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
            params.leftMargin = 20;
            tabView.setLayoutParams(params);
        });
    }

    private void loadDomesticCard() {
        int[] cardIds = { R.id.cardHCM, R.id.cardHN, R.id.cardDN, R.id.cardDL };
        int[] imageRes = { R.drawable.hcm, R.drawable.hn, R.drawable.danang, R.drawable.dalat };
        String[] locationNames = { "Hồ Chí Minh", "Hà Nội", "Đà Nẵng", "Đà Lạt" };

        for (int i = 0; i < cardIds.length; i++) {
            CardView card = binding.hotDealFragment.findViewById(cardIds[i]);
            ImageView imageView = card.findViewById(R.id.imageLocation);
            TextView textView = card.findViewById(R.id.locationName);

            imageView.setImageResource(imageRes[i]);
            textView.setText(locationNames[i]);
        }
    }

    private void loadInternationCard() {
        int[] cardIds = { R.id.cardFrance, R.id.cardSingapore, R.id.cardKorea, R.id.cardJapan, R.id.cardUK };
        int[] imageRes = { R.drawable.france, R.drawable.singapore, R.drawable.korea, R.drawable.japan, R.drawable.uk };
        String[] locationNames = { "Pháp", "Singapore", "Hàn Quốc", "Nhật Bản", "Anh" };

        for (int i = 0; i < cardIds.length; i++) {
            CardView card = binding.hotDealFragment.findViewById(cardIds[i]);
            ImageView imageView = card.findViewById(R.id.imageLocation);
            TextView textView = card.findViewById(R.id.locationName);

            imageView.setImageResource(imageRes[i]);
            textView.setText(locationNames[i]);
        }
    }

    private void showDatePickerDialog() {
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select dates");
        builder.setSelection(androidx.core.util.Pair.create(
                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
        ));
        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker = builder.build();

        picker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Long startDate = selection.first;
            Long endDate = selection.second;

            checkInDate = dateFormat.format(new Date(startDate));
            checkOutDate = dateFormat.format(new Date(endDate));

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(startDate);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(endDate);

            checkInDay = startCalendar.get(Calendar.DAY_OF_MONTH);
            checkInMonth = startCalendar.get(Calendar.MONTH);
            checkInYear = startCalendar.get(Calendar.YEAR);

            checkOutDay = endCalendar.get(Calendar.DAY_OF_MONTH);
            checkOutMonth = endCalendar.get(Calendar.MONTH);
            checkOutYear = endCalendar.get(Calendar.YEAR);

            String selectedDatesText = " " + checkInDate + " - " + checkOutDate;
            datepickerButton.setText(selectedDatesText);
        });

        picker.show(getChildFragmentManager(), "dateRangePicker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_DIALOG_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String location = data.getStringExtra("location");
                if (location != null) {
                    locationButton.setText(location);
                }
            }
        }
    }

    @Override
    public void onGuestCountsSelected(int adultsCount, int roomCount) {
        this.adultsCount = adultsCount;
        this.roomCount = roomCount;
        updateGuestPickerButtonText();
    }

    private void updateGuestPickerButtonText() {
        String guestText = adultsCount + " Khách, " + roomCount + " Phòng";
        guestpickerButton.setText(guestText);
    }

    private static final int LOCATION_DIALOG_REQUEST_CODE = 1;
}