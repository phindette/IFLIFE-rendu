package com.example.myapplication.Modele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DateDAO {

    @Query("SELECT * FROM Date")
    List<Date> getAll();

    @Query("SELECT * FROM Date")
    List<Date> getLast();

    @Insert
    void insert(Date date);

    @Insert
    long[] insertAll(Date... date);

    @Delete
    void delete(Date date);

    @Update
    void update(Date date);
}
