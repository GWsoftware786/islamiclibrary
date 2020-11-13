package com.gwsoftware.alahazratkakalam.asyncktask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gwsoftware.alahazratkakalam.db.DatabaseClient;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;

public class SaveTask extends AsyncTask<Void, Void, Void> {
    CollectionModel collectionModel;
    Context context;

    public SaveTask(Context context, CollectionModel collectionModel) {
        this.context = context;
        this.collectionModel = collectionModel;
    }


    @Override
    protected Void doInBackground(Void... voids) {


        //adding to database
        DatabaseClient.getInstance(context).getAppDatabase()
                .taskDao()
                .insert(collectionModel);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
    }
}

