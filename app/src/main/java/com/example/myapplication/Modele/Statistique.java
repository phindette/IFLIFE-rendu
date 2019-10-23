package com.example.myapplication.Modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Statistique {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "taux")
    private int taux;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "borneMin")
    private int bornMin;

    @ColumnInfo(name = "borneMax")
    private int borneMax;

    Statistique(String nomstat){
        setNom(nomstat);
        setBornMin(0);
        setBorneMax(100);
        setTaux(0);
    }
    public void augmenterStat(int nbAjout){
        setTaux(getTaux() + nbAjout);
    }

    public void baisserStat(int nbEnlever){
        setTaux(getTaux() - nbEnlever);
    }

    public int getTaux() {
        return taux;
    }

    public void setTaux(int taux) {
        this.taux = taux;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getBornMin() {
        return bornMin;
    }

    public void setBornMin(int bornMin) {
        this.bornMin = bornMin;
    }

    public int getBorneMax() {
        return borneMax;
    }

    public void setBorneMax(int borneMax) {
        this.borneMax = borneMax;
    }
}
