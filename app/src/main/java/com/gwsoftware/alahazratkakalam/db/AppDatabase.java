package com.gwsoftware.alahazratkakalam.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gwsoftware.alahazratkakalam.dao.CollectionDao;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;

@Database(entities = {CollectionModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CollectionDao taskDao();
}
