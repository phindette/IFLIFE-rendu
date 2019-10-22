package com.example.myapplication.Modele;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Date.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract DateDAO DateDao();

}