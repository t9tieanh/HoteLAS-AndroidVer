package com.example.hotelas.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.example.hotelas.model.response.reservation.InitialReservationResponse;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentPrefManager {

    private static final String PREF_NAME = "payment_pref";
    private static final String KEY_RESERVATION_ID = "reservation_id";
    private static final String KEY_EXPIRE_DATE_TIME = "expire_date_time";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public PaymentPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    // Lưu thông tin thanh toán
    public void savePaymentInfo(InitialReservationResponse response) {
        editor.putString(KEY_RESERVATION_ID, response.getReservationId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editor.putString(KEY_EXPIRE_DATE_TIME, response.getExpireDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
        editor.apply();
    }

    // Lấy thông tin thanh toán
    public InitialReservationResponse getPaymentInfo() {
        String reservationId = sharedPreferences.getString(KEY_RESERVATION_ID, null);
        String expireDateTimeStr = sharedPreferences.getString(KEY_EXPIRE_DATE_TIME, null);

        if (reservationId != null && expireDateTimeStr != null) {
            LocalDateTime expireDateTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                expireDateTime = LocalDateTime.parse(expireDateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }
            return new InitialReservationResponse(reservationId, expireDateTime);
        } else {
            return null;
        }
    }

    // Xóa thông tin thanh toán
    public void clearPaymentInfo() {
        editor.remove(KEY_RESERVATION_ID);
        editor.remove(KEY_EXPIRE_DATE_TIME);
        editor.apply();
    }

    public boolean isPaymentExpired() {
        // Lấy thông tin thanh toán
        String reservationId = sharedPreferences.getString(KEY_RESERVATION_ID, null);
        String expireDateTimeStr = sharedPreferences.getString(KEY_EXPIRE_DATE_TIME, null);

        if (reservationId != null && expireDateTimeStr != null) {
            // Chuyển đổi chuỗi thời gian hết hạn thành LocalDateTime
            LocalDateTime expireDateTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                expireDateTime = LocalDateTime.parse(expireDateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            }

            // Kiểm tra thời gian hết hạn
            LocalDateTime currentDateTime = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                currentDateTime = LocalDateTime.now();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return expireDateTime != null && expireDateTime.isBefore(currentDateTime);
            }
        }

        return true; // Nếu không có thông tin thanh toán, xem như đã hết hạn
    }


    public void updatePaymentField(PaymentField field, String newValue) {
        InitialReservationResponse paymentInfo = getPaymentInfo();
        if (paymentInfo == null) return;

        // Gọi enum để áp dụng thay đổi
        field.apply(paymentInfo, newValue);

        // Lưu lại thông tin thanh toán đã cập nhật
        savePaymentInfo(paymentInfo);
    }

    public enum PaymentField {
        RESERVATION_ID {
            @Override
            public void apply(InitialReservationResponse response, String value) {
                response.setReservationId(value);
            }
        },
        EXPIRE_DATE_TIME {
            @Override
            public void apply(InitialReservationResponse response, String value) {
                // Chuyển chuỗi ngày giờ thành LocalDateTime
                LocalDateTime expireDateTime = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    expireDateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
                response.setExpireDateTime(expireDateTime);
            }
        };

        // Áp dụng giá trị mới vào đối tượng InitialReservationResponse
        public abstract void apply(InitialReservationResponse response, String value);
    }
}
