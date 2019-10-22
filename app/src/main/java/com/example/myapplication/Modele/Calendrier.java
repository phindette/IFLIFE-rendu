package com.example.myapplication.Modele;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class Calendrier {
    private Calendar calendar;
    private ArrayList<String> jours;
    private SimpleDateFormat sdf;

    public Calendrier(){
        sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
        calendar = new GregorianCalendar(2020,9,1,7,0,0);
        jours = new ArrayList<>();
        List<String> namesList = Arrays.asList( "lundi", "mardi", "mercredi","jeudi","vendredi","samedi","dimanche");
        jours.addAll(namesList);
    }

    public Calendrier(int year, int month, int dayOfMonth, int hourOfDay, int minute){
        sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
        calendar = new GregorianCalendar(year,month,dayOfMonth,hourOfDay,minute,0);
        jours = new ArrayList<>();
        List<String> namesList = Arrays.asList( "lundi", "mardi", "mercredi","jeudi","vendredi","samedi","dimanche");
        jours.addAll(namesList);
    }

    public void ajouterJour(int n){
        calendar.add(Calendar.DAY_OF_MONTH, n);
    }
    public void ajouterHeure(int n){
        calendar.add(Calendar.HOUR_OF_DAY, n);
    }
    public void ajouterMinutes(int n){
        calendar.add(Calendar.MINUTE, n);
    }

    public String getJour(){
        return jours.get(calendar.get(Calendar.DAY_OF_WEEK));
    }
    public int getJourDuMois(){ return calendar.get(Calendar.DAY_OF_MONTH);
    }


    public String getDate(){
        return sdf.format(calendar.getTime());
    }
}
