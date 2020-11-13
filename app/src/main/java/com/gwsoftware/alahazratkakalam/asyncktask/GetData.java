package com.gwsoftware.alahazratkakalam.asyncktask;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.gwsoftware.alahazratkakalam.db.DatabaseClient;
import com.gwsoftware.alahazratkakalam.interfaces.GetLocalData;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;

import java.util.List;

public class GetData extends AsyncTask<Void, Void, List<CollectionModel>> {

    Context context;
    GetLocalData getLocalData;
    ProgressDialog progressDialog;

    public GetData(Context context, GetLocalData getLocalData) {
        this.context = context;
        this.getLocalData = getLocalData;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    @Override
    protected List<CollectionModel> doInBackground(Void... voids) {
        List<CollectionModel> taskList = DatabaseClient
                .getInstance(context)
                .getAppDatabase()
                .taskDao()
                .getAll();
        return taskList;
    }

    @Override
    protected void onPostExecute(List<CollectionModel> tasks) {
        super.onPostExecute(tasks);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (getLocalData != null) {
            getLocalData.getData(tasks);
        }
        /*TasksAdapter adapter = new TasksAdapter(MainActivity.this, tasks);
        recyclerView.setAdapter(adapter);*/
    }
}
