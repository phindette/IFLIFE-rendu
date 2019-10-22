package com.example.myapplication.Modele;

import java.util.ArrayList;

public abstract class Activite {
    private String nom;
    private Statistique stat;


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Statistique getStat() {
        return stat;
    }

    public void setStat(Statistique stat) {
        this.stat = stat;
    }
}
