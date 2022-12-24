package com.gwsoftware.alahazratkakalam.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.gwsoftware.alahazratkakalam.dao.CollectionDao;
import com.gwsoftware.alahazratkakalam.models.CollectionModel;

@Database(entities = {CollectionModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CollectionDao taskDao();
}
