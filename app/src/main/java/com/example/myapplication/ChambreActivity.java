package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.Modele.MyApplication;
import com.example.myapplication.Modele.Nourriture;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChambreActivity extends AppCompatActivity {
    public MyApplication application;
    public TextView nomJournee;
    public TextView numeroJour;
    public ProgressBar barreEnergie;
    public ProgressBar barreSatiete;
    public ProgressBar barreHygiene;
    public ProgressBar barreHumeur;
    public Thread  thread;
    private ArrayList<Nourriture> nourritures;

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

        //Initialisation de la nourriture (à modif en DAO)
        nourritures = new ArrayList<>();
        init_nourriture();

        //Ligne de test pour les barres
        //application.getUtilisateur().augmenterSatiete(150);





        //PARTIE CONTROLE
        jeu();



    }


    public void jeu() {
           thread = new Thread() {
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

    public void cliqueBtnDormir(View w){
        //FONCTION PERMETTANT A L'UTiLISATEUR DE DORMIR
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Combien d'heures voulez vous dormir ?");

        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(8);
        numberPicker.setMinValue(6);
        alertDialogBuilder.setView(numberPicker);


        alertDialogBuilder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ChambreActivity.this,"Vous allez dormir : "+numberPicker.getValue()+" heures",Toast.LENGTH_SHORT).show();

                //Gestion du modèle
                application.getDormir().setHeure(numberPicker.getValue());
                switch(numberPicker.getValue()){
                    case 6 :
                        application.getDormir().setStat(60);
                        break;
                    case 7 :
                        application.getDormir().setStat(70);
                        break;
                    case 8 :
                        application.getDormir().setStat(80);
                        break;
                }
                application.dormir();

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void cliqueBtnDouche(View w){
        application.prendreDouche();
    }

    public void cliqueBtnShop(View w){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Que voulez-vous acheter?");

        LinearLayout layout = new LinearLayout(this);
        Button boutonLivre = new Button(this);

        boutonLivre.setText("Livres");
        boutonLivre.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Toast.makeText(ChambreActivity.this,"clic sur le shop Livres",Toast.LENGTH_SHORT).show();
            }
        });
        layout.addView(boutonLivre);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        Button boutonNourriture = new Button(this);

        boutonNourriture.setText("Nourritures");
        boutonNourriture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                afficheShopNourriture();
            }
        });

        layout.addView(boutonNourriture);
        alertDialogBuilder.setView(layout);
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();


    }

    private void afficheShopNourriture(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Choisir l'article à acheter");

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layoutGlobal = new LinearLayout(this);
        layoutGlobal.setOrientation(LinearLayout.VERTICAL);

        for(int i = 0; i< nourritures.size();i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            //ajout de l'image
            File imgFile = new File(nourritures.get(i).getPathImage());
            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView myImage = new ImageView(this);

                myImage.setImageBitmap(myBitmap);

                layout.addView(myImage);
            }
            //ajout de la regen
            TextView regen = new TextView(this);
            regen.setText("restore "+nourritures.get(i).getMontantRegen() + "points de nourriture");
            layout.addView(regen);

            //ajout du prix
            TextView prix = new TextView(this);
            prix.setText("coûte "+nourritures.get(i).getCout()+" € /u");
            layout.addView(prix);
            layout.setBackgroundResource(R.drawable.border);
            layout.setClickable(true);

            layoutGlobal.addView(layout);
        }

        scrollView.addView(layoutGlobal);
        alertDialogBuilder.setView(scrollView);
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }



    private void init_nourriture(){
        Nourriture tacosS = new Nourriture("Tacos simple","Sans tomates, sans salade et c'est parti !",60, 5.0);
        nourritures.add(tacosS);
        Nourriture kebab = new Nourriture("Kebab","La viande provient de nos campagnes, le pain de nos boulangers.",50, 5.0);
        nourritures.add(kebab);
        Nourriture tacosD = new Nourriture("Tacos double","Sans tomates, sans salade et double viande !",80, 6.5);
        nourritures.add(tacosD);
        Nourriture bkebab = new Nourriture("Big Kebab","La viande provient de nos campagnes, le pain de nos boulangers mais il est plus gros! ",60, 5.5);
        nourritures.add(bkebab);
        Nourriture patesausel = new Nourriture("Pates au sel","Efficace et pas cher, c'est les pates au sel que je préfère",30, 2.0);
        nourritures.add(patesausel);
    }
}
