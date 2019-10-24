package com.example.myapplication.Modele;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Date.class,Utilisateur.class,Competences.class, Statistique.class,Nourriture.class,Livres.class,Partiel.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract DateDAO DateDao();
    public abstract UtilisateurDAO UtilisateurDao();
    public abstract CompetenceDAO CompetenceDao();
    public abstract StatistiqueDAO StatistiqueDao();
    public abstract NourritureDAO NourritureDao();
    public abstract LivreDAO LivresDao();
    public abstract PartielDAO PartielDao();
}