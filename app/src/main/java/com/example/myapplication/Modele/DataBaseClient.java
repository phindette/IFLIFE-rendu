package com.example.myapplication.Modele;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;

public class DataBaseClient {

    // Instance unique permettant de faire le lien avec la base de données
    private static DataBaseClient instance;

    // Objet représentant la base de données de votre application
    private AppDataBase appDatabase;

    // Constructeur
    private DataBaseClient(final Context context) {

        // Créer l'objet représentant la base de données de votre application
        // à l'aide du "Room database builder"
        // MyToDos est le nom de la base de données
        //appDatabase = Room.databaseBuilder(context, AppDatabase.class, "MyToDos").build();

        // Ajout de la méthode addCallback permettant de populate (remplir) la base de données à sa création
        appDatabase = Room.databaseBuilder(context, AppDataBase.class, "DB").addCallback(roomDatabaseCallback).build();
    }

    // Méthode statique
    // Retourne l'instance de l'objet DatabaseClient
    public static synchronized DataBaseClient getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseClient(context);
        }
        return instance;
    }

    // Retourne l'objet représentant la base de données de votre application
    public AppDataBase getAppDatabase() {
        return appDatabase;
    }

    // Objet permettant de populate (remplir) la base de données à sa création
    RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {

        // Called when the database is created for the first time.
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            //ajouter des lignes ici

            //Nourritures     AJOUTER PATH IMAGE A LA PLACE DE "" EN 4E PARAMETRE
            db.execSQL("INSERT INTO Nourriture (nom,description,montantRegen,pathImage,cout) VALUES(\"Tacos simple\",\"Sans tomates, sans salade et c'est parti !\",60,\"\", 5.0);");
            db.execSQL("INSERT INTO Nourriture (nom,description,montantRegen,pathImage,cout) VALUES(\"Kebab\",\"La viande provient de nos campagnes, le pain de nos boulangers.\",50,\"\", 5.0);");
            db.execSQL("INSERT INTO Nourriture (nom,description,montantRegen,pathImage,cout) VALUES(\"Tacos double\",\"Sans tomates, sans salade et double viande !\",80,\"\", 6.5);");
            db.execSQL("INSERT INTO Nourriture (nom,description,montantRegen,pathImage,cout) VALUES(\"Big Kebab\",\"La viande provient de nos campagnes, le pain de nos boulangers mais il est plus gros! \",60,\"\", 5.5);");
            db.execSQL("INSERT INTO Nourriture (nom,description,montantRegen,pathImage,cout) VALUES(\"Pates au sel\",\"Efficace et pas cher, c'est les pates au sel que je préfère\",30,\"\",2.0);");

            //Competences
            db.execSQL("INSERT INTO Competences (nom,tauxMaitrise) VALUES(\"Ada\",0);");
            db.execSQL("INSERT INTO Competences (nom,tauxMaitrise) VALUES(\"Postgress\",0);");
            db.execSQL("INSERT INTO Competences (nom,tauxMaitrise) VALUES(\"html/css\",0);");

            //Livres
            db.execSQL("INSERT INTO Livres (nom,description,augmentation,cout,idCompetence,pathImage) VALUES(\"Manuel d'ada novice\",\"Apprendre les bases de l'ada\",10,10.5,(SELECT id from Competences where nom='Ada'),\"\");");
            db.execSQL("INSERT INTO Livres (nom,description,augmentation,cout,idCompetence,pathImage) VALUES(\"Manuel de postgress novice\",\"Apprendre les bases de postgress\",10,10.5,(SELECT id from Competences where nom='Postgress'),\"\");");
            db.execSQL("INSERT INTO Livres (nom,description,augmentation,cout,idCompetence,pathImage) VALUES(\"Manuel d'html/css novice\",\"Apprendre les bases de html/css\",10,10.5,(SELECT id from Competences where nom='html/css'),\"\");");
        }
    };
}