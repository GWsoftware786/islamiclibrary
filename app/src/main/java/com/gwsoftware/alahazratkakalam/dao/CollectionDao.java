package com.gwsoftware.alahazratkakalam.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
