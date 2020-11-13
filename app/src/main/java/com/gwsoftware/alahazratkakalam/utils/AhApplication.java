package com.gwsoftware.alahazratkakalam.utils;

import android.app.Application;

import com.gwsoftware.alahazratkakalam.models.DataObjectModel;

public class AhApplication extends Application {

    DataObjectModel dataObjectModel;
    public static AhApplication instance;

    public static AhApplication getInstance(){
        if(instance == null){
            instance = new AhApplication();
        }
        return instance;
    }

    public DataObjectModel getDataObjectModel() {
        return dataObjectModel;
    }

    public void setDataObjectModel(DataObjectModel dataObjectModel) {
        this.dataObjectModel = dataObjectModel;
    }
}
