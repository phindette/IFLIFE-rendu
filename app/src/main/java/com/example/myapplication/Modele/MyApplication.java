package com.example.myapplication.Modele;


public class MyApplication {

    private Statistique energie;
    private Statistique satiete;
    private Statistique hygiene;
    private Humeur humeur;
    private Calendrier calendrier;
    private Utilisateur utilisateur;

    public MyApplication(){
        energie = new Statistique("Energie");
        satiete = new Statistique("Satiété");
        hygiene = new Statistique("Hygiène");
        humeur = new Humeur();
        calendrier = new Calendrier();
        utilisateur = new Utilisateur();
    }

    public void prendreDouche(){
        getUtilisateur().augmenterHygiene(50);
        getCalendrier().ajouterMinutes(20);
    }

    public void manger(Activite a){
        getUtilisateur().augmenterSatiete(50);
        //Action instantanée
    }

    public void dormir(){
        getUtilisateur().augmenterEnergie(75);
        getCalendrier().ajouterHeure(6);
    }

    public void calculerHumer(){
        getUtilisateur().getHumeur();
    }

    public Statistique getEnergie() {
        return energie;
    }

    public Statistique getSatiete() {
        return satiete;
    }

    public Statistique getHygiene() {
        return hygiene;
    }

    public Humeur getHumeur() {
        return humeur;
    }

    public Calendrier getCalendrier() {
        return calendrier;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
}
