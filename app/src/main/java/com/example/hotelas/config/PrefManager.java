package com.example.hotelas.config;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "LoginDetails";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_FULL_NAME = "FullName";
    private static final String KEY_PICTURE = "Picture";
    private static final String KEY_LOGGED_IN = "IsLoggedIn";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void saveLoginDetails(String fullName, String email, String picture) {
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PICTURE, picture);
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, "");
    }

    public String getPicture() {
        return sharedPreferences.getString(KEY_PICTURE, "");
    }

    public boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}