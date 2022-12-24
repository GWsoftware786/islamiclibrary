package com.gwsoftware.alahazratkakalam.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.activity.PdfViewActivity;
import com.gwsoftware.alahazratkakalam.asyncktask.DownloadAnyFile;
import com.gwsoftware.alahazratkakalam.asyncktask.SaveImageFromUrl;
import com.gwsoftware.alahazratkakalam.asyncktask.SaveTask;
import com.gwsoftware.alahazratkakalam.interfaces.AlertCallback;
import com.gwsoftware.alahazratkakalam.interfaces.DownloadAnyFileCallback;
import com.gwsoftware.alahazratkakalam.interfaces.SaveImageCallback;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.io.File;
import java.util.ArrayList;


public class QuranViewAdapter extends RecyclerView.Adapter<QuranViewAdapter.MyViewHolder> {
    Context context;
    AhApplication ahApplication;
    ArrayList<DataObjectModel.Quraan> filesList;

    public QuranViewAdapter(Context context, ArrayList<DataObjectModel.Quraan> filesList) {
        this.context = context;
        ahApplication = AhApplication.getInstance();
        this.filesList = new ArrayList<>();
        this.filesList = filesList;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quran_list_item_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        //ArrayList<DataObjectModel.Pdf> pdfist = ahApplication.getDataObjectModel().getPdfs();

        final DataObjectModel.Quraan pdf = filesList.get(i);
        myViewHolder.paraName.setText(pdf.getPara_name());
        myViewHolder.paraNumber.setText("Para " + pdf.getPara_number());
        myViewHolder.pageCount.setText(pdf.getPdf_pages());

        myViewHolder.pdfSize.setText(pdf.getPara_size());
        if (new File(Constants.PDF_FOLDER + pdf.getPara_name() + File.separator + pdf.getPara_name() + ".png").exists()) {
            myViewHolder.openTitle.setVisibility(View.VISIBLE);
            myViewHolder.downloadImg.setVisibility(View.GONE);
        } else {
            myViewHolder.openTitle.setVisibility(View.GONE);
            myViewHolder.downloadImg.setVisibility(View.VISIBLE);
        }

        Uri myUri = Uri.parse(pdf.getThumbImage());
        Glide.with(context) //
                .load(myUri)
                .override(250, 350) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(myViewHolder.bookImage);
        myViewHolder.downloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Utils.showAlert(context, "Do You Want To Download ?", new AlertCallback() {
                    @Override
                    public void alertCallback(boolean value) {
                        if (value) {
                            if (Utils.isNetworkConnected(context)) {
                                if (Utils.checkWriteExternalPermission(context)) {
                                    if (!new File(Constants.PDF_FOLDER).exists()) {
                                        new File(Constants.PDF_FOLDER).mkdirs();
                                    }
                                    new DownloadAnyFile(context, Constants.PDF_FOLDER + "/" + pdf.getPara_name(), pdf.getDownloadIrl(),".pdf",pdf.getPara_name(), new DownloadAnyFileCallback() {

                                        @Override
                                        public void downloadCallback() {
                                            myViewHolder.openTitle.setVisibility(View.VISIBLE);
                                            myViewHolder.downloadImg.setVisibility(View.GONE);

                                            new SaveImageFromUrl(context, pdf.getThumbImage(), pdf.getPara_name(), new SaveImageCallback() {
                                                @Override
                                                public void imageSaveCallback(String imagePath) {
                                                    CollectionModel collectionModel = new CollectionModel();
                                                    collectionModel.setAuthor("NA");
                                                    collectionModel.setLanguage("Arabic");
                                                    collectionModel.setPdf_category(pdf.getPdf_category());
                                                    collectionModel.setPdf_pages(pdf.getPdf_pages());
                                                    collectionModel.setPdf_name(pdf.getPara_name());
                                                    collectionModel.setPdf_size(pdf.getPara_size());
                                                    collectionModel.setPdf_thumb(imagePath);
                                                    String url = Constants.PDF_FOLDER + pdf.getPara_name() + "/" + pdf.getPara_name() + ".pdf";
                                                    collectionModel.setPdf_url(url);
                                                    SaveTask saveTask = new SaveTask(context, collectionModel);
                                                    saveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                                }
                                            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                                        }
                                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                            } else {
                                Toast.makeText(context, "Internet Connection Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }, true, "Yes");


            }
        });
        myViewHolder.openTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdfIntent = new Intent(context, PdfViewActivity.class);
                pdfIntent.putExtra(Constants.PDF_NAME, pdf.getPara_name());
                pdfIntent.putExtra("is_display_adview", false);
                context.startActivity(pdfIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;
        ImageView downloadImg;
        TextView paraName;
        TextView paraNumber;
        ImageView openTitle;
        TextView pdfSize;
        TextView pageCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            paraName = itemView.findViewById(R.id.para_name);
            paraNumber = itemView.findViewById(R.id.para_number);
            bookImage = itemView.findViewById(R.id.book_image);
            downloadImg = itemView.findViewById(R.id.download_img);
            openTitle = itemView.findViewById(R.id.open_title);
            pdfSize = itemView.findViewById(R.id.pdf_size);
            pageCount = itemView.findViewById(R.id.pdf_pages);

        }
    }

    public void updateSearchResult(ArrayList<DataObjectModel.Quraan> searchList) {
        filesList = searchList;
        this.notifyDataSetChanged();
    }


}