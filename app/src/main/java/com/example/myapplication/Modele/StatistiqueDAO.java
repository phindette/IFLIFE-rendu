package com.example.myapplication.Modele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Modele.Statistique;

import java.util.List;

@Dao
public interface StatistiqueDAO {

    @Query("SELECT * FROM Statistique")
    List<Statistique> getAll();

    @Query("SELECT * FROM Statistique order by id desc limit 1")
    Statistique getLast();

    @Query("SELECT * FROM Statistique where nom=:stat order by id desc limit 1")
    Statistique getLastByName(String stat);

    @Insert
    void insert(Statistique s);

    @Insert
    long[] insertAll(Statistique... s);

    @Delete
    void delete(Statistique s);

    @Update
    void update(Statistique s);
}