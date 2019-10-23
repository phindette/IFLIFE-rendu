package com.example.myapplication.Modele;

import java.util.ArrayList;

public class Activite {
    private String nom;
    private int stat;
    private int heure;

    public Activite(String nom,int stat, int heure){
        setNom(nom);
        setStat(stat);
        setHeure(heure);
    }
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }

    public int getHeure(){
        return heure;
    }

    public void setHeure(int temps) {
        this.heure = temps;
    }
}
