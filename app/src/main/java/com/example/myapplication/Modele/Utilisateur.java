package com.example.myapplication.Modele;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
    private HashMap<Nourriture,Integer> inv_nourriture;

    @Ignore
    private ArrayList<Livres> inv_livres;

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

    public Utilisateur(){
        energie = new Statistique("Energie");
        energie.setTaux(50);
        satiete = new Statistique("Satiété");
        satiete.setTaux(50);
        hygiene = new Statistique("Hygiène");
        hygiene.setTaux(50);
        humeur = new Humeur();
        humeur.setTaux(50);
        competences = new ArrayList<Competences>();
        inv_livres = new ArrayList<>();
        inv_nourriture = new HashMap<>();
        argent = 100; //à changer

    }
    public void addCompetence(Competences competences){
        if(!this.competences.contains(competences)){
            this.competences.add(competences);
        }

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
    public void acheterLivre(Livres livre){
        if(livre.getCout()<this.getArgent()){
            retirerArgent(livre.getCout());
            int i = 0;
            while(i < competences.size() && competences.get(i) != livre.getCompetence()){
                i++;
            }
            if(competences.get(i) == livre.getCompetence()){
                competences.get(i).augmenterTaux(livre.getAugmentation());
            }
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


    public Humeur getHumeur(){
        return humeur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void ajouterLivre(Livres livre){
        inv_livres.add(livre);
    }

    public ArrayList<Livres> getInv_livres() {
        return inv_livres;
    }

    public void ajouterNourriture(Nourriture nourriture){

    }
}
