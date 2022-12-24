package com.gwsoftware.alahazratkakalam.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.util.ArrayList;

public class VolumeAdapter extends RecyclerView.Adapter<VolumeAdapter.MyViewHolder> {
    Context context;
    AhApplication ahApplication;
    ArrayList<String> categoriesList;
    com.gwsoftware.alahazratkakalam.interfaces.recyclerViewCallback recyclerViewCallback;

    public VolumeAdapter(Context context, ArrayList<String> categoriesList) {
        this.context = context;
        ahApplication = AhApplication.getInstance();
        this.categoriesList = new ArrayList<>();
        this.categoriesList = categoriesList;
        this.recyclerViewCallback = (com.gwsoftware.alahazratkakalam.interfaces.recyclerViewCallback) context;


    }


    @Override
    public VolumeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.books_category_list_item_view, parent, false);
        VolumeAdapter.MyViewHolder vh = new VolumeAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final VolumeAdapter.MyViewHolder myViewHolder, final int i) {

        final String categoryTitle = categoriesList.get(i);
        myViewHolder.categoryTitle.setText(categoryTitle);

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewCallback != null) {
                    v.setTag(categoryTitle);
                    recyclerViewCallback.recyclerViewItemClicked(i, v);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTitle;
        CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.category_title);
            cardView = itemView.findViewById(R.id.card_view);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.width = (int) (Utils.getScreenWidth() / 2.1);
            layoutParams.height = (int) (Utils.getScreenWidth() / 3);

        }
    }

    public void updateSearchResult(ArrayList<String> categoriesList) {
        this.categoriesList = categoriesList;
        this.notifyDataSetChanged();
    }


}