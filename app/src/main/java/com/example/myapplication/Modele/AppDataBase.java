package com.example.myapplication.Modele;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Date.class,Utilisateur.class,Competences.class, Statistique.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract DateDAO DateDao();
    public abstract UtilisateurDAO UtilisateurDao();
    public abstract CompetenceDAO CompetenceDao();
    public abstract PartielDAO PartielDao();
    public abstract StatistiqueDAO StatistiqueDao();

}