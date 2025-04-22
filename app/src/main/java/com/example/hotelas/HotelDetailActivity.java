package com.example.hotelas;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.hotelas.adapter.AmenityAdapter;
import com.example.hotelas.adapter.RoomTypeAdapter;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.databinding.ActivityHotelDetailBinding;
import com.example.hotelas.model.common.AmenityDTO;
import com.example.hotelas.model.response.room.RoomTypeResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelDetailActivity extends AppCompatActivity {

    private ActivityHotelDetailBinding binding;
    private AmenityAdapter amenityAdapter;
    private RoomTypeAdapter roomTypeAdapter;

    private final List<AmenityDTO> amenities = new ArrayList<>();
    private final List<RoomTypeResponse> roomTypes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate layout và gán view binding
        binding = ActivityHotelDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initDataFromIntent();
        setupAmenities();
        setupBookButton();
        setupRoomTypes();
        setupHotelImages();
    }

    private void setupRoomTypes() {
        List<RoomTypeResponse> roomTypes = new ArrayList<>();

        roomTypes.add(RoomTypeResponse.builder()
                .id("1")
                .name("Phòng Deluxe Giường Đôi")
                .price(950_000L)
                .maxOccupation(2L)
                .freeChildren(1L)
                .description("Có ban công, view hồ bơi, giường cỡ lớn")
                .avatar("https://cdn.pixabay.com/photo/2016/11/29/03/53/hotel-1867768_960_720.jpg")
                .imgRoomUrl(Arrays.asList(
                        "https://cdn.pixabay.com/photo/2016/11/29/03/53/hotel-1867768_960_720.jpg",
                        "https://cdn.pixabay.com/photo/2017/01/20/00/30/hotel-room-1997286_960_720.jpg"
                ))
                .build()
        );

        roomTypes.add(RoomTypeResponse.builder()
                .id("2")
                .name("Phòng Superior")
                .price(750_000L)
                .maxOccupation(2L)
                .freeChildren(0L)
                .description("Có cửa sổ, nội thất hiện đại")
                .avatar("https://cdn.pixabay.com/photo/2017/01/20/00/30/hotel-room-1997286_960_720.jpg")
                .imgRoomUrl(Arrays.asList(
                        "https://cdn.pixabay.com/photo/2017/01/20/00/30/hotel-room-1997286_960_720.jpg"
                ))
                .build()
        );

        roomTypes.add(RoomTypeResponse.builder()
                .id("3")
                .name("Phòng Gia Đình")
                .price(1_250_000L)
                .maxOccupation(4L)
                .freeChildren(2L)
                .description("Phù hợp cho gia đình, rộng rãi, thoáng mát")
                .avatar("https://cdn.pixabay.com/photo/2017/08/06/11/02/interior-2591364_960_720.jpg")
                .imgRoomUrl(Arrays.asList(
                        "https://cdn.pixabay.com/photo/2017/08/06/11/02/interior-2591364_960_720.jpg"
                ))
                .build()
        );

        // Truyền vào Adapter
        RoomTypeAdapter adapter = new RoomTypeAdapter(roomTypes, this);
        binding.roomTypeRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.roomTypeRecyclerView.setAdapter(adapter);
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


    /**
     * Lấy thông tin tên và địa chỉ khách sạn từ Intent (nếu có)
     */
    private void initDataFromIntent() {
        Intent intent = getIntent();
        String hotelName = intent.getStringExtra("hotelName");
        String hotelAddress = intent.getStringExtra("hotelAddress");

        if (hotelName != null) {
            binding.hotelNameText.setText(hotelName);
        }
        if (hotelAddress != null) {
            binding.addressText.setText(hotelAddress);
        }
    }

    /**
     * Khởi tạo danh sách tiện ích và gán vào RecyclerView
     */
    private void setupAmenities() {
        // Ví dụ backend trả về 4 tiện ích, bạn hãy thay bằng dữ liệu thực tế khi fetch xong
        amenities.add(new AmenityDTO(
                "Máy lạnh",
                "https://s3-ap-southeast-1.amazonaws.com/cntres-assets-ap-southeast-1-250226768838-cf675839782fd369/imageResource/2016/12/21/1482303254515-bd78d369590cba427807f5b7b3df6022.png",
                "Cơ sở kinh doanh"
        ));
        amenities.add(new AmenityDTO(
                "WiFi miễn phí",
                "https://s3-ap-southeast-1.amazonaws.com/cntres-assets-ap-southeast-1-250226768838-cf675839782fd369/imageResource/2016/12/21/1482303254515-bd78d369590cba427807f5b7b3df6022.png",
                "Dịch vụ"
        ));
        amenities.add(new AmenityDTO(
                "Thang máy",
                "https://s3-ap-southeast-1.amazonaws.com/cntres-assets-ap-southeast-1-250226768838-cf675839782fd369/imageResource/2016/12/21/1482303254515-bd78d369590cba427807f5b7b3df6022.png",
                "Tiện ích chung"
        ));
        amenities.add(new AmenityDTO(
                "Lễ tân 24h",
                "https://s3-ap-southeast-1.amazonaws.com/cntres-assets-ap-southeast-1-250226768838-cf675839782fd369/imageResource/2016/12/21/1482303254515-bd78d369590cba427807f5b7b3df6022.png",
                "Dịch vụ"
        ));

        // Khởi tạo adapter và layout manager ngang
        amenityAdapter = new AmenityAdapter(amenities);
        binding.amenityRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.amenityRecyclerView.setAdapter(amenityAdapter);
    }

    /**
     * Bắt sự kiện khi người dùng click nút "Chọn Phòng"
     */
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

