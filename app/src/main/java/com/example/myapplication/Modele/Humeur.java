package com.example.myapplication.Modele;

public class Humeur extends Statistique{

    Humeur(){
        super("Humeur");
    }

    @Override
    public int getTaux() {
        int t = super.getTaux();
        return (int) (t*5/100);
    }
}
