package com.gwsoftware.alahazratkakalam.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.adapter.CollectionsListAdapter;
import com.gwsoftware.alahazratkakalam.asyncktask.GetData;
import com.gwsoftware.alahazratkakalam.interfaces.GetLocalData;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;

import java.util.List;

public class DownloadedBooks extends Fragment {
    RecyclerView collectionRecyclerView;
    CollectionsListAdapter collectionsListAdapter;
    TextView noDownloadsFound;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view = inflater.inflate(R.layout.downloaded_books, container, false);
        collectionRecyclerView = view.findViewById(R.id.collectionRecyclerView);
        noDownloadsFound = view.findViewById(R.id.no_downloads_found);
        initializeRecyclerView();
        getCollectionsList();
        return view;
    }
    private void initializeRecyclerView() {
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        collectionRecyclerView.setLayoutManager(gridLayoutManager);


    }
    private void getCollectionsList() {
        new GetData(getContext(), new GetLocalData() {
            @Override
            public void getData(List<CollectionModel> tasks) {
                if (tasks != null && tasks.size() > 0) {
                    noDownloadsFound.setVisibility(View.GONE);
                    collectionRecyclerView.setVisibility(View.VISIBLE);
                    collectionsListAdapter = new CollectionsListAdapter(getContext(), tasks);
                    collectionRecyclerView.setAdapter(collectionsListAdapter);
                } else {
                    noDownloadsFound.setVisibility(View.VISIBLE);
                    collectionRecyclerView.setVisibility(View.GONE);
                }

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}