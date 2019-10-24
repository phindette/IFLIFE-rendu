package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.ConstraintHorizontalLayout;

import android.app.Application;
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

import com.example.myapplication.Modele.Competences;
import com.example.myapplication.Modele.DataBaseClient;
import com.example.myapplication.Modele.Livres;
import com.example.myapplication.Modele.MyApplication;
import com.example.myapplication.Modele.Nourriture;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
    public TextView textHour;
    private ArrayList<Nourriture> nourritures;
    private ArrayList<Livres> livres;
    private Nourriture nourri; //A changer par un appel à la base de donnée quand données persistantes. Utilisé dans l'achat de nourriture.
    private ArrayList<Competences> listcomp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chambre);

        //PARTIE INITIALISATION
        application = new MyApplication(); //ligne à changer avec l'initialisation database
        nomJournee = (TextView) findViewById(R.id.textDayOfTheWeek);
        numeroJour = (TextView) findViewById(R.id.textDay);
        textHour = (TextView) findViewById(R.id.textHour);

        barreEnergie = (ProgressBar)  findViewById(R.id.energieBar);
        barreSatiete = (ProgressBar)  findViewById(R.id.satieteBar);
        barreHygiene = (ProgressBar)  findViewById(R.id.hygieneBar);
        barreHumeur = (ProgressBar)  findViewById(R.id.humeurBar);

        //Initialisation de la nourriture (à modif en DAO)
        nourritures = new ArrayList<>();
        livres = new ArrayList<>();
        listcomp = new ArrayList<>();
        init_nourriture();
        System.out.println("pute");

        //Initialisation des compétences et des livres (à modif en DAO)
        init_competences();
        init_livres();

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
                                textHour.setText(application.getCalendrier().getHeure()+":"+application.getCalendrier().getMinutes());

                                //UPDATE DES PROGRESS BAR
                                barreEnergie.setProgress(application.getUtilisateur().getEnergie().getTaux());
                                barreSatiete.setProgress(application.getUtilisateur().getSatiete().getTaux());
                                barreHygiene.setProgress(application.getUtilisateur().getHygiene().getTaux());
                                barreHumeur.setProgress(application.getUtilisateur().getHumeur().getTaux());

                                //boolean c = true;

                                //CALCUL DE L'HUMEUR
                                application.getUtilisateur().getHumeur().calculerTaux(application.getUtilisateur().getEnergie(),application.getUtilisateur().getSatiete(),application.getUtilisateur().getHygiene());


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


        alertDialogBuilder.setView(layout);
        AlertDialog alertD = alertDialogBuilder.create();

        boutonLivre.setText("Livres");
        boutonLivre.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                afficheShopLivre();
            }
        });
        layout.addView(boutonLivre);


        Button boutonNourriture = new Button(this);

        boutonNourriture.setText("Nourritures");
        boutonNourriture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                afficheShopNourriture();

            }
        });

        layout.addView(boutonNourriture);

        alertD.show();


    }

    private void afficheShopNourriture(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Choisir l'article à acheter");

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layoutGlobal = new LinearLayout(this);
        layoutGlobal.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layouttete = new LinearLayout(this);
        layouttete.setOrientation(LinearLayout.HORIZONTAL);

        final TextView vuetext = new TextView(this);
        vuetext.setText("Vous avez "+application.getUtilisateur().getArgent()+" €");
        layouttete.addView(vuetext);
        layoutGlobal.addView(layouttete);

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

            final Nourriture nourriture = nourritures.get(i);
            layout.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    application.getUtilisateur().acheterNourriture(nourriture);
                    vuetext.setText("Vous avez "+application.getUtilisateur().getArgent()+" €");
                }
            });

            layoutGlobal.addView(layout);
        }

        scrollView.addView(layoutGlobal);
        alertDialogBuilder.setView(scrollView);
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    private void afficheShopLivre(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Choisir l'article à acheter");

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layoutGlobal = new LinearLayout(this);
        layoutGlobal.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layouttete = new LinearLayout(this);
        layouttete.setOrientation(LinearLayout.HORIZONTAL);

        final TextView vuetext = new TextView(this);
        vuetext.setText("Vous avez "+application.getUtilisateur().getArgent()+" €");
        layouttete.addView(vuetext);
        layoutGlobal.addView(layouttete);

        for(int i = 0; i< livres.size();i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            //ajout de l'image
            File imgFile = new File(livres.get(i).getPathImage());
            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                ImageView myImage = new ImageView(this);

                myImage.setImageBitmap(myBitmap);

                layout.addView(myImage);
            }
            //ajout de la regen
            TextView regen = new TextView(this);
            regen.setText("restore "+livres.get(i).getAugmentation() + "points de compétences");
            layout.addView(regen);

            //ajout du prix
            TextView prix = new TextView(this);
            prix.setText("coûte "+livres.get(i).getCout()+" € /u");
            layout.addView(prix);
            layout.setBackgroundResource(R.drawable.border);
            layout.setClickable(true);

            final Livres livre = livres.get(i);
            layout.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    application.getUtilisateur().acheterLivre(livre);
                    vuetext.setText("Vous avez "+application.getUtilisateur().getArgent()+" €");
                }
            });

            layoutGlobal.addView(layout);
        }

        scrollView.addView(layoutGlobal);
        alertDialogBuilder.setView(scrollView);
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }



    private void init_nourriture(){


        // Récupération du DatabaseClient
        final DataBaseClient mDb = DataBaseClient.getInstance(getApplicationContext());

        class GetNourriture extends AsyncTask<Void, Void, List<Nourriture>> {

            @Override
            protected List<Nourriture> doInBackground(Void... voids) {
                List<Nourriture> c = mDb.getAppDatabase()
                        .NourritureDao()
                        .getAll();
                return c;
            }
        }

        try {
            nourritures.addAll(new GetNourriture().execute().get());
        }catch(Exception e){
        }
    }

    private void init_competences(){
        Competences ada = new Competences("Ada",0);
        application.getUtilisateur().addCompetence(ada);
        listcomp.add(ada);
        Competences sql = new Competences("Postgress",0);
        application.getUtilisateur().addCompetence(sql);
        listcomp.add(sql);
        Competences htmlcss = new Competences("html/css",0);
        application.getUtilisateur().addCompetence(htmlcss);
        listcomp.add(htmlcss);
    }

    private void init_livres(){
        Livres livreAdaN = new Livres("Manuel d'ada novice","Apprendre les bases de l'ada",10,10.5,listcomp.get(0),getApplicationContext());
        livres.add(livreAdaN);
        Livres sqlN = new Livres("Manuel de postgress novice","Apprendre les bases de postgress",10,10.5,listcomp.get(1),getApplicationContext());
        livres.add(sqlN);
        Livres htmlcssN = new Livres("Manuel d'html/css novice","Apprendre les bases de html/css",10,10.5,listcomp.get(2), getApplicationContext());
        livres.add(htmlcssN);
    }
}
