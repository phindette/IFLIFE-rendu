package com.example.myapplication.Modele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LivreDAO {

    @Query("SELECT * FROM Livres")
    List<Livres> getAll();

    @Query("SELECT * FROM Livres order by id desc limit 1")
    Livres getLast();

    @Query("SELECT * FROM Livres where idCompetence=(SELECT id from Competences where nom=:competence)")
    List<Livres> getByCompetence(String competence); // à invoquer avec competence = competence.getNom()

    @Insert
    void insert(Livres l);

    @Insert
    long[] insertAll(Livres... l);

    @Delete
    void delete(Livres l);

    @Update
    void update(Livres l);
}