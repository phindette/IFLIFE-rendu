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
    private double argent;

    Utilisateur(){
        energie = new Statistique("Energie");
        satiete = new Statistique("Satiété");
        hygiene = new Statistique("Hygiène");
        humeur = new Humeur();
        competences = new ArrayList<Competences>();
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


    public Statistique getHumeur(){
        return humeur;
    }

}
