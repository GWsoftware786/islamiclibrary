package com.gwsoftware.alahazratkakalam.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.adapter.HomeRecyclerViewAdapter;
import com.gwsoftware.alahazratkakalam.utils.GridSpacingItemDecoration;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        initializeRecyclerView();
        return view;
    }

    private void initializeRecyclerView() {
       // LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        //recyclerView.setLayoutManager(gridLayoutManager);
        int spacing = 10; // 50px
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, includeEdge));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

        //AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getContext(), 500);
        recyclerView.setLayoutManager(gridLayoutManager);


        HomeRecyclerViewAdapter customAdapter = new HomeRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(customAdapter);
    }
}