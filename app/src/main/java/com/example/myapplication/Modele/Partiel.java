package com.example.myapplication.Modele;

import android.content.Context;
import android.os.AsyncTask;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.text.DateFormatSymbols;
import java.util.Calendar;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Competences.class,
                parentColumns = "id",
                childColumns = "idCompetence"
        )})
public class Partiel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nom")
    private String nom;

    @Ignore
    private Competences competenceAPasser;

    @Ignore
    private boolean reussit;

    @Ignore
    private Context context;

    @Ignore
    private Date dateDuDS;

    @ColumnInfo(name = "idCompetence")
    private int idCompetence;

    @ColumnInfo(name = "tauxRequis")
    private int tauxRequis;

    public Partiel(String nom, int tauxRequis, Competences cpt, Context context,Date dateDS) {
        setNom(nom);
        setTauxRequis(tauxRequis);
        setCompetenceAPasser(cpt);
        setDateDuDS(dateDS);
        this.context = context;

        // Récupération du DatabaseClient
        final DataBaseClient mDb = DataBaseClient.getInstance(context);

        class GetId extends AsyncTask<Void, Void, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                int id = mDb.getAppDatabase()
                        .CompetenceDao()
                        .getId(competenceAPasser.getNom());
                return id;
            }
        }

        try {
            setIdCompetence(new GetId().execute().get());
        }catch (Exception e){
            setIdCompetence(1);
        }
    }

    Partiel(String nom, int tauxRequis,int idCompetence) {
        setNom(nom);
        setTauxRequis(tauxRequis);
        setIdCompetence(idCompetence);
    }

    // à exécuter après création de partiel via la db
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
            setCompetenceAPasser(new GetId().execute().get());
        }catch (Exception e){
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTauxRequis() {
        return tauxRequis;
    }

    public void setTauxRequis(int tauxRequis) {
        this.tauxRequis = tauxRequis;
    }

    public Competences getCompetenceAPasser() {
        return competenceAPasser;
    }

    public void setCompetenceAPasser(Competences competenceAPasser) {
        this.competenceAPasser = competenceAPasser;
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

    public Date getDateDuDS() {
        return dateDuDS;
    }

    public void setDateDuDS(Date dateDuDS) {
        this.dateDuDS = dateDuDS;
    }

    public String getMoisDuDS() {
        return new DateFormatSymbols().getMonths()[dateDuDS.getMonth()-1];
    }

    public boolean isReussit() {
        return reussit;
    }

    public void setReussit(boolean reussit) {
        this.reussit = reussit;
    }
}
