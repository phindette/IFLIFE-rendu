package com.example.myapplication.Modele;

public class Livres {
    private String pathImage;
    private String nom;
    private String description;
    private Competences competence;
    private double cout;
    private int augmentation;


    public Livres(String nom, String description, int augmentation, double prix,Competences competence) {
        this.nom = nom;
        this.description = description;
        this.cout = prix;
        this.pathImage = "";
        this.augmentation = augmentation;
        this.competence = competence;
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
}
