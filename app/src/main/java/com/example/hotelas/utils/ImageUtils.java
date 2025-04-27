package com.example.hotelas.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImageUtils {

    /**
     * Lấy Bitmap từ ImageView
     * @param imageView ImageView chứa ảnh cần lấy
     * @return Bitmap của ảnh
     */
    public static Bitmap getBitmapFromImageView(ImageView imageView) {
        if (imageView.getDrawable() instanceof BitmapDrawable) {
            return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        }
        return null; // Trả về null nếu ImageView không chứa một BitmapDrawable
    }

    /**
     * Chuyển Bitmap thành File tạm thời
     */
    public static File bitmapToFile(Bitmap bitmap, Context context) throws IOException {
        // Tạo file tạm thời trong bộ nhớ của ứng dụng
        File file = new File(context.getCacheDir(), "image.jpg");

        // Ghi Bitmap vào file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        }

        return file;
    }

    /**
     * Chuyển File thành MultipartBody.Part để gửi qua Retrofit
     */
    public static MultipartBody.Part createImagePart(File imgFile) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imgFile);
        return MultipartBody.Part.createFormData("img", imgFile.getName(), requestFile);
    }

    /**
     * Tạo RequestBody từ String (dùng cho các tham số khác ngoài ảnh)
     */
    public static RequestBody createRequestBody(String value) {
        return RequestBody.create(MediaType.parse("text/plain"), value);
    }
}
