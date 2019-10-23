package com.example.myapplication.Modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Competences {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "tauxMaitrise")
    private int tauxMaitrise;

    public Competences(String nom, int tauxMaitrise) {
        setNom(nom);
        setTauxMaitrise(tauxMaitrise);
    }

    public void augmenterTaux(int nb) {
        setTauxMaitrise(this.tauxMaitrise + nb);
    }

    public void diminuerTaux(int nb) {
        setTauxMaitrise(this.tauxMaitrise - nb);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getTauxMaitrise() {
        return tauxMaitrise;
    }

    public void setTauxMaitrise(int tauxMaitrise) {
        this.tauxMaitrise = tauxMaitrise;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
