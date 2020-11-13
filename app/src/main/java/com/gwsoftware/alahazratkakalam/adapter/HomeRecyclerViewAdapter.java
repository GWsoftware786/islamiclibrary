package com.gwsoftware.alahazratkakalam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.activity.BooksListActivity;
import com.gwsoftware.alahazratkakalam.interfaces.recyclerViewCallback;
import com.gwsoftware.alahazratkakalam.utils.AhApplication;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.util.ArrayList;
import java.util.Random;


public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.MyViewHolder> {
    Context context;
    AhApplication ahApplication;
    recyclerViewCallback recyclerViewCallback;
    public HomeRecyclerViewAdapter(Context context) {
        this.context = context;
        this.recyclerViewCallback = (com.gwsoftware.alahazratkakalam.interfaces.recyclerViewCallback) context;
        ahApplication = AhApplication.getInstance();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recycler_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        final ArrayList<String> categories = ahApplication.getDataObjectModel().getCategories();
        //Random rnd = new Random();
       // int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
       // myViewHolder.parentLayout.setBackgroundColor(color);
        myViewHolder.title.setText(categories.get(i));

        myViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerViewCallback!=null){
                    v.setTag(categories.get(i));
                    recyclerViewCallback.recyclerViewItemClicked(i,v);
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return (ahApplication.getDataObjectModel() != null && ahApplication.getDataObjectModel().getCategories() != null ? ahApplication.getDataObjectModel().getCategories().size() : 0);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout parentLayout;
        public TextView title;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            title = itemView.findViewById(R.id.name);
            parentLayout = itemView.findViewById(R.id.item_parent);
            cardView = itemView.findViewById(R.id.card_view);
            RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.width = (int) (Utils.getScreenWidth()/2.1);
            layoutParams.height = (int) (Utils.getScreenWidth()/3);
        }
    }
}