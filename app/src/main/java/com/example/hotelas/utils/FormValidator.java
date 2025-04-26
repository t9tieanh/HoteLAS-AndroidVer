package com.example.hotelas.utils;

import android.util.Patterns;

public class FormValidator {

    // Validate Name (không được trống)
    public static boolean validateName(String name) {
        return !name.trim().isEmpty();
    }

    // Validate Phone (phải là số và không được trống)
    public static boolean validatePhone(String phone) {
        return !phone.trim().isEmpty() && Patterns.PHONE.matcher(phone).matches();
    }

    // Validate Email (đúng định dạng email)
    public static boolean validateEmail(String email) {
        return !email.trim().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Kiểm tra tất cả các trường
    public static boolean validateAll(String name, String phone, String email) {
        return validateName(name) && validatePhone(phone) && validateEmail(email);
    }
}
