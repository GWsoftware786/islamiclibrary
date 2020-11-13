package com.gwsoftware.alahazratkakalam.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.gwsoftware.alahazratkakalam.models.CollectionModel;

import java.util.List;

@Dao
public interface CollectionDao {
    @Query("SELECT * FROM CollectionModel")
    List<CollectionModel> getAll();

    @Insert
    void insert(CollectionModel task);

    @Delete
    void delete(CollectionModel task);

    @Update
    void update(CollectionModel task);
}
