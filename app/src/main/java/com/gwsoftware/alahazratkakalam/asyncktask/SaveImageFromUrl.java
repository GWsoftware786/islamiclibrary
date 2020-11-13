package com.gwsoftware.alahazratkakalam.asyncktask;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.gwsoftware.alahazratkakalam.interfaces.SaveImageCallback;
import com.gwsoftware.alahazratkakalam.utils.Constants;
import com.gwsoftware.alahazratkakalam.utils.Utils;

public class SaveImageFromUrl extends AsyncTask<Void, Void, Bitmap> {

    //DataObjectModel.Pdf pdf;
    SaveImageCallback saveImageCallback;
    String thumbPath;
    String fileName;

    public SaveImageFromUrl(Context context, String thumbPath, String fileName, SaveImageCallback saveImageCallback) {
        //this.pdf = pdf;
        this.thumbPath = thumbPath;
        this.fileName = fileName;
        this.saveImageCallback = saveImageCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = Utils.getBitmapFromURL(thumbPath);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        String imagePath = Utils.saveImage(bitmap, Constants.PDF_FOLDER + fileName, fileName);
        if (saveImageCallback != null) {
            saveImageCallback.imageSaveCallback(imagePath);
        }
    }
}