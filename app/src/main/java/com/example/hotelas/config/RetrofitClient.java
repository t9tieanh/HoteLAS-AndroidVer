package com.example.hotelas.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitClient {
//    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:8080";

    private static Retrofit retrofitWithoutAuth;
    private static Retrofit retrofitWithAuth;

    public static Retrofit getRetrofitInstance() {
        if (retrofitWithoutAuth == null) {
            retrofitWithoutAuth = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitWithoutAuth;
    }

    // Retrofit có token (reservation, user info...)
    public static Retrofit getRetrofitWithAuth(String token) {
        // luôn tạo mới nếu token thay đổi
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(token))
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Đảm bảo Jackson xử lý LocalDate
        objectMapper.findAndRegisterModules();

        retrofitWithAuth = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        return retrofitWithAuth;
    }
}
