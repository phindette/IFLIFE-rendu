package com.example.myapplication.Modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

    @ColumnInfo(name = "idCompetence")
    private int idCompetence;

    @ColumnInfo(name = "tauxRequis")
    private int tauxRequis;

    Partiel(String nom, int tauxRequis) {
        setNom(nom);
        setTauxRequis(tauxRequis);
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
}
