package com.gwsoftware.alahazratkakalam.asyncktask;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.gwsoftware.alahazratkakalam.db.DatabaseClient;
import com.gwsoftware.alahazratkakalam.interfaces.DeleteCallback;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;

public class DeleteTask extends AsyncTask<Void, Void, Void> {
    Context context;
    CollectionModel pdf;
    DeleteCallback deleteCallback;
    ProgressDialog progressDialog;

    public DeleteTask(Context context, CollectionModel pdf, DeleteCallback deleteCallback) {
        this.context = context;
        this.pdf = pdf;
        this.deleteCallback = deleteCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Downloading Book");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseClient.getInstance(context).getAppDatabase()
                .taskDao()
                .delete(pdf);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (deleteCallback != null) {
            deleteCallback.deleteCallback();
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show();

    }
}
