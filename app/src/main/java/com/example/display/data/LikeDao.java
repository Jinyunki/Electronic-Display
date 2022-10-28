package com.example.display.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface LikeDao {
    @Insert
    void insert(LikeData likeData);

    @Delete
    void delete(LikeData likeData);

    @Query("DELETE FROM table_name WHERE id = :id")
    void delete(int id);

    @Delete
    void reset(List<LikeData> likeData);

    @Query("UPDATE table_name SET text = :sText WHERE ID = :sID")
    void update(int sID, String sText);

    @Query("SELECT * FROM table_name")
    List<LikeData> getAll();
}