package com.gwsoftware.alahazratkakalam.interfaces;

import com.gwsoftware.alahazratkakalam.models.DataObjectModel;

public interface DownloadCallback {
    void downloadCompleted(DataObjectModel.Pdf pdf, String folderPath);
}
