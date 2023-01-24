package com.gwsoftware.alahazratkakalam.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class AppUpdate {
    public void checkAppUpdate(final Context context) {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

        Task<AppUpdateInfo> appUpdateInfo = appUpdateManager.getAppUpdateInfo();
        appUpdateInfo.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                    //Toast.makeText(context, "Update is avail", Toast.LENGTH_SHORT).show();
                    showUpdateDialog(context);
                }
            }
        });

    }

    Dialog dialog;

    private void showUpdateDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("A New Update is Available");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=com.mfsoftware.alahazratkakalam")));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //background.start();
            }
        });

        builder.setCancelable(false);
        dialog = builder.show();
    }


}
