package com.gwsoftware.alahazratkakalam.asyncktask;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.gwsoftware.alahazratkakalam.BuildConfig;

import org.jsoup.Jsoup;

public class GetNewVersionCode extends AsyncTask<Void, String, String> {

    Context context;

    public GetNewVersionCode(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String newVersion = null;
        try {

            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();
            //  //Log.e("latestversion", "---" + newVersion);
            return newVersion;
        } catch (Exception e) {

            return newVersion;
        } catch (OutOfMemoryError e) {
            System.gc();
            return newVersion;
        }
    }

    @Override
    protected void onPostExecute(String newVersion) {
        super.onPostExecute(newVersion);
        if (newVersion != null && !newVersion.isEmpty()) {
            if (!newVersion.equals(BuildConfig.VERSION_NAME)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent updateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en"));
                        context.startActivity(updateIntent);
                    }
                });

                builder.setNegativeButton("Remind Me Later", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setTitle("Update App");
                builder.setMessage("New Version Is Available.");
                if (!((Activity) context).isFinishing()) {
                    builder.show();
                }
            }
        }
    }

}