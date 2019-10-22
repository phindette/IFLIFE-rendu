package com.example.myapplication.Modele;

public class Statistique {
    private int taux;
    private String nom;
    private int bornMin;
    private int borneMax;

    Statistique(String nomstat){
        nom = nomstat;
        bornMin = 0;
        borneMax = 100;
        taux =0;
    }
    public void augmenterStat(int nbAjout){
        taux += nbAjout;
    }

    public void baisserStat(int nbEnlever){
        taux -= nbEnlever;
    }
}
