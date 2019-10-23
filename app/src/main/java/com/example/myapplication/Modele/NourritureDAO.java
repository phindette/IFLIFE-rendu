package com.example.myapplication.Modele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Modele.Nourriture;
import java.util.List;

@Dao
public interface NourritureDAO {


    @Query("SELECT * FROM Nourriture")
    List<Nourriture> getAll();

    @Query("SELECT * FROM Nourriture where nom=:n")
    Nourriture getItem(String n);

    @Insert
    void insert(Nourriture n);

    @Insert
    long[] insertAll(Nourriture n);

    @Delete
    void delete(Nourriture n);

    @Update
    void update(Nourriture n);
}
