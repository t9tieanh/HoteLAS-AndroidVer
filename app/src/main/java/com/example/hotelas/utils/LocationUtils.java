package com.example.hotelas.utils;

import android.content.Context;

import com.example.hotelas.model.common.Address;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationUtils {
    public static List<Address> readAddressList(Context context, String fileName) {
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");

            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> rawList = new Gson().fromJson(reader, listType);
            reader.close();

            // Convert to List<Address>
            List<Address> result = new ArrayList<>();
            for (String addr : rawList) {
                result.add(new Address(addr));
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi đọc JSON: " + e.getMessage());
        }
    }
}
