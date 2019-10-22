package com.example.myapplication.Modele;

public class BonusMalus {
    private String description;
    private int temps;
    private int taux;

    BonusMalus(String description, int temps, int taux) {
        setDescription(description);
        setTemps(temps);
        setTaux(taux);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public int getTaux() {
        return taux;
    }

    public void setTaux(int taux) {
        this.taux = taux;
    }
}
