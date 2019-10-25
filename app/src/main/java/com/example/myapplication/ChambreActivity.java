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
import com.example.myapplication.Modele.DataBaseClient;
import com.example.myapplication.Modele.Date;
import com.example.myapplication.Modele.Livres;
import com.example.myapplication.Modele.MyApplication;
import com.example.myapplication.Modele.Nourriture;
import com.example.myapplication.Modele.Partiel;
import com.example.myapplication.Modele.Statistique;

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

        //Initialisation des compétences et des livres (à modif en DAO) ->fait
        init_competences();
        init_livres();

        //Initialisation des examens
        init_partiels();

        //Ligne de test pour les barres
        //application.getUtilisateur().augmenterSatiete(150);

        // get la database
        final DataBaseClient mDb = DataBaseClient.getInstance(getApplicationContext());

        // reprendre le jeu à la dernière save
        class GetLastDate extends AsyncTask<Void, Void, Date> {

            @Override
            protected Date doInBackground(Void... voids) {
                Date d = mDb.getAppDatabase().DateDao().getLast();
                return d;
            }
        }

        try {
            Date d =new GetLastDate().execute().get();
            if(d != null){
                application.getCalendrier().setByDate(d);
            }
        }catch(Exception e){
            application.getCalendrier().setByDate(new Date(2020,9,1,7,0));
        }

        // reprendre les statistiques
        class GetStats extends AsyncTask<Void, Void, ArrayList<Statistique>> {

            @Override
            protected ArrayList<Statistique> doInBackground(Void... voids) {
                ArrayList<Statistique> lSt = new ArrayList<>();
                lSt.add(mDb.getAppDatabase().StatistiqueDao().getLastByName("Energie"));
                lSt.add(mDb.getAppDatabase().StatistiqueDao().getLastByName("Satiété"));
                lSt.add(mDb.getAppDatabase().StatistiqueDao().getLastByName("Humeur"));
                lSt.add(mDb.getAppDatabase().StatistiqueDao().getLastByName("Hygiène"));
                return lSt;
            }
        }

        try {
            ArrayList<Statistique> ls = new GetStats().execute().get();
            application.getUtilisateur().getEnergie().setTaux(ls.get(0).getTaux());
            application.getUtilisateur().getSatiete().setTaux(ls.get(1).getTaux());
            application.getUtilisateur().getHumeur().setTaux(ls.get(2).getTaux());
            application.getUtilisateur().getHygiene().setTaux(ls.get(3).getTaux());
        }catch(Exception e){
        }

        //reprendre les competences
        class GetComps extends AsyncTask<Void, Void, ArrayList<Competences>> {

            @Override
            protected ArrayList<Competences> doInBackground(Void... voids) {
                ArrayList<Competences> lCp = new ArrayList<>();
                lCp.addAll(mDb.getAppDatabase().CompetenceDao().getAll());
                return lCp;
            }
        }

        try {
            ArrayList<Competences> lCp = new GetComps().execute().get();
            if(lCp.isEmpty() == false){
                application.getUtilisateur().getCompetences().clear();
                application.getUtilisateur().getCompetences().addAll(lCp);
            }
        }catch(Exception e){
        }

        //PARTIE CONTROLE
        jeu();



    }

    //sauvegarder l'heure
    private void saveHeure() {
        final DataBaseClient mDb = DataBaseClient.getInstance(getApplicationContext());
        final Date d = new Date(application.getCalendrier().getAnnee(), application.getCalendrier().getIntMois(), application.getCalendrier().getJourDuMois(), application.getCalendrier().getHeure(), application.getCalendrier().getMinutes());
        class saveHeure extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                mDb.getAppDatabase().DateDao().insert(d);
                return null;
            }
        }
        saveHeure sv = new saveHeure();
        sv.execute();
    }

//    //sauvegarder l'utilisateur
//    private void saveUser() {
//        final DataBaseClient mDb = DataBaseClient.getInstance(getApplicationContext());
//        class saveHeure extends AsyncTask<Void, Void, Void> {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                mDb.getAppDatabase().DateDao().insert(d);
//                return null;
//            }
//        }
//        saveHeure sv = new saveHeure();
//        sv.execute();
//    }

    //sauvegarder les statistiques
    private void saveStat(){
        final DataBaseClient mDb = DataBaseClient.getInstance(getApplicationContext());
          class saveStat extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                mDb.getAppDatabase().StatistiqueDao().insert(application.getUtilisateur().getEnergie());
                mDb.getAppDatabase().StatistiqueDao().insert(application.getUtilisateur().getHumeur());
                mDb.getAppDatabase().StatistiqueDao().insert(application.getUtilisateur().getHygiene());
                mDb.getAppDatabase().StatistiqueDao().insert(application.getUtilisateur().getSatiete());

                return null;
            }
        }
        saveStat st = new saveStat();
        st.execute();
    }

    //sauvegarder les competences
    private void saveComp(){
        final DataBaseClient mDb = DataBaseClient.getInstance(getApplicationContext());
        class saveComp extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                for(Competences cpt : application.getUtilisateur().getCompetences()){
                    mDb.getAppDatabase().CompetenceDao().update(cpt);
                }
                return null;
            }
        }
        saveComp sc = new saveComp();
        sc.execute();
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
                    saveComp();
                    saveHeure();
                    saveStat();
                }
            }
        };

        thread.start();




    }


    public void cliqueManger(View w) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChambreActivity.this);
        alertDialogBuilder.setTitle("Que voulez-vous manger?");

        ScrollView scrollView = new ScrollView(this);
        LinearLayout layoutGlobal = new LinearLayout(this);
        layoutGlobal.setOrientation(LinearLayout.VERTICAL);

        for(Nourriture n : application.getUtilisateur().getInv_Nourriture().keySet()){
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);

            final Button bouton = new Button(this);
            bouton.setText(n.getNom() + " X "+application.getUtilisateur().getInv_Nourriture().get(n)+" qui restaure "+n.getMontantRegen()+" points");

            final Nourriture nourri = n;
            bouton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    application.getUtilisateur().manger(nourri);
                    if(application.getUtilisateur().getInv_Nourriture().get(nourri) == null){
                        v.setVisibility(View.INVISIBLE);
                    }else{
                        bouton.setText(nourri.getNom() + " X "+application.getUtilisateur().getInv_Nourriture().get(nourri)+" qui restaure "+nourri.getMontantRegen()+" points");
                    }

                }
            });
            layout.addView(bouton);
            layoutGlobal.addView(layout);
        }
        scrollView.addView(layoutGlobal);
        alertDialogBuilder.setView(scrollView);
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();


        //mettre a jour la db
        saveStat();


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

        saveComp();

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
                    Toast.makeText(ChambreActivity.this,"Vous allez réviser : "+numberPicker.getValue()+" heures",Toast.LENGTH_SHORT).show();

                    //Gestion du modèle
                    application.getUtilisateur().reviser(livre,numberPicker.getValue());
                    application.getUtilisateur().getEnergie().setTaux(application.getUtilisateur().getEnergie().getTaux()/2);
                    application.getUtilisateur().getHygiene().setTaux(application.getUtilisateur().getHygiene().getTaux()/2);
                    application.getUtilisateur().getSatiete().setTaux(application.getUtilisateur().getSatiete().getTaux()/2);
                    application.getCalendrier().ajouterHeure(numberPicker.getValue());

                    //mettre a jour la db
                    saveHeure();
                    saveStat();
                    saveComp();
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

                //mettre à jour la db
                saveStat();
                saveHeure();
            }
        });



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void clicqueBtnCompetences(View w){

        this.listcomp = application.getUtilisateur().getCompetences();

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
        saveHeure();
        saveStat();
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
        this.listcomp = application.getUtilisateur().getCompetences();
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

                    //mettre a jour la db
                    saveStat();
                    saveHeure();

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
                    //mettre a jour la db
                    saveHeure();

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
                    //mettre a jour la db
                    saveStat();
                    saveHeure();
                    saveComp();

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
            regen.setText(nourritures.get(i).getNom()+" restore "+nourritures.get(i).getMontantRegen() + " points de nourriture");
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
        Date dateDSAda = new Date(2020,9,2,8,0);

        Partiel partielAda = new Partiel("Examen d'ada",5,application.getUtilisateur().getCompetences().get(0),this,dateDSAda);
        application.getCalendrier().ajouterPartiel(partielAda);
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

        // Récupération du DatabaseClient
        final DataBaseClient mDb = DataBaseClient.getInstance(getApplicationContext());

        class GetCompetences extends AsyncTask<Void, Void, List<Competences>> {

            @Override
            protected List<Competences> doInBackground(Void... voids) {
                List<Competences> c = mDb.getAppDatabase()
                        .CompetenceDao()
                        .getAll();
                return c;
            }
        }

        try {
            listcomp.addAll(new GetCompetences().execute().get());
            for(Competences cpt : listcomp){
                System.out.println(cpt.getNom());
                application.getUtilisateur().addCompetence(cpt);
            }
        }catch(Exception e){
        }

    }

    private void init_livres(){

        // Récupération du DatabaseClient
        final DataBaseClient mDb = DataBaseClient.getInstance(getApplicationContext());

        class GetNourriture extends AsyncTask<Void, Void, List<Livres>> {

            @Override
            protected List<Livres> doInBackground(Void... voids) {
                List<Livres> c = mDb.getAppDatabase()
                        .LivresDao()
                        .getAll();
                return c;
            }
        }

        try {
            livres.addAll(new GetNourriture().execute().get());
            for(Livres l : livres){

                l.construireLivre(getApplicationContext());
            }
        }catch(Exception e){
        }
    }
}
