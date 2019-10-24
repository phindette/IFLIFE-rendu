package com.example.myapplication.Modele;

import android.content.Context;
import android.os.AsyncTask;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Competences.class,
                parentColumns = "id",
                childColumns = "idCompetence"
        )},
        indices = {@Index("idCompetence")})
public class Livres {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pathImage")
    private String pathImage;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "description")
    private String description;

    @Ignore
    private Competences competence;

    @ColumnInfo(name = "cout")
    private double cout;

    @ColumnInfo(name = "augmentation")
    private int augmentation;

    @ColumnInfo(name = "idCompetence")
    private int idCompetence;

    @Ignore
    private Context context;


    public Livres(String nom, String description, int augmentation, double cout,Competences competence, Context context) {
        this.nom = nom;
        this.description = description;
        this.cout = cout;
        this.pathImage = "";
        this.augmentation = augmentation;
        this.competence = competence;
        this.context = context;

        final Competences comp = this.competence;
        // Récupération du DatabaseClient
        final DataBaseClient mDb = DataBaseClient.getInstance(context);

        class GetId extends AsyncTask<Void, Void, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                int id = mDb.getAppDatabase()
                        .CompetenceDao()
                        .getId(comp.getNom());
                return id;
            }
        }

        try {
            setIdCompetence(new GetId().execute().get());
        }catch (Exception e){
            setIdCompetence(1);
        }
    }

    public Livres(String nom, String description, int augmentation, double cout, int idCompetence ) {
        this.nom = nom;
        this.description = description;
        this.cout = cout;
        this.pathImage = "";
        this.augmentation = augmentation;
        this.idCompetence = idCompetence;
    }

    // à exécuter après création de livre via la db
    public void construireLivre(Context context){
        this.context = context;

        // Récupération du DatabaseClient
        final DataBaseClient mDb = DataBaseClient.getInstance(context);

        class GetId extends AsyncTask<Void, Void, Competences> {

            @Override
            protected Competences doInBackground(Void... voids) {
                Competences c = mDb.getAppDatabase()
                        .CompetenceDao()
                        .getById(idCompetence);
                return c;
            }
        }

        try {
            setCompetence(new GetId().execute().get());
        }catch (Exception e){
        }
    }

    public String getPathImage() {
        return pathImage;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public Competences getCompetence() {
        return competence;
    }

    public double getCout() {
        return cout;
    }

    public int getAugmentation() {
        return augmentation;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompetence(Competences competence) {
        this.competence = competence;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }

    public void setAugmentation(int augmentation) {
        this.augmentation = augmentation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCompetence() {
        return idCompetence;
    }

    public void setIdCompetence(int idCompetence) {
        this.idCompetence = idCompetence;
    }
}
