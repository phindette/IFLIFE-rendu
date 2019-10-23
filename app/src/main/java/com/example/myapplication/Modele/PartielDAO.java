package com.example.myapplication.Modele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PartielDAO {

    @Query("SELECT * FROM Partiel")
    List<Partiel> getAll();

    @Query("SELECT * FROM Partiel where idCompetence=(SELECT id from Competences where nom=:competence)")
    List<Partiel> getByCompetence(String competence); // à invoquer avec competence = competence.getNom()

    @Insert
    void insert(Partiel p);

    @Delete
    void delete(Partiel p);

    @Update
    void update(Partiel p);
}