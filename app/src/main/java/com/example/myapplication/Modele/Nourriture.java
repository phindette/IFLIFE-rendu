package com.example.myapplication.Modele;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Nourriture {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "montantRegen")
    private int montantRegen;

    @ColumnInfo(name = "pathImage")
    private String pathImage;

    @ColumnInfo(name = "cout")
    private double cout;


    public Nourriture(String nom, String description, int montant, double prix) {
        this.nom = nom;
        this.description = description;
        this.montantRegen = montant;
        this.cout = prix;
        this.pathImage = "";
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getMontantRegen() {
        return montantRegen;
    }

    public String getPathImage() {
        return pathImage;
    }

    public double getCout() {
        return cout;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMontantRegen(int montant) {
        this.montantRegen = montant;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public void setCout(double prix) {
        this.cout = prix;
    }
}
