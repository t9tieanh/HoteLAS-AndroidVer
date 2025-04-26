package com.example.hotelas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.hotelas.adapter.AmenityAdapter;
import com.example.hotelas.adapter.RoomTypeAdapter;
import com.example.hotelas.adapter.SearchHotelResultAdapter;
import com.example.hotelas.config.PaymentPrefManager;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.databinding.ActivityHotelDetailBinding;
import com.example.hotelas.model.common.AmenityDTO;
import com.example.hotelas.model.request.reservation.initial.InitialReservationRequest;
import com.example.hotelas.model.request.reservation.initial.ReservationDetailRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.hotel.HotelDestailResponse;
import com.example.hotelas.model.response.reservation.InitialReservationResponse;
import com.example.hotelas.model.response.room.RoomTypeResponse;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.hotel.HotelService;
import com.example.hotelas.service.reservation.ReservationAPIService;
import com.example.hotelas.service.reservation.ReservationService;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HotelDetailActivity extends AppCompatActivity {

    private ActivityHotelDetailBinding binding;
    private AmenityAdapter amenityAdapter;
    private RoomTypeAdapter roomTypeAdapter;

    private final List<AmenityDTO> amenities = new ArrayList<>();
    private final List<RoomTypeResponse> roomTypes = new ArrayList<>();
    private HotelDestailResponse hotelDetail;
    private boolean isExpanded = false;

    private PaymentPrefManager paymentPrefManager;

    // dùng để search
    String hotelId;
    Date checkIn;
    Date checkOut;
    int adultsCount;
    int roomCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate layout và gán view binding
        binding = ActivityHotelDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get data từ intent
        Intent intent = getIntent();

        hotelId = intent.getStringExtra("hotelId");
        checkIn = (Date) intent.getSerializableExtra("checkIn");
        checkOut = (Date) intent.getSerializableExtra("checkOut");
        adultsCount = intent.getIntExtra("adultsCount", 1);
        roomCount = intent.getIntExtra("roomCount", 1);

        // code phần description
        binding.toggleIntroText.setOnClickListener(v -> {
            if (isExpanded) {
                binding.introText.setMaxLines(4);
                binding.toggleIntroText.setText("Xem thêm");
            } else {
                binding.introText.setMaxLines(Integer.MAX_VALUE);
                binding.toggleIntroText.setText("Thu gọn");
            }
            isExpanded = !isExpanded;
        });

        getHotelDetail();
        setupHotelImages();


        // set mở tất cả ảnh
        binding.viewAllImagesButton.setOnClickListener(v -> {
            Intent intentt = new Intent(this, PhotogalleryActivity.class);
            intentt.putExtra("hotelId", hotelId);
            intentt.putExtra("hotelName", hotelDetail.getName());
            startActivity(intentt);
        });
    }

    private void getHotelDetail() {
        HotelService hotelService = new HotelService();

        hotelService.getHotelDetail(
                hotelId,
                checkIn,
                checkOut,
                (long) adultsCount,
                (long) roomCount,
                new HotelService.CallBack<HotelDestailResponse>() {
                    @Override
                    public void onSuccess(ApiResponse<HotelDestailResponse> result) {
                        hotelDetail = result.getResult();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            setHotelDetail();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(HotelDetailActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("HotelDetailError", errorMessage);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setHotelDetail () {
        // load ảnh
        Glide.with(this)
                .load(FileContant.FILE_API_URL +hotelDetail.getAvatar())
                .into(binding.mainImageLeft);

        if (hotelDetail.getImgs().get(0) != null) {
            Glide.with(this)
                    .load(FileContant.FILE_API_URL + hotelDetail.getImgs().get(0))
                    .into(binding.topRightImage);
        }
        if (hotelDetail.getImgs().get(1) != null) {
            Glide.with(this)
                    .load(FileContant.FILE_API_URL + hotelDetail.getImgs().get(1))
                    .into(binding.topRightImage);
        }


        // tạo adapter cho room
        RoomTypeAdapter adapter = new RoomTypeAdapter(hotelDetail.getRooms(), HotelDetailActivity.this,
                room -> {
                    List<ReservationDetailRequest> reservationDetails = new ArrayList<>();
                    reservationDetails.add(
                            ReservationDetailRequest.builder()
                                    .quantity(Long.valueOf(roomCount))
                                    .roomId(room.getId())
                                    .build()
                    );

                    // Chuyển đổi Date → LocalDate
                    LocalDate localCheckIn = checkIn.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    LocalDate localCheckOut = checkOut.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();

                    InitialReservationRequest request = InitialReservationRequest.builder()
                            .reservationDetails(reservationDetails)
                            .checkIn(localCheckIn)
                            .checkOut(localCheckOut)
                            .build();

                    String token = new PrefManager(HotelDetailActivity.this).getAuthResponse().getAccessToken();
                    if (token != null && !token.trim().isEmpty()) {
                        ReservationService reservationService = new ReservationService(token);

                        reservationService.createReservation(request, new ServiceExecutor.CallBack<InitialReservationResponse>() {
                            @Override
                            public void onSuccess(ApiResponse<InitialReservationResponse> result) {
                                // lưu lại thông tin thanh toán vào pref
                                paymentPrefManager = new PaymentPrefManager(HotelDetailActivity.this);
                                paymentPrefManager.savePaymentInfo(result.getResult());

                                Log.d("Tạo lịch đặt phòng","Tạo lịch đặt phòng thành công !");

                                // Chuyển sang PaymentActivity
                                Intent intent = new Intent(HotelDetailActivity.this, PaymentActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(HotelDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

        binding.roomTypeRecyclerView.setLayoutManager(
                new LinearLayoutManager(HotelDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.roomTypeRecyclerView.setAdapter(adapter);


        // tạo adapter cho amentity
        amenityAdapter = new AmenityAdapter(hotelDetail.getFacilities());
        binding.amenityRecyclerView.setLayoutManager(
                new LinearLayoutManager(HotelDetailActivity.this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.amenityRecyclerView.setAdapter(amenityAdapter);

        // set thông tin hotel
        binding.hotelNameText.setText(hotelDetail.getName());
        binding.introText.setText(hotelDetail.getDescription());

        //get giá
        NumberFormat format = NumberFormat.getInstance(new Locale("vi", "VN"));
        binding.priceText.setText(format.format(hotelDetail.getOriginalPrice()) + " VND");
        binding.subNameText.setText(hotelDetail.getSubName());
        binding.addressText.setText(hotelDetail.getAddress().toString());
    }

    private void setupHotelImages() {
        Glide.with(this)
                .load(FileContant.FILE_API_URL +"e544bf4b-b2c1-4f34-829c-c5585b8f6323.jpg")
                .into(binding.mainImageLeft);

        Glide.with(this)
                .load(FileContant.FILE_API_URL + "e544bf4b-b2c1-4f34-829c-c5585b8f6323.jpg")
                .into(binding.topRightImage);

        Glide.with(this)
                .load(FileContant.FILE_API_URL +"e544bf4b-b2c1-4f34-829c-c5585b8f6323.jpg")
                .into(binding.bottomRightImage);
    }


    private void setupBookButton() {
        binding.bookButton.setOnClickListener(v -> {
            // TODO: chuyển sang màn chọn phòng, hoặc thực hiện booking
            // Ví dụ:
            // Intent i = new Intent(this, RoomSelectionActivity.class);
            // i.putExtra("hotelId", currentHotelId);
            // startActivity(i);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // giải phóng reference
    }
}

