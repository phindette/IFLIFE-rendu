package com.example.myapplication.Modele;

public class Humeur extends Statistique{

    Humeur(){
        super("Humeur");
    }

    public void calculerTaux(Statistique cEnergie,Statistique cSatiete,Statistique cHygiene){
        int t;
        t =  (cEnergie.getTaux()/3 + cSatiete.getTaux()/3 + cHygiene.getTaux()/3);
        setTaux(t);
    }
}
