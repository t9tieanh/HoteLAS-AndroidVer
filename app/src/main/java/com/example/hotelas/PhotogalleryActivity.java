package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hotelas.adapter.ViewPagerAdapter;
import com.example.hotelas.databinding.ActivityPhotogalleryBinding;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.hotel.HotelImageResponse;
import com.example.hotelas.model.response.hotel.HotelImageTypeCountReponse;
import com.example.hotelas.service.hotel.HotelService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Date;
import java.util.List;

public class PhotogalleryActivity extends AppCompatActivity {

//    private String hotelId = "26d7806d-d44f-400f-97ea-ff6b94ad1aed";
//    private String hotelName = "Lumia Sky Resort & Spa";

    // get data từ intent
    // Lấy dữ liệu từ Intent
    private String hotelId;
    private String hotelName;
    private List<HotelImageTypeCountReponse> imageTypeList;
    private ActivityPhotogalleryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhotogalleryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hotelId = getIntent().getStringExtra("hotelId");
        hotelName = getIntent().getStringExtra("hotelName");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thư viện ảnh của " + hotelName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getHotelDetail();
    }

    private void getHotelDetail() {
        HotelService hotelService = new HotelService();

        hotelService.getImageType(
                hotelId,
                new HotelService.CallBack<List<HotelImageTypeCountReponse>>() {
                    @Override
                    public void onSuccess(ApiResponse<List<HotelImageTypeCountReponse>> result) {
                        imageTypeList = result.getResult();
                        setupTabs();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(PhotogalleryActivity.this, "Không thể tải ảnh: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupTabs() {
        binding.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        ViewPagerAdapter adapter = new ViewPagerAdapter(this, imageTypeList);
        binding.viewPager.setAdapter(adapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    HotelImageTypeCountReponse item = imageTypeList.get(position);
                    String label = item.getImageEnum().getDescription() + " (" + item.getCount() + ")";
                    tab.setText(label);
                }
        ).attach();

        //load data trang đầu tiên
        loadImagesForTab(0);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Gọi API để tải ảnh cho tab đã chọn
                int position = tab.getPosition();
                loadImagesForTab(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không cần xử lý khi tab không được chọn
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không cần xử lý khi tab đã được chọn lại
            }
        });
    }

    private void loadImagesForTab(int position) {
        HotelImageTypeCountReponse item = imageTypeList.get(position);

        if (item == null)
            return;

        String imageType = item.getImageEnum().getDescription();

        // Gọi API tải ảnh cho tab hiện tại
        HotelService hotelService = new HotelService();
        hotelService.getImages(
                hotelId,
                imageType,
                new HotelService.CallBack<HotelImageResponse>() {
                    @Override
                    public void onSuccess(ApiResponse<HotelImageResponse> result) {
                        List<String> imageUrls = result.getResult().getImgs();

                        // Cập nhật fragment hiện tại
                        PhotoFragment fragment = (PhotoFragment) getSupportFragmentManager()
                                .findFragmentByTag("f" + position); // Fragment tag theo vị trí trong ViewPager2
                        if (fragment != null) {
                            fragment.updateImages(imageUrls);
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
