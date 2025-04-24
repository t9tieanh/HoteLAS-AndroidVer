package com.example.hotelas;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hotelas.adapter.BillingAdapter;
import com.example.hotelas.adapter.PaymentPagerAdapter;
import com.example.hotelas.adapter.RoomSelectedAdapter;
import com.example.hotelas.model.common.AddressDTO;
import com.example.hotelas.model.common.BillingItem;
import com.example.hotelas.model.common.DiscountDTO;
import com.example.hotelas.model.response.reservation.common.ReservationDetailResponse;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private PaymentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationOnClickListener(v -> finish());
//        getSupportActionBar().hide();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new PaymentPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        TextView countdownText = findViewById(R.id.countdownText);
        new CountDownTimer(600000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;

                String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                countdownText.setText("Chúng tôi đang giữ phòng cho quý khách: " + timeFormatted);
            }

            public void onFinish() {
                countdownText.setText("Hết thời gian giữ phòng!");
                countdownText.setTextColor(Color.GRAY);
                // Có thể thêm logic điều hướng hoặc thông báo ở đây
            }

        }.start();

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Thông tin khách hàng");
                    tab.setIcon(R.drawable.ic_one);
                    break;
                case 1:
                    tab.setText("Chi tiết thanh toán");
                    tab.setIcon(R.drawable.ic_two);
                    break;
                case 2:
                    tab.setText("Đã xác nhận");
                    tab.setIcon(R.drawable.ic_three);
                    break;
            }
        }).attach();

        RecyclerView recyclerView = findViewById(R.id.selectedRoomsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu ảo
        List<ReservationDetailResponse> selectedRooms = new ArrayList<>();

        selectedRooms.add(ReservationDetailResponse.builder()
                .hotelName("Aurora Palace")
                .address(new AddressDTO("45 Đ. Nguyễn Tất Thành", "Quận 7", "Hồ Chí Minh", "Việt Nam"))
                .hotelImgUrl("https://example.com/hotel.jpg")
                .name("Basic Room")
                .description("Phòng Deluxe Ocean View mang đến không gian sang trọng và thoải mái với diện tích rộng rãi, thiết kế hiện đại.")
                .imgUrl("https://example.com/room1.jpg")
                .quantity(2L)
                .price(1200000.0)
                .build());

        selectedRooms.add(ReservationDetailResponse.builder()
                .hotelName("Aurora Palace")
                .address(new AddressDTO("45 Đ. Nguyễn Tất Thành", "Quận 7", "Hồ Chí Minh", "Việt Nam"))
                .hotelImgUrl("https://example.com/hotel.jpg")
                .name("Suite Room")
                .description("Phòng cao cấp với view biển, giường lớn, nội thất hiện đại và bồn tắm riêng.")
                .imgUrl("https://example.com/room2.jpg")
                .quantity(1L)
                .price(2200000.0)
                .build());

        // Sửa lại context khi truyền vào Adapter
        RoomSelectedAdapter adapter = new RoomSelectedAdapter(this, selectedRooms);
        recyclerView.setAdapter(adapter);


        // thêm phần bill
        RecyclerView recyclerVieww = findViewById(R.id.billRecyclerView);
        List<DiscountDTO> discountList = new ArrayList<>();

        discountList.add(DiscountDTO.builder()
                .name("Khuyến mãi tháng 4")
                .code("APRIL2025")
//                .beginDate(LocalDate.of(2025, 4, 1))
//                .endDate(LocalDate.of(2025, 4, 30))
                .discountPrecentage(10)
                .minloyaltyPoints(0)
                .minBookingAmount(1000000)
                .maxDiscountAmount(200000)
                .isPublic(true)
                .build());

        discountList.add(DiscountDTO.builder()
                .name("Thành viên vàng")
                .code("GOLDMEMBER")
//                .beginDate(LocalDate.of(2025, 1, 1))
//                .endDate(LocalDate.of(2025, 12, 31))
                .discountPrecentage(5)
                .minloyaltyPoints(100)
                .minBookingAmount(500000)
                .maxDiscountAmount(100000)
                .isPublic(false)
                .build());

        List<BillingItem> billingItems = new ArrayList<>();
        billingItems.add(new BillingItem(BillingItem.Type.ORIGINAL_PRICE, "Giá gốc", "1.500.000 ₫"));

        for (DiscountDTO discount : discountList) {
            String label = "Giảm: " + discount.getName();
            String value = "-" + discount.getDiscountPrecentage() + "%";
            billingItems.add(new BillingItem(BillingItem.Type.DISCOUNT, label, value));
        }

        BillingAdapter adapterb = new BillingAdapter(billingItems);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(this));
        recyclerVieww.setAdapter(adapterb);
    }
}

