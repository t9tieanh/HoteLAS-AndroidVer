package com.example.hotelas.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hotelas.adapter.AddressAdapter;
import com.example.hotelas.databinding.ActivityLocationDialogBinding;
import com.example.hotelas.model.common.Address;

import java.util.ArrayList;
import java.util.List;

public class LocationDialogActivity extends AppCompatActivity {

    private ActivityLocationDialogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ẩn ActionBar (nếu đang dùng AppCompatActivity)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hiển thị nút back trên ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = binding.locationInputEditText.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("location", location);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        // adapter
        List<Address> list = new ArrayList<>();list.add(new Address("Hà Nội, Việt Nam"));
        list.add(new Address("Đà Nẵng, Việt Nam"));
        list.add(new Address("Hồ Chí Minh, Việt Nam"));
        list.add(new Address("Nha Trang, Việt Nam"));
        list.add(new Address("Hội An, Việt Nam"));
        list.add(new Address("Huế, Việt Nam"));
        binding.locationSuggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AddressAdapter adapter = new AddressAdapter(list, address -> {
            binding.locationInputEditText.setText(address.getAddress());
        });
        binding.locationSuggestionsRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
