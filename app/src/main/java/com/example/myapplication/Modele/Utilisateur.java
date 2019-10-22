package com.example.myapplication.Modele;
import java.io.Serializable;
import java.util.ArrayList;

public class Utilisateur implements Serializable {
    private String nom;
    private String sexe;
    private ArrayList<Competences> competences;
    private Statistique energie;
    private Statistique satiete;
    private Statistique hygiene;
    private Humeur humeur;



    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public ArrayList<Competences> getCompetences() {
        return competences;
    }

    public void setCompetences(ArrayList<Competences> competences) {
        this.competences = competences;
    }

    public void augmenterHygiene(int tauxAugmentation){
        hygiene.augmenterStat(tauxAugmentation);
    }

    public Statistique getHygiene(){
        return hygiene;
    }

    public void augmenterSatiete(int tauxAugmentation){
        satiete.augmenterStat(tauxAugmentation);
    }

    public Statistique getSatiete(){
        return satiete;
    }

    public void augmenterEnergie(int tauxAugmentation){
        energie.augmenterStat(tauxAugmentation);
    }

    public Statistique getEnergie(){
        return energie;
    }


    public Statistique getHumeur(){
        return humeur;
    }

}
