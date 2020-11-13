package com.gwsoftware.alahazratkakalam.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdView;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.io.File;

public class PdfViewActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    PDFView pdfView;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        pdfView = findViewById(R.id.pdfView);
        String pdfName = getIntent().getStringExtra(Constants.PDF_NAME);
        boolean isDisplayAdView = getIntent().getBooleanExtra("is_display_adview", false);
        adView = findViewById(R.id.adView);
        if (isDisplayAdView) {
            Utils.loadAdView(this, adView);
        } else {
            adView.setVisibility(View.GONE);
        }

        if (pdfName != null) {
            String pdfPath = Constants.PDF_FOLDER + pdfName + File.separator + pdfName + ".pdf";
            if (pdfPath != null && new File(pdfPath).exists()) {
                loadPdf(new File(pdfPath));
            }
        }

    }

    private void loadPdf(File pdfFile) {
        pdfView.fromFile(pdfFile)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }
}
