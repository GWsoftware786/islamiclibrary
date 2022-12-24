package com.gwsoftware.alahazratkakalam.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.activity.PdfViewActivity;
import com.gwsoftware.alahazratkakalam.asyncktask.DeleteTask;
import com.gwsoftware.alahazratkakalam.asyncktask.GetData;
import com.gwsoftware.alahazratkakalam.interfaces.AlertCallback;
import com.gwsoftware.alahazratkakalam.interfaces.DeleteCallback;
import com.gwsoftware.alahazratkakalam.interfaces.GetLocalData;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CollectionsListAdapter extends RecyclerView.Adapter<CollectionsListAdapter.MyViewHolder> {
    Context context;
    AhApplication ahApplication;
    List<CollectionModel> filesList;

    public CollectionsListAdapter(Context context, List<CollectionModel> filesList) {
        this.context = context;
        ahApplication = AhApplication.getInstance();
        this.filesList = new ArrayList<>();
        this.filesList = filesList;


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.collections_list_item_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final CollectionModel pdf = filesList.get(i);
        myViewHolder.author.setText(pdf.getAuthor());
        myViewHolder.bookName.setText(pdf.getPdf_name());
        myViewHolder.category.setText(pdf.getPdf_category());
        myViewHolder.language.setText(pdf.getLanguage());
        myViewHolder.pages.setText(pdf.getPdf_pages());
        Glide.with(context) //
                .load(pdf.getPdf_thumb())
                .override(250, 350) // resizes the image to these dimensions (in pixel)
                .centerCrop()
                .into(myViewHolder.bookImage);

        myViewHolder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Utils.showAlert(context, "Do You Want To Delete ?", new AlertCallback() {
                    @Override
                    public void alertCallback(boolean value) {
                        if (value) {
                            new DeleteTask(context, pdf, new DeleteCallback() {
                                @Override
                                public void deleteCallback() {
                                    new GetData(context, new GetLocalData() {
                                        @Override
                                        public void getData(List<CollectionModel> tasks) {
                                            File file = new File(Constants.PDF_FOLDER + pdf.getPdf_name());
                                            if (file.exists()) {
                                                Utils.deleteFolder(file);
                                            }

                                            filesList = tasks;
                                            notifyDataSetChanged();


                                        }
                                    }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                }
                            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                    }
                },true,"Yes");


            }
        });

        myViewHolder.openTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataObjectModel.Pdf dataObjectModel = new DataObjectModel().new Pdf();
                dataObjectModel.setAuthor(pdf.getAuthor());
                dataObjectModel.setPdf_thumb(pdf.getPdf_thumb());
                dataObjectModel.setPdf_url(pdf.getPdf_url());
                dataObjectModel.setPdf_pages(pdf.getPdf_pages());
                dataObjectModel.setPdf_name(pdf.getPdf_name());
                Intent intent = new Intent(context, PdfViewActivity.class);
                intent.putExtra(Constants.PDF_NAME, pdf.getPdf_name());
                intent.putExtra("is_display_adview", true);
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView bookImage;
        ImageView deleteImg;
        public TextView author;
        TextView bookName;
        TextView pages;
        TextView language;
        TextView category;
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
            deleteImg = itemView.findViewById(R.id.delete_img);
            openTitle = itemView.findViewById(R.id.open_title);

        }
    }

    public void updateSearchResult(List<CollectionModel> searchList) {
        filesList = searchList;
        this.notifyDataSetChanged();
    }


}