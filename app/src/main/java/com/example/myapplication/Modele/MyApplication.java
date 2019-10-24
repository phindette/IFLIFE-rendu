package com.example.myapplication.Modele;


public class MyApplication {

    private Statistique energie;
    private Statistique satiete;
    private Statistique hygiene;
    private Calendrier calendrier;
    private Utilisateur utilisateur;

    //Activité dont les temps sont fixes donc qui ne seront jamais modifiées dans le code
    private Activite prendreDouche;
    private Activite manger;
    private Activite seLaver;

    //Activité dont un va modifier le temps et l'activation durant le jeu
    private Activite reviser;
    private Activite dormir;


    public MyApplication(){
        calendrier = new Calendrier();
        utilisateur = new Utilisateur();

        setPrendreDouche(new Activite("Se doucher",50,1));
        setManger(new Activite("Se nourrir",50,0));
        setSeLaver(new Activite("Se laver",80,1));

        setReviser(new Activite("Reviser",50,0));
        setDormir(new Activite("Se coucher",50,0));
    }

    public void prendreDouche(){
        getUtilisateur().augmenterHygiene(seLaver.getStat());
        getCalendrier().ajouterMinutes(20);
    }

    public void manger(Activite a){
        getUtilisateur().augmenterSatiete(50);
        //Action instantanée
    }

    public void dormir(){
        getUtilisateur().augmenterEnergie(dormir.getStat());
        getCalendrier().ajouterHeure(dormir.getHeure());
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


    public Calendrier getCalendrier() {
        return calendrier;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public Activite getPrendreDouche() {
        return prendreDouche;
    }

    public void setPrendreDouche(Activite prendreDouche) {
        this.prendreDouche = prendreDouche;
    }

    public Activite getManger() {
        return manger;
    }

    public void setManger(Activite manger) {
        this.manger = manger;
    }

    public Activite getSeLaver() {
        return seLaver;
    }

    public void setSeLaver(Activite seLaver) {
        this.seLaver = seLaver;
    }

    public Activite getReviser() {
        return reviser;
    }

    public void setReviser(Activite reviser) {
        this.reviser = reviser;
    }

    public Activite getDormir() {
        return dormir;
    }

    public void setDormir(Activite dormir) {
        this.dormir = dormir;
    }
}
