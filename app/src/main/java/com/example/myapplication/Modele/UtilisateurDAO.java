package com.example.myapplication.Modele;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Modele.Utilisateur;

import java.util.List;

@Dao
public interface UtilisateurDAO {

    @Query("SELECT * FROM Utilisateur")
    List<Utilisateur> getAll();

    @Query("SELECT * FROM Utilisateur order by id desc limit 1")
    Utilisateur getLast();

    @Insert
    void insert(Utilisateur u);

    @Insert
    long[] insertAll(Utilisateur... u);

    @Delete
    void delete(Utilisateur u);

    @Update
    void update(Utilisateur u);
}