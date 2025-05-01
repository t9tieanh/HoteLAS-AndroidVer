package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.adapter.SearchHotelResultAdapter;
import com.example.hotelas.databinding.ActivitySearchResultsBinding;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.hotel.HotelResultResponse;
import com.example.hotelas.model.response.paging.PagingResponse;
import com.example.hotelas.service.hotel.HotelService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private SearchResultsAdapter adapter;
    private ActivitySearchResultsBinding binding;
    private final List<HotelResultResponse> resultHotels = new ArrayList<>();
    int numOfAdults;
    int roomCount;
    int checkInDay;
    int checkInMonth;
    int checkInYear;
    int checkOutDay;
    int checkOutMonth;
    int checkOutYear;

    Date checkInDate = null;
    Date checkOutDate = null;

    private List<HotelResultResponse> hotelList = new ArrayList<>();
    private RecyclerView hotelRecyclerView;
    private SearchHotelResultAdapter hotelAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // full screen
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        String location = getIntent().getStringExtra("location");
        numOfAdults = getIntent().getIntExtra("adultsCount", 1);
        roomCount = getIntent().getIntExtra("roomCount", 1);
        checkInDay = getIntent().getIntExtra("checkInDay", 0);
        checkInMonth = getIntent().getIntExtra("checkInMonth", 0);
        checkInYear = getIntent().getIntExtra("checkInYear", 0);
        checkOutDay = getIntent().getIntExtra("checkOutDay", 0);
        checkOutMonth = getIntent().getIntExtra("checkOutMonth", 0);
        checkOutYear = getIntent().getIntExtra("checkOutYear", 0);
        String checkOutDateString = getIntent().getStringExtra("checkOutDate");
        String checkInDateString = getIntent().getStringExtra("checkInDate");

        SimpleDateFormat parseFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat displayFormat = new SimpleDateFormat("d MMMM", Locale.getDefault());
        try {
            checkInDate = parseFormat.parse(checkInDateString);
            checkOutDate = parseFormat.parse(checkOutDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String checkInFormatted = displayFormat.format(checkInDate);
        String checkOutFormatted = displayFormat.format(checkOutDate);

        // set title
        binding.etSearch.setText(location + " " + checkInFormatted + " - " + checkOutFormatted);
        binding.chipLocation.setText(location);
        binding.chPcheckIn.setText(checkInFormatted);
        binding.chPcheckOut.setText(checkOutFormatted);

        searchHotel();
    }

    private void searchHotel () {
        HotelService hotelService = new HotelService();
        binding.progressBar.setVisibility(View.VISIBLE);

        hotelService.getListHotel(checkInDate,checkOutDate,(long)numOfAdults,(long)roomCount, 0 ,new HotelService.CallBack<PagingResponse<HotelResultResponse>>() {
            @Override
            public void onSuccess(ApiResponse<PagingResponse<HotelResultResponse>> result) {
                binding.progressBar.setVisibility(View.GONE);
                hotelList = result.getResult().getContent();

                if (hotelList == null || hotelList.isEmpty()) {
                    binding.tvResultCount.setText("Không có khách sạn nào !");
                    binding.tvNoResults.setVisibility(View.VISIBLE);
                    binding.recyclerViewResults.setVisibility(View.GONE);
                } else {
                    binding.tvResultCount.setText("Tìm kiếm được " + String.valueOf(result.getResult().getTotalElements()) +" có sẵn!");
                    binding.tvNoResults.setVisibility(View.GONE);
                    binding.recyclerViewResults.setVisibility(View.VISIBLE);

                    binding.recyclerViewResults.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));
                    hotelAdapter = new SearchHotelResultAdapter(hotelList, SearchResultActivity.this, new SearchHotelResultAdapter.OnHotelClickListener() {
                        @Override
                        public void onHotelClick(HotelResultResponse hotel) {
                            Intent intent = new Intent(SearchResultActivity.this, HotelDetailActivity.class);
                            intent.putExtra("hotelId", hotel.getId());
                            intent.putExtra("checkIn", checkInDate);
                            intent.putExtra("checkOut", checkOutDate);
                            intent.putExtra("adultsCount", numOfAdults);
                            intent.putExtra("roomCount", roomCount);
                            startActivity(intent);
                        }
                    });
                    binding.recyclerViewResults.setAdapter(hotelAdapter);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                binding.progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Đóng activity hiện tại, quay lại activity trước
        return true;
    }

}

