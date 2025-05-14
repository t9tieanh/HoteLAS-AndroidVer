package com.example.hotelas;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.databinding.DataBindingUtil.bind;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.hotelas.adapter.BillingAdapter;
import com.example.hotelas.adapter.PaymentPagerAdapter;
import com.example.hotelas.adapter.RoomSelectedAdapter;
import com.example.hotelas.config.PaymentPrefManager;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.constant.FileContant;
import com.example.hotelas.databinding.ActivityPaymentBinding;
import com.example.hotelas.model.common.AddressDTO;
import com.example.hotelas.model.common.BillingItem;
import com.example.hotelas.model.common.DiscountDTO;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.reservation.InitialReservationResponse;
import com.example.hotelas.model.response.reservation.ReservationStepResponse;
import com.example.hotelas.model.response.reservation.common.ReservationDetailResponse;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.reservation.ReservationService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    private ActivityPaymentBinding binding;

    private PaymentPagerAdapter pagerAdapter;
    private PaymentPrefManager paymentPrefManager;

    public ReservationStepResponse reservationStepResponse;
    private ReservationService reservationService;

    private BillingAdapter billingAdapter;

    // Thông tin reservation
    public String reservationId;
    private LocalDateTime expiredDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            String scheme = data.getScheme(); // "myapp"
            String host = data.getHost();     // "payment"
            String path = data.getPath();     // "/success"

            // Em xử lý logic theo link ở đây
            if ("/success".equals(path)) {
                // Thành công rồi nè
                Toast.makeText(this, "Payment Success!", Toast.LENGTH_SHORT).show();
            }
        }


        // lấy thông tin reservation từ pref
        paymentPrefManager = new PaymentPrefManager(this);
        InitialReservationResponse savedResponse = paymentPrefManager.getPaymentInfo();
        reservationId = savedResponse.getReservationId();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            expiredDateTime = savedResponse.getExpireDateTime();
        }

        pagerAdapter = new PaymentPagerAdapter(this);
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.setUserInputEnabled(false);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
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

        // Vô hiệu hóa click vào tab
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) binding.tabLayout.getChildAt(0)).getChildAt(i);
            tab.setEnabled(false);
        }

        binding.selectedRoomsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.billRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set sự kiện cho nút tiếp tục
        binding.payButton.setOnClickListener(v -> onClickNextStep());

        getReservationInfo();
    }

    public void getReservationInfo() {
        String token = new PrefManager(this).getAuthResponse().getAccessToken();
        reservationService = new ReservationService(token);

        reservationService.getCurrentStep(reservationId,
                new ServiceExecutor.CallBack<ReservationStepResponse>() {
                    @Override
                    public void onSuccess(ApiResponse<ReservationStepResponse> result) {
                        reservationStepResponse = result.getResult();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            setReservationData();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(PaymentActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        Toast.makeText(PaymentActivity.this, "Bạn hiện chưa chọn đặt phòng nào!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(PaymentActivity.this, StartActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setReservationData() {

        // set total
        binding.totalPriceTextView.setText(reservationStepResponse.getTotalPrice().toString() + "VND");

        // hiển thị fragment hiên tại (tương ứng với step)
        nextStep(reservationStepResponse.getCurrentStep());

        try {
            String rawDateTime = reservationStepResponse.getExpireDateTime().toString();
            String cleanedDateTime = rawDateTime.split("\\.")[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime expireTime = LocalDateTime.parse(cleanedDateTime, formatter);

            ZonedDateTime zonedExpireTime = expireTime.atZone(ZoneId.systemDefault());
            long expireMillis = zonedExpireTime.toInstant().toEpochMilli();
            long nowMillis = System.currentTimeMillis();
            long millisUntilFinished = expireMillis - nowMillis;

            if (reservationStepResponse.getCurrentStep() == 2) {
                // đổi màu background
                binding.countdownText.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
                binding.countdownText.setTextColor(ContextCompat.getColor(this, R.color.md_theme_primary));
                millisUntilFinished = 60 * 1000; // <-- Nếu là Step 3 thì chỉ đếm 1 phút
            }

            if (millisUntilFinished <= 0) {
                binding.countdownText.setText("Hết thời gian giữ phòng!");
                binding.countdownText.setTextColor(Color.GRAY);
                return;
            }

            new CountDownTimer(millisUntilFinished, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    long minutes = seconds / 60;
                    seconds = seconds % 60;

                    String timeFormatted = String.format("%02d:%02d", minutes, seconds);

                    if (reservationStepResponse.getCurrentStep() == 2) {
                        binding.countdownText.setText("Chúng tôi sẽ đưa bạn về lại trang chủ trong: " + timeFormatted);
                    } else {
                        binding.countdownText.setText("Chúng tôi đang giữ phòng cho quý khách: " + timeFormatted);
                    }
                }

                public void onFinish() {
                    if (reservationStepResponse.getCurrentStep() == 2) {
                        binding.countdownText.setText("Đang đưa bạn về lại trang chủ...");
                        binding.countdownText.setTextColor(Color.GRAY);
                        // TODO: Em có thể cho tự động navigate về trang chủ ở đây

                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            // xóa thông tin reservation
                            paymentPrefManager = new PaymentPrefManager(PaymentActivity.this);
                            paymentPrefManager.clearPaymentInfo();


                            Intent intent = new Intent(PaymentActivity.this, StartActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            PaymentActivity.this.finish();
                        }, 1500); // Delay 1.5 giây

                    } else {
                        binding.countdownText.setText("Hết thời gian giữ phòng!");
                        binding.countdownText.setTextColor(Color.GRAY);
                    }
                }
            }.start();

        } catch (Exception e) {
            binding.countdownText.setText("Không thể hiển thị thời gian giữ phòng");
            binding.countdownText.setTextColor(Color.RED);
            e.printStackTrace();
        }

        // Set thông tin hotel
        binding.hotelNameText.setText(reservationStepResponse.getReservationDetail().get(0).getHotelName());
        binding.hotelAddressText.setText("Địa chỉ: " + reservationStepResponse.getReservationDetail().get(0).getAddress());

        Glide.with(this)
                .load(FileContant.FILE_API_URL + reservationStepResponse.getReservationDetail().get(0).getHotelImgUrl())
                .placeholder(R.drawable.loading)
                .into(binding.hotelImage);

        // Hiển thị các phòng đã chọn
        RoomSelectedAdapter adapter = new RoomSelectedAdapter(this, reservationStepResponse.getReservationDetail());
        binding.selectedRoomsRecyclerView.setAdapter(adapter);

        // Hiển thị thông tin hóa đơn
        List<BillingItem> billingItems = new ArrayList<>();

        for (ReservationDetailResponse response : reservationStepResponse.getReservationDetail()) {
            String label = "x" + response.getQuantity() + " " + response.getName();
            String value = "+" + (response.getPrice() * response.getQuantity()) + "vnd";
            billingItems.add(new BillingItem(BillingItem.Type.DISCOUNT, label, value));
        }

        for (DiscountDTO discount : reservationStepResponse.getDiscounts()) {
            String label = "Giảm: " + discount.getName();
            String value = "-" + discount.getDiscountPrecentage() + "%";
            billingItems.add(new BillingItem(BillingItem.Type.DISCOUNT, label, value));
        }

        billingAdapter = new BillingAdapter(billingItems);
        binding.billRecyclerView.setAdapter(billingAdapter);
    }

    // hiển thị fragment hiên tại (tương ứng với step)
    public void nextStep (int step) {
        binding.viewPager.setCurrentItem(step, true);
    }

    // các fragment được dùng trong payment activity đều phải implement interface này

    public interface OnNextStepClickListener {
        void onNextStep();
    }

    //
    private void onClickNextStep () {

        // lấy fragment
        int currentItem = binding.viewPager.getCurrentItem();
        String tag = "f" + currentItem;
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);

        if (fragment instanceof OnNextStepClickListener) {
            ((OnNextStepClickListener) fragment).onNextStep();  // Gọi hàm onNextStep() của fragment
        }
    }
}


