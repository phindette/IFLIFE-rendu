package com.example.myapplication.Modele;

public class Partiel {

    private String nom;
    private Competences competenceAPasser;
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
