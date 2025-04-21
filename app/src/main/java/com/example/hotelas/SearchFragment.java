package com.example.hotelas;
import android.app.Activity;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hotelas.databinding.FragmentSearchBinding;
import com.example.hotelas.dialog.GuestPickerDialog;
import com.example.hotelas.dialog.LocationDialogActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchFragment extends Fragment implements GuestPickerDialog.GuestPickerListener {

    private FragmentSearchBinding binding;
    private MaterialButton locationButton;
    private MaterialButton datepickerButton;
    private MaterialButton guestpickerButton;
    private MaterialButton searchButton;
    private String checkInDate;
    private String checkOutDate;
    private int adultsCount = 0;
    private int childsCount = 0;
    private int checkInDay = 0;
    private int checkInMonth = 0;
    private int checkInYear = 0;
    private int checkOutDay = 0;
    private int checkOutMonth = 0;
    private int checkOutYear = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        requireActivity().setTitle("Search");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationButton = binding.locationButton;
        datepickerButton = binding.datepickerButton;
        guestpickerButton = binding.guestpickerButton;
        searchButton = binding.searchButton;

        locationButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LocationDialogActivity.class);
            startActivityForResult(intent, LOCATION_DIALOG_REQUEST_CODE);
        });

        datepickerButton.setOnClickListener(v -> showDatePickerDialog());

        guestpickerButton.setOnClickListener(v -> {
            GuestPickerDialog guestPickerDialog = new GuestPickerDialog();
            guestPickerDialog.setGuestPickerListener(this);
            guestPickerDialog.show(getParentFragmentManager(), "GuestPickerDialog");
        });

        searchButton.setOnClickListener(v -> {
            String location = locationButton.getText().toString();
            Intent intent = new Intent(requireContext(), SearchResultActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("adultsCount", adultsCount);
            intent.putExtra("childsCount", childsCount);
            intent.putExtra("checkInDay", checkInDay);
            intent.putExtra("checkInMonth", checkInMonth);
            intent.putExtra("checkInYear", checkInYear);
            intent.putExtra("checkOutDay", checkOutDay);
            intent.putExtra("checkOutMonth", checkOutMonth);
            intent.putExtra("checkOutYear", checkOutYear);
            intent.putExtra("checkInDate", checkInDate);
            intent.putExtra("checkOutDate", checkOutDate);
            startActivity(intent);
        });
    }

    private void showDatePickerDialog() {
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select dates");
        builder.setSelection(androidx.core.util.Pair.create(
                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
        ));
        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> picker = builder.build();

        picker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            Long startDate = selection.first;
            Long endDate = selection.second;

            checkInDate = dateFormat.format(new Date(startDate));
            checkOutDate = dateFormat.format(new Date(endDate));

            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(startDate);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(endDate);

            checkInDay = startCalendar.get(Calendar.DAY_OF_MONTH);
            checkInMonth = startCalendar.get(Calendar.MONTH);
            checkInYear = startCalendar.get(Calendar.YEAR);

            checkOutDay = endCalendar.get(Calendar.DAY_OF_MONTH);
            checkOutMonth = endCalendar.get(Calendar.MONTH);
            checkOutYear = endCalendar.get(Calendar.YEAR);

            String selectedDatesText = " " + checkInDate + " - " + checkOutDate;
            datepickerButton.setText(selectedDatesText);
        });

        picker.show(getChildFragmentManager(), "dateRangePicker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_DIALOG_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String location = data.getStringExtra("location");
                if (location != null) {
                    locationButton.setText(location);
                }
            }
        }
    }

    @Override
    public void onGuestCountsSelected(int adultsCount, int childsCount) {
        this.adultsCount = adultsCount;
        this.childsCount = childsCount;
        updateGuestPickerButtonText();
    }

    private void updateGuestPickerButtonText() {
        String guestText = adultsCount + " Adults, " + childsCount + " Kids";
        guestpickerButton.setText(guestText);
    }

    private static final int LOCATION_DIALOG_REQUEST_CODE = 1;
}