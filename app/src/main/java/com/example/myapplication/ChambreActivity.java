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
import android.view.Gravity;
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
import com.example.myapplication.Modele.Date;
import com.example.myapplication.Modele.Livres;
import com.example.myapplication.Modele.MyApplication;
import com.example.myapplication.Modele.Nourriture;
import com.example.myapplication.Modele.Partiel;

import org.w3c.dom.Text;

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

        //Initialisation des examens
        init_partiels();

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
                                TextView humeur = (TextView) findViewById(R.id.textHumeur);
                                humeur.setText(application.getUtilisateur().getHumeur().getTaux() + " ");
                                TextView energie = (TextView) findViewById(R.id.textEnergie);
                                energie.setText(application.getUtilisateur().getEnergie().getTaux() + " ");
                                TextView hygiene = (TextView) findViewById(R.id.textHygiene);
                                hygiene.setText(application.getUtilisateur().getHygiene().getTaux() +" ");
                                TextView satiete = (TextView) findViewById(R.id.textSatiete);
                                satiete.setText(application.getUtilisateur().getSatiete().getTaux() +" ");

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

    public void cliqueRevision(View w) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Que voulez-vous réviser?");

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layoutGlobal = new LinearLayout(this);
        layoutGlobal.setOrientation(LinearLayout.VERTICAL);

        for(int i = 0; i< application.getUtilisateur().getInv_livres().size();i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            Button bouton = new Button(this);
            bouton.setText(application.getUtilisateur().getInv_livres().get(i).getNom());



            final Livres livre = application.getUtilisateur().getInv_livres().get(i);
            bouton.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    reviser(livre);
                }
            });
            layout.addView(bouton);

            layoutGlobal.addView(layout);
        }

        scrollView.addView(layoutGlobal);
        alertDialogBuilder.setView(scrollView);
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

    }

        private  void reviser(final Livres livre){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
            alertDialogBuilder.setTitle("Combien d'heures voulez vous réviser");

            final NumberPicker numberPicker = new NumberPicker(this);
            numberPicker.setMaxValue(5);
            numberPicker.setMinValue(1);
            alertDialogBuilder.setView(numberPicker);

            alertDialogBuilder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(ChambreActivity.this,"Vous allez réivser : "+numberPicker.getValue()+" heures",Toast.LENGTH_SHORT).show();

                    //Gestion du modèle
                    application.getUtilisateur().reviser(livre,numberPicker.getValue());
                    application.getCalendrier().ajouterHeure(numberPicker.getMaxValue());

                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

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

    public void clicqueBtnCompetences(View w){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Vos compétences :");

        LinearLayout layoutGlobal = new LinearLayout(this);
        layoutGlobal.setOrientation(LinearLayout.VERTICAL);

        for(int i = 0; i< listcomp.size();i++){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            TextView text = new TextView(this);
            text.setText(listcomp.get(i).getNom()+"  ");

            ProgressBar progbar = new ProgressBar(getApplicationContext(), null, android.R.attr.progressBarStyleHorizontal);
            progbar.setProgress(listcomp.get(i).getTauxMaitrise());


            TextView text2 = new TextView(this);
            text2.setText(listcomp.get(i).getTauxMaitrise()+ " ");

            layout.addView(text);
            layout.addView(text2);

            layoutGlobal.addView(layout);
            layoutGlobal.addView(progbar);
        }

        alertDialogBuilder.setView(layoutGlobal);
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();

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

    public void cliqueBtnDate(View w){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Vos prochains test :");

        LinearLayout layout = new LinearLayout(this);
        layout.setGravity(Gravity.CENTER);
        for(int i=0; i < application.getCalendrier().getPartiels().size();i++){
            if(application.getCalendrier().getJourDuMois() <=application.getCalendrier().getPartiels().get(i).getDateDuDS().getDayOfMonth()  && application.getCalendrier().getIntMois() <= application.getCalendrier().getPartiels().get(i).getDateDuDS().getMonth()){
                TextView textDS = new TextView(this);
                textDS.setText(application.getCalendrier().getPartiels().get(i).getNom() +" le " +application.getCalendrier().getPartiels().get(i).getDateDuDS().getDayOfMonth() + " "+application.getCalendrier().getPartiels().get(i).getMoisDuDS());
                textDS.setGravity(Gravity.CENTER);
                layout.addView(textDS);

                TextView textDS2 = new TextView(this);
                textDS2.setText("Requiert une competence de : "+ application.getCalendrier().getPartiels().get(i).getTauxRequis());
                textDS2.setGravity(Gravity.CENTER);
                layout.addView(textDS2);
            }

        }



        alertDialogBuilder.setView(layout);
        AlertDialog alertD = alertDialogBuilder.create();




        alertD.show();
    }

    public void clickBtnCours(View w){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        //Variable permettant de savoir si le joueur a un DS aujourd'hui
        boolean ds = false;
        int o=0;
        for(int i=0; i < application.getCalendrier().getPartiels().size();i++){
            if(application.getCalendrier().getJourDuMois() == application.getCalendrier().getPartiels().get(i).getDateDuDS().getDayOfMonth()  && application.getCalendrier().getIntMois() == application.getCalendrier().getPartiels().get(i).getDateDuDS().getMonth()){
                ds = true;
                o =i;
            }
        }
        final Partiel partielAPasser =  application.getCalendrier().getPartiels().get(o);
        if(application.getCalendrier().getHeure() <= 8 && ds){
            //PARTIE POUR ALLER EN DS
            alertDialogBuilder.setTitle("Voulez vous allez faire votre examen (vous reviendrez à 17 heures) ?");
            alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    //Passer l'heure à 17
                    application.getCalendrier().setHeureDeLaJournee(17);

                    //Modification des stats de l'utilisateur d'energies
                    application.getUtilisateur().getEnergie().setTaux(application.getUtilisateur().getEnergie().getTaux()/2);
                    application.getUtilisateur().getHygiene().setTaux(application.getUtilisateur().getHygiene().getTaux()/2);
                    application.getUtilisateur().getSatiete().setTaux(application.getUtilisateur().getSatiete().getTaux()/2);

                    //Verification de la réussite de l'utilisateur
                    if(partielAPasser.getCompetenceAPasser().getTauxMaitrise() >= partielAPasser.getTauxRequis()){
                        int argentEnPlus;
                        argentEnPlus = partielAPasser.getCompetenceAPasser().getTauxMaitrise() -partielAPasser.getTauxRequis();
                        application.getUtilisateur().ajouterArgent(argentEnPlus);

                        Toast.makeText(ChambreActivity.this,"Vous avec réussi votre examen, et avez empoché "+ argentEnPlus + " $",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        application.getUtilisateur().setNombreDSManque(application.getUtilisateur().getNombreDSManque()+1);
                        Toast.makeText(ChambreActivity.this,"Vous avec raté votre examen plus que "+ (8-application.getUtilisateur().getNombreDSManque()) + " echecs avant l'exclusion",Toast.LENGTH_SHORT).show();

                    }
                }
            });
            alertDialogBuilder.setNegativeButton("J'abandonne cet examen", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    application.getCalendrier().setHeureDeLaJournee(17);
                    application.getUtilisateur().setNombreDSManque(application.getUtilisateur().getNombreDSManque()+1);

                    Toast.makeText(ChambreActivity.this,"Vous décidez de ne pas aller en examen, il ne vous reste plus que "+ (8-application.getUtilisateur().getNombreDSManque()) + " echecs avant l'exclusion",Toast.LENGTH_SHORT).show();
                    partielAPasser.setReussit(false);
                }
            });
        }
        else if(application.getCalendrier().getHeure() <= 8){
            //PARTIE POUR ALLER EN COURS
            alertDialogBuilder.setTitle("Voulez vous allez en cours (vous reviendrez à 17 heures) ?");
            alertDialogBuilder.setPositiveButton("Oui", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    //Passer l'heure à 17
                    application.getCalendrier().setHeureDeLaJournee(17);

                    //Modification des stats de l'utilisateur d'energies
                    application.getUtilisateur().getEnergie().setTaux(application.getUtilisateur().getEnergie().getTaux()/2);
                    application.getUtilisateur().getHygiene().setTaux(application.getUtilisateur().getHygiene().getTaux()/2);
                    application.getUtilisateur().getSatiete().setTaux(application.getUtilisateur().getSatiete().getTaux()/2);

                    //Modification des stats de l'utlisateur
                    for(int i =0;i < application.getUtilisateur().getCompetences().size();i++){
                        if(application.getUtilisateur().getHumeur().getTaux() <= 25){
                            application.getUtilisateur().getCompetences().get(i).augmenterTaux(5);
                        }
                        else if(application.getUtilisateur().getHumeur().getTaux() <= 50){
                            application.getUtilisateur().getCompetences().get(i).augmenterTaux(10);
                        }
                        else if(application.getUtilisateur().getHumeur().getTaux() <= 75){
                            application.getUtilisateur().getCompetences().get(i).augmenterTaux(15);
                        }
                        else if(application.getUtilisateur().getHumeur().getTaux() > 75){
                            application.getUtilisateur().getCompetences().get(i).augmenterTaux(20);
                        }
                    }

                    Toast.makeText(ChambreActivity.this,"Vous êtes allé en cours",Toast.LENGTH_SHORT).show();
                }
            });
            alertDialogBuilder.setNegativeButton("Non", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(ChambreActivity.this,"Vous décidez de ne pas aller en cours pour le moment",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            //PARTIE POUR DIRE QUE L'UTILISATEUR A RATE LE COURS
            alertDialogBuilder.setTitle(" Le cours de la journée est déjà passé !");
            alertDialogBuilder.setPositiveButton("Retour", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }





        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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


    private void init_partiels(){
        Date dateDSAda = new Date();
        dateDSAda.setDayOfMonth(2);
        dateDSAda.setMinute(0);
        dateDSAda.setYear(2020);
        dateDSAda.setMonth(9);

        Partiel partielAda = new Partiel("Examen d'ada",5,application.getUtilisateur().getCompetences().get(0),this,dateDSAda);
        application.getCalendrier().ajouterPartiel(partielAda);
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
