package com.example.hotelas;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hotelas.adapter.AppliedDiscountAdapter;
import com.example.hotelas.config.PrefManager;
import com.example.hotelas.databinding.FragmentCustomerInfoBinding;
import com.example.hotelas.model.common.DiscountDTO;
import com.example.hotelas.model.request.reservation.updateinfo.UpdateReservationInfoRequest;
import com.example.hotelas.model.response.ApiResponse;
import com.example.hotelas.model.response.CreationResponse;
import com.example.hotelas.model.response.CustomerResponseDTO;
import com.example.hotelas.service.callback.ServiceExecutor;
import com.example.hotelas.service.reservation.ReservationService;
import com.example.hotelas.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

public class CustomerInfoFragment extends Fragment implements PaymentActivity.OnNextStepClickListener {

    private FragmentCustomerInfoBinding binding;
    private UserService userService;
    private ReservationService reservationService;
    private CustomerResponseDTO customerInfo;

    String reservationId;


    public CustomerInfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCustomerInfoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        reservationId = ((PaymentActivity) requireActivity()).reservationId;

        // Setup RecyclerView
        binding.appliedDiscountRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<DiscountDTO> appliedDiscounts = new ArrayList<>();
        appliedDiscounts.add(
                DiscountDTO.builder()
                        .name("Summer Sale")
                        .code("SUMMER20")
                        .build()
        );

        AppliedDiscountAdapter adapter = new AppliedDiscountAdapter(appliedDiscounts);
        binding.appliedDiscountRecycler.setAdapter(adapter);

        setCustomerInfo();

        return view;
    }

    private void setCustomerInfo() {
        String token = new PrefManager(requireContext()).getAuthResponse().getAccessToken();
        userService = new UserService(token);

        userService.getCustomerProfile(new ServiceExecutor.CallBack<CustomerResponseDTO>() {
            @Override
            public void onSuccess(ApiResponse<CustomerResponseDTO> result) {
                customerInfo = result.getResult();

                binding.nameText.setText(customerInfo.getName());
                binding.phoneText.setText(customerInfo.getPhone());
                binding.emailText.setText(customerInfo.getEmail());
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        String name = binding.nameText.getText().toString().trim();
        String phone = binding.phoneText.getText().toString().trim();
        String email = binding.emailText.getText().toString().trim();

        // Kiểm tra name
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập tên khách hàng", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra phone
        if (phone.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(requireContext(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra email
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Email không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onNextStep() {
        if (!validate())
            return;

        String token = new PrefManager(requireContext()).getAuthResponse().getAccessToken();
        reservationService = new ReservationService(token);


        UpdateReservationInfoRequest request = UpdateReservationInfoRequest.builder()
                .email(binding.emailText.getText().toString())
                .name(binding.nameText.getText().toString())
                .phone(binding.phoneText.getText().toString())
                .reservationId(reservationId)
                .build();

        reservationService.updateCustomerInfoReservation(request, new ServiceExecutor.CallBack<CreationResponse>() {
            @Override
            public void onSuccess(ApiResponse<CreationResponse> result) {
                Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                ((PaymentActivity)requireContext()).nextStep(1); // -> chuyển đến step tiếp theo -> thanh toán
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

