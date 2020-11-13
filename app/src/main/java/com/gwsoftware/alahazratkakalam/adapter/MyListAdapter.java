package com.gwsoftware.alahazratkakalam.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gwsoftware.alahazratkakalam.R;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    DataObjectModel dataObjectModel;

    public MyListAdapter(Activity context, DataObjectModel dataObjectModel) {
        super(context, R.layout.my_list_item);
        this.context = context;
        this.dataObjectModel = dataObjectModel;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.my_list_item, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);

        //titleText.setText(dataObjectModel.getPdfs().get(position).getAuthor());
       // Glide.with(context).load(dataObjectModel.getPdfs().get(position).getPdf_url()).into(imageView);

        return rowView;

    }


}