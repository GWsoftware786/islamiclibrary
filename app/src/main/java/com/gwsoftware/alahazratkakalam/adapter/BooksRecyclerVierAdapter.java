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
import com.gwsoftware.alahazratkakalam.asyncktask.DownloadFile;
import com.gwsoftware.alahazratkakalam.asyncktask.SaveImageFromUrl;
import com.gwsoftware.alahazratkakalam.asyncktask.SaveTask;
import com.gwsoftware.alahazratkakalam.interfaces.AlertCallback;
import com.gwsoftware.alahazratkakalam.interfaces.DownloadCallback;
import com.gwsoftware.alahazratkakalam.interfaces.SaveImageCallback;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.io.File;
import java.util.ArrayList;


public class BooksRecyclerVierAdapter extends RecyclerView.Adapter<BooksRecyclerVierAdapter.MyViewHolder> {
    Context context;
    AhApplication ahApplication;
    ArrayList<DataObjectModel.Pdf> filesList;

    public BooksRecyclerVierAdapter(Context context, ArrayList<DataObjectModel.Pdf> filesList) {
        this.context = context;
        ahApplication = AhApplication.getInstance();
        this.filesList = new ArrayList<>();
        this.filesList = filesList;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_list_item_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        //ArrayList<DataObjectModel.Pdf> pdfist = ahApplication.getDataObjectModel().getPdfs();

        final DataObjectModel.Pdf pdf = filesList.get(i);
        myViewHolder.author.setText(pdf.getAuthor());
        myViewHolder.bookName.setText(pdf.getPdf_name());
        myViewHolder.category.setText(pdf.getPdf_category());
        myViewHolder.language.setText(pdf.getLanguage());
        myViewHolder.pages.setText(pdf.getPdf_pages());
        myViewHolder.fileSize.setText(pdf.getPdf_size());

        if (new File(Constants.PDF_FOLDER + pdf.getPdf_name() + File.separator + pdf.getPdf_name() + ".png").exists()) {
            myViewHolder.openTitle.setVisibility(View.VISIBLE);
            myViewHolder.downloadImg.setVisibility(View.GONE);
        } else {
            myViewHolder.openTitle.setVisibility(View.GONE);
            myViewHolder.downloadImg.setVisibility(View.VISIBLE);
        }

        Uri myUri = Uri.parse(pdf.getPdf_thumb());
        Glide.with(context) //
                .load(myUri)
                .override(250, 350) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(myViewHolder.bookImage);
        myViewHolder.downloadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkConnected(context)) {
                    Utils.showAlert(context, "Do You Want To Download ?", new AlertCallback() {
                        @Override
                        public void alertCallback(boolean value) {
                            if (value) {
                                if (Utils.checkWriteExternalPermission(context)) {
                                    if (!new File(Constants.PDF_FOLDER).exists()) {
                                        new File(Constants.PDF_FOLDER).mkdirs();
                                    }
                                    new DownloadFile(context, pdf, new DownloadCallback() {
                                        @Override
                                        public void downloadCompleted(final DataObjectModel.Pdf pdf, final String destinationFolderPath) {
                                            myViewHolder.openTitle.setVisibility(View.VISIBLE);
                                            myViewHolder.downloadImg.setVisibility(View.GONE);

                                            new SaveImageFromUrl(context, pdf.getPdf_thumb(), pdf.getPdf_name(), new SaveImageCallback() {
                                                @Override
                                                public void imageSaveCallback(String imagePath) {
                                                    CollectionModel collectionModel = new CollectionModel();
                                                    collectionModel.setAuthor(pdf.getAuthor());
                                                    collectionModel.setLanguage(pdf.getLanguage());
                                                    collectionModel.setPdf_category(pdf.getPdf_category());
                                                    collectionModel.setPdf_pages(pdf.getPdf_pages());
                                                    collectionModel.setPdf_name(pdf.getPdf_name());
                                                    collectionModel.setPdf_size(pdf.getPdf_size());
                                                    collectionModel.setPdf_thumb(imagePath);
                                                    collectionModel.setPdf_url(destinationFolderPath + "/" + pdf.getPdf_name() + ".pdf");
                                                    SaveTask saveTask = new SaveTask(context, collectionModel);
                                                    saveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                                                }
                                            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                                        }
                                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                } else {
                                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                }
                            }
                        }
                    }, true, "Yes");


                } else {
                    Toast.makeText(context, "Internet Connection Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        myViewHolder.openTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pdfIntent = new Intent(context, PdfViewActivity.class);
                pdfIntent.putExtra(Constants.PDF_NAME, pdf.getPdf_name());
                pdfIntent.putExtra("is_display_adview",true);
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
        public TextView author;
        TextView bookName;
        TextView pages;
        TextView language;
        TextView category;
        TextView fileSize;
        ImageView openTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            bookName = itemView.findViewById(R.id.book_name);
            author = itemView.findViewById(R.id.author_name);
            bookImage = itemView.findViewById(R.id.book_image);
            pages = itemView.findViewById(R.id.pages);
            language = itemView.findViewById(R.id.language);
            category = itemView.findViewById(R.id.category);
            downloadImg = itemView.findViewById(R.id.download_img);
            openTitle = itemView.findViewById(R.id.open_title);
            fileSize = itemView.findViewById(R.id.size);

        }
    }

    public void updateSearchResult(ArrayList<DataObjectModel.Pdf> searchList) {
        filesList = searchList;
        this.notifyDataSetChanged();
    }


}