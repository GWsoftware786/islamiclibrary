package com.gwsoftware.alahazratkakalam.asyncktask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gwsoftware.alahazratkakalam.interfaces.DownloadCallback;
import com.gwsoftware.alahazratkakalam.models.DataObjectModel;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadFile extends AsyncTask<Void, Integer, Void> {

    File apkStorage = null;
    File outputFile = null;
    // String filePath;

    ProgressDialog progressDialog;
    Context context;
    DownloadCallback downloadCallback;
    DataObjectModel.Pdf pdf;
    String folderPath;

    public DownloadFile(Context context, DataObjectModel.Pdf pdf, DownloadCallback downloadCallback) {
        this.context = context;
        this.downloadCallback = downloadCallback;
        this.pdf = pdf;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        folderPath = Constants.PDF_FOLDER + pdf.getPdf_name();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Downloading Book");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (downloadCallback != null) {
            downloadCallback.downloadCompleted(pdf, folderPath);
        }
        Toast.makeText(context, pdf.getPdf_name() + " Downloaded.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(progress[0]);
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {

            URL url = new URL(pdf.getPdf_url());//Create Download URl
            HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
            c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
            c.connect();//connect the URL Connection
            //If Connection response is not OK then show Logs
            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("Testing...", "Server returned HTTP " + c.getResponseCode() + " " + c.getResponseMessage());

            }
            int lenghtOfFile = c.getContentLength();
            //String folderPath = Constants.PDF_FOLDER + pdf.getPdf_name();
            apkStorage = new File(folderPath);
                /*//Get File if SD card is present
                if (new CheckForSDCard().isSDCardPresent()) {

                    apkStorage = new File(Environment.getExternalStorageDirectory() + "/" + Utils.downloadDirectory);
                } else
                    Toast.makeText(MainActivity.this, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT).show();
*/
            //If File is not present create directory
            if (!apkStorage.exists()) {
                apkStorage.mkdir();
                Log.e("Testing...", "Directory Created.");
            }

            outputFile = new File(apkStorage, pdf.getPdf_name() + ".pdf");//Create Output file in Main File

            //Create New File if not present
            if (!outputFile.exists()) {
                outputFile.createNewFile();
                Log.e("Testing...", "File Created");
            }

            FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

            InputStream is = c.getInputStream();//Get InputStream for connection

            byte[] buffer = new byte[1024];//Set buffer type
            int len1 = 0;//init length
            long total = 0;
            while ((len1 = is.read(buffer)) != -1) {
                total += len1;
                fos.write(buffer, 0, len1);//Write new file
                publishProgress((int) (total * 100 / lenghtOfFile));
                if (isCancelled()) {

                    Utils.deleteFolder(apkStorage);
                    break;
                }
            }
            //Close all connection after doing task
            fos.close();
            is.close();

        } catch (Exception e) {

            //Read exception if something went wrong
            e.printStackTrace();
            outputFile = null;
            Log.e("Testing...", "Download Error Exception " + e.getMessage());
        }

        return null;
    }
}