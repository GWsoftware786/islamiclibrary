package com.gwsoftware.alahazratkakalam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.adapter.QuranViewAdapter;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.util.ArrayList;

public class QuranActivity extends AppCompatActivity {

    RecyclerView quranRecyclerView;
    QuranViewAdapter quranViewAdapter;
    AhApplication ahApplication;
    private ArrayList<DataObjectModel.Quraan> filesList;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran);
        adView = findViewById(R.id.adView);
        Utils.loadAdView(this,adView);
        ahApplication = AhApplication.getInstance();
        quranRecyclerView = findViewById(R.id.quranRecyclerView);
        initializeQuranRecyclerView();

    }

    private void initializeQuranRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QuranActivity.this);
        quranRecyclerView.setLayoutManager(linearLayoutManager);
        quranViewAdapter = new QuranViewAdapter(QuranActivity.this, getFilesList());
        quranRecyclerView.setAdapter(quranViewAdapter);
    }

    private ArrayList<DataObjectModel.Quraan> getFilesList() {
        if (ahApplication.getDataObjectModel() != null && ahApplication.getDataObjectModel().getQuraan() != null) {
            filesList = ahApplication.getDataObjectModel().getQuraan();
        }
        return filesList;
    }
}
