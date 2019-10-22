package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.myapplication.Modele.MyApplication;

public class ChambreActivity extends AppCompatActivity {
    public MyApplication application;
    public TextView nomJournee;
    public TextView nomMois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chambre);
        application = new MyApplication(); //ligne à changer avec l'initialisation database
        nomJournee = (TextView) findViewById(R.id.textDayOfTheWeek);
        nomMois = (TextView) findViewById(R.id.textDay);

        nomJournee.setText(application.getCalendrier().getJour());
        nomMois.setText(application.getCalendrier().getDate());
    }

    public void seDoucher(){
        application.prendreDouche();
    }
}
