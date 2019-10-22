package com.example.myapplication.Modele;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MyApplication {

    private Statistique energie;
    private Statistique satiete;
    private Statistique hygiene;
    private Humeur humeur;
    private Calendar calendar;

    public MyApplication(){
        energie = new Statistique("Energie");
        satiete = new Statistique("Satiété");
        hygiene = new Statistique("Hygiène");
        humeur = new Humeur();
        calendar = new GregorianCalendar(2018,9,1,7,0,0);
    }
}
