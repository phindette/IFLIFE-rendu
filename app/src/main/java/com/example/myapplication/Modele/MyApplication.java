package com.example.myapplication.Modele;
import java.util.Calendar;
import java.util.GregorianCalendar;


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
    }

    public void prendreDouche(){
        utilisateur.augmenterHygiene(50);
    }

    public void manger(Activite a){
        utilisateur.augmenterSatiete(50);
    }

    public void dormir(){
        utilisateur.augmenterEnergie(75);
    }

    public void calculerHumer(){
        utilisateur.getHumeur();
    }
}
