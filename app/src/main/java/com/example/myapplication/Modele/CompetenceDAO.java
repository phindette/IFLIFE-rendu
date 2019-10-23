package com.example.myapplication.Modele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CompetenceDAO {

    @Query("SELECT * FROM Competences")
    List<Competences> getAll();

    @Query("SELECT * FROM Competences order by id desc limit 1")
    Competences getLast();

    @Query("SELECT id FROM Competences WHERE nom=:competence")
    int getId(String competence);

    @Insert
    void insert(Competences c);

    @Insert
    long[] insertAll(Competences... c);

    @Delete
    void delete(Competences c);

    @Update
    void update(Competences c);
}
