package com.example.hotelas.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hotelas.adapter.AddressAdapter;
import com.example.hotelas.databinding.ActivityLocationDialogBinding;
import com.example.hotelas.model.common.Address;
import com.example.hotelas.utils.LocationUtils;

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

        // adapter (lấy list từ file json)
        List<Address> list = LocationUtils.readAddressList(LocationDialogActivity.this, "locations.json");

        binding.locationSuggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        AddressAdapter adapter = new AddressAdapter(list, address -> {
            binding.locationInputEditText.setText(address.getAddress());
        });
        binding.locationSuggestionsRecyclerView.setAdapter(adapter);

        binding.locationInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s); // Lọc gần đúng
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
