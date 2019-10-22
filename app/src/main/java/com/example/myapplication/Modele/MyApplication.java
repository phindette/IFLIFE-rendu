package com.example.myapplication.Modele;

public class MyApplication {
    private Statistique energie;
    private Statistique satiete;
    private Statistique hygiene;

    public MyApplication(){
        energie = new Statistique("Energie");
        satiete = new Statistique("Satiété");
        hygiene = new Statistique("Hygiène");
    }
}
