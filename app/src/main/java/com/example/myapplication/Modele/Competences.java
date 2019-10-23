package com.example.myapplication.Modele;

public class Competences {
    private String nom;
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
}
