package com.example.hotelas.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
     * Tạo MultipartBody.Part từ một Uri (file image, video, v.v.).
     *
     * @param context   Context để lấy ContentResolver
     * @param paramName Tên tham số trùng với @RequestParam("...")
     * @param uri       Uri của file cần upload
     * @return MultipartBody.Part để dùng với Retrofit
     * @throws IOException nếu đọc file gặp lỗi
     */
    public static MultipartBody.Part createPartFromUri(
            Context context,
            String paramName,
            Uri uri
    ) throws IOException {
        // Xác định MIME type của file
        String mimeType = context.getContentResolver().getType(uri);
        if (mimeType == null) {
            // fallback nếu không lấy được mimeType
            String ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }

        // Đọc toàn bộ bytes từ URI
        InputStream is = context.getContentResolver().openInputStream(uri);
        byte[] bytes = new byte[is.available()];
        int len = is.read(bytes);
        is.close();

        // Xác định filename (timestamp + extension)
        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        String filename = "upload_" + System.currentTimeMillis()
                + (extension != null ? "." + extension : "");

        // Tạo RequestBody và MultipartBody.Part
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), bytes);
        return MultipartBody.Part.createFormData(paramName, filename, requestBody);
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
