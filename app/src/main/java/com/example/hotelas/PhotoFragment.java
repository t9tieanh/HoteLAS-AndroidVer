package com.example.hotelas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hotelas.adapter.PhotoAdapter;
import com.example.hotelas.enums.HotelImageEnum;

import java.util.ArrayList;
import java.util.List;

public class PhotoFragment extends Fragment {
    private static final String ARG_IMAGE_TYPE = "imageType";
    private List<String> imageUrls = new ArrayList<>();
    private boolean isLoaded = false;
    private RecyclerView recyclerView;
    private PhotoAdapter adapter;

    public static PhotoFragment newInstance(String imageTypeName) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_TYPE, imageTypeName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new PhotoAdapter(requireContext(),imageUrls);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void updateImages(List<String> newImages) {

        if (isLoaded)
            return; // đã fetch dữ liệu -> không cần fetch lại

        imageUrls.clear();
        imageUrls.addAll(newImages);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            isLoaded = true;
        }
    }
}


