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
        editor.putString(KEY_EXPIRE_DATE_TIME, response.getExpireDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
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
}
