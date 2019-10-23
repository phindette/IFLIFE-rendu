package com.example.myapplication.Modele;

public class Nourriture {
    private String pathImage;
    private String nom;
    private String description;
    private int montantRegen;
    private double cout;

    public Nourriture(String nom, String description, int montantRegen, double cout) {
        this.nom = nom;
        this.description = description;
        this.montantRegen = montantRegen;
        this.cout = cout;
        pathImage = "";
    }

    public String getNom() {
        return nom;
    }

    public String getPathImage(){
        return this.pathImage;
    }

    public void setPathImage(String path){
        this.pathImage = path;
    }

    public String getDescription() {
        return description;
    }

    public int getMontantRegen() {
        return montantRegen;
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

    public void setMontantRegen(int montantRegen) {
        this.montantRegen = montantRegen;
    }

    public void setCout(double cout) {
        this.cout = cout;
    }
}
