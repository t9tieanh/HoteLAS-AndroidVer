package com.example.hotelas.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hotelas.model.response.AuthenticationResponse;
import com.google.gson.Gson;

public class PrefManager {
    private static final String PREF_NAME = "my_app_prefs";
    private static final String KEY_AUTH_RESPONSE = "auth_response";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public PrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void saveAuthResponse(AuthenticationResponse authResponse) {
        String json = gson.toJson(authResponse);
        editor.putString(KEY_AUTH_RESPONSE, json);
        editor.apply();
    }

    public AuthenticationResponse getAuthResponse() {
        String json = sharedPreferences.getString(KEY_AUTH_RESPONSE, null);
        if (json != null) {
            return gson.fromJson(json, AuthenticationResponse.class);
        }
        return null;
    }

    public void clearAuthResponse() {
        editor.remove(KEY_AUTH_RESPONSE);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getAuthResponse() != null;
    }
}
