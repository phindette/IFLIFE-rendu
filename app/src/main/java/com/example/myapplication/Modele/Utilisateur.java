package com.example.myapplication.Modele;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity
public class Utilisateur implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "nom")
    private String nom;

    @ColumnInfo(name = "sexe")
    private String sexe;

    @Ignore
    private ArrayList<Competences> competences;

    @Ignore
    private Statistique energie;

    @Ignore
    private Statistique satiete;

    @Ignore
    private Statistique hygiene;

    @Ignore
    private Humeur humeur;

    @Ignore
    private double argent;

    Utilisateur(){
        energie = new Statistique("Energie");
        satiete = new Statistique("Satiété");
        hygiene = new Statistique("Hygiène");
        humeur = new Humeur();
        competences = new ArrayList<Competences>();
        argent = 100; //à changer
    }

    public double getArgent(){
        return argent;
    }
    public void ajouterArgent(double montant){
        argent = argent + montant;
    }

    public boolean retirerArgent(double montant){
        if(argent - montant < 0){
            return false;
        }else{
            argent = argent - montant;
            return true;
        }
    }

    public void acheterNourriture(Nourriture nourriture){
        if(nourriture.getCout() < this.getArgent()){
            retirerArgent(nourriture.getCout());
            augmenterSatiete(nourriture.getMontantRegen());
        }
    }

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
