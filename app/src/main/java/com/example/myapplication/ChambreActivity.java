package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Modele.MyApplication;

public class ChambreActivity extends AppCompatActivity {
    public MyApplication application;
    public TextView nomJournee;
    public TextView numeroJour;
    public ProgressBar barreEnergie;
    public ProgressBar barreSatiete;
    public ProgressBar barreHygiene;
    public ProgressBar barreHumeur;
    public Thread  thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chambre);

        //PARTIE INITIALISATION
        application = new MyApplication(); //ligne à changer avec l'initialisation database
        nomJournee = (TextView) findViewById(R.id.textDayOfTheWeek);
        numeroJour = (TextView) findViewById(R.id.textDay);

        barreEnergie = (ProgressBar)  findViewById(R.id.energieBar);
        barreSatiete = (ProgressBar)  findViewById(R.id.satieteBar);
        barreHygiene = (ProgressBar)  findViewById(R.id.hygieneBar);
        barreHumeur = (ProgressBar)  findViewById(R.id.humeurBar);

        //Ligne de test pour les barres
        //application.getUtilisateur().augmenterSatiete(150);



        //PARTIE CONTROLE
        jeu();



    }


    public void jeu() {
           thread = new Thread() {
               int i =0;
            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //UPDATE DE LA DATE
                                nomJournee.setText(application.getCalendrier().getJour());
                                numeroJour.setText("Jour "+application.getCalendrier().getJourDuMois());

                                //UPDATE DES PROGRESS BAR
                                barreEnergie.setProgress(application.getUtilisateur().getEnergie().getTaux());
                                barreSatiete.setProgress(application.getUtilisateur().getSatiete().getTaux());
                                barreHygiene.setProgress(application.getUtilisateur().getHygiene().getTaux());
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();




    }

    public void seDoucher(){
        application.prendreDouche();
    }
}
