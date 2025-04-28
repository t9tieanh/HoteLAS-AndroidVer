package com.example.hotelas.service.user;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.hotelas.config.RetrofitClient;
import com.example.hotelas.model.request.user.CustomerRequestDTO;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.utils.ImageUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class UserService {

    private final UserAPIService apiService;

    public UserService(String token) {
        apiService = RetrofitClient.getRetrofitWithAuth(token).create(UserAPIService.class);
    }

    public void getCustomerProfile(ServiceExecutor.CallBack<CustomerResponseDTO> callback) {
        Call<ApiResponse<CustomerResponseDTO>> call = apiService.getCustomerProfile();
        ServiceExecutor.enqueue(call, callback);
    }


    // active account
    public void activeAccount (
            Context context,
            String username,
            String password,
            String name,
            String email,
            Bitmap img,
            ServiceExecutor.CallBack<Boolean> callback
    ) {

        // Tạo các RequestBody từ thông tin người dùng
        RequestBody usernameRequest = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody passwordRequest = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody nameRequest = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody emailRequest = RequestBody.create(MediaType.parse("text/plain"), email);

        // Gọi API của Retrofit với các tham số đã chuẩn bị
        Call<ApiResponse<Boolean>> call = apiService.activeAccount(
                usernameRequest,
                passwordRequest,
                nameRequest,
                emailRequest,
                getUserImage(img, context)
        );

        // Xử lý kết quả bằng ServiceExecutor
        ServiceExecutor.enqueue(call, callback);
    }


    // update image user
    public void updateUserImage (
            Context context,
            MultipartBody.Part img,
            ServiceExecutor.CallBack<CreationResponse> callback
    ) {

        // Gọi API của Retrofit với các tham số đã chuẩn bị
        Call<ApiResponse<CreationResponse>> call = apiService.updateImage(
                img
        );

        // Xử lý kết quả bằng ServiceExecutor
        ServiceExecutor.enqueue(call, callback);
    }

    // update image user
    public void editCustomerProfile (
            CustomerRequestDTO request,
            ServiceExecutor.CallBack<CustomerResponseDTO> callback
    ) {
        // Gọi API của Retrofit với các tham số đã chuẩn bị
        Call<ApiResponse<CustomerResponseDTO>> call
                = apiService.editCustomerProfile(request.getName(), request.getPhone(), request.getEmail());

        // Xử lý kết quả bằng ServiceExecutor
        ServiceExecutor.enqueue(call, callback);
    }

    private MultipartBody.Part getUserImage (Bitmap bitmap, Context context) {

        // Chuyển Bitmap thành File
        File imgFile = null;
        try {
            imgFile = ImageUtils.bitmapToFile(bitmap, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ImageUtils.createImagePart(imgFile);
    }
}
