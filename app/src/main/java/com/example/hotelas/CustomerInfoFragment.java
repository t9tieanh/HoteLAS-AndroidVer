package com.example.hotelas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.adapter.AppliedDiscountAdapter;
import com.example.hotelas.adapter.RoomSelectedAdapter;
import com.example.hotelas.model.common.AddressDTO;
import com.example.hotelas.model.common.DiscountDTO;
import com.example.hotelas.model.response.reservation.common.ReservationDetailResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class CustomerInfoFragment extends Fragment {
    private TextInputEditText edtFullName, edtPhone, edtEmail;

    public CustomerInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_info, container, false);

        // Sửa lại findViewById
//        RecyclerView recyclerView = view.findViewById(R.id.selectedRoomsRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//
//        // Dữ liệu ảo
//        List<ReservationDetailResponse> selectedRooms = new ArrayList<>();
//
//        selectedRooms.add(ReservationDetailResponse.builder()
//                .hotelName("Aurora Palace")
//                .address(new AddressDTO("45 Đ. Nguyễn Tất Thành", "Quận 7", "Hồ Chí Minh", "Việt Nam"))
//                .hotelImgUrl("https://example.com/hotel.jpg")
//                .name("Basic Room")
//                .description("Phòng Deluxe Ocean View mang đến không gian sang trọng và thoải mái với diện tích rộng rãi, thiết kế hiện đại.")
//                .imgUrl("https://example.com/room1.jpg")
//                .quantity(2L)
//                .price(1200000.0)
//                .build());
//
//        selectedRooms.add(ReservationDetailResponse.builder()
//                .hotelName("Aurora Palace")
//                .address(new AddressDTO("45 Đ. Nguyễn Tất Thành", "Quận 7", "Hồ Chí Minh", "Việt Nam"))
//                .hotelImgUrl("https://example.com/hotel.jpg")
//                .name("Suite Room")
//                .description("Phòng cao cấp với view biển, giường lớn, nội thất hiện đại và bồn tắm riêng.")
//                .imgUrl("https://example.com/room2.jpg")
//                .quantity(1L)
//                .price(2200000.0)
//                .build());
//
//        // Sửa lại context khi truyền vào Adapter
//        RoomSelectedAdapter adapter = new RoomSelectedAdapter(requireContext(), selectedRooms);
//        recyclerView.setAdapter(adapter);


        RecyclerView recyclerView = view.findViewById(R.id.appliedDiscountRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        List<DiscountDTO> appliedDiscounts = new ArrayList<>();
// Thêm các discount đã apply vào đây
        appliedDiscounts.add(
                DiscountDTO.builder()
                        .name("Summer Sale")
                        .code("SUMMER20")
                        .build()
        );

        AppliedDiscountAdapter adapter = new AppliedDiscountAdapter(appliedDiscounts);
        recyclerView.setAdapter(adapter);


        return view;
    }

}
