package com.example.myapplication.Modele;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Calendrier {
    private Calendar calendar;
    private ArrayList<String> jours;
    private SimpleDateFormat sdf;
    private ArrayList<Partiel> partiels;

    public Calendrier(){
        sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
        setCalendar(new GregorianCalendar(2020,9,1,7,0,0));
        jours = new ArrayList<>();
        List<String> namesList = Arrays.asList( "lundi", "mardi", "mercredi","jeudi","vendredi","samedi","dimanche");
        jours.addAll(namesList);
        partiels = new ArrayList<>();
    }

    public Calendrier(int year, int month, int dayOfMonth, int hourOfDay, int minute){
        sdf = new SimpleDateFormat("yyyy MMM dd HH:mm");
        setCalendar(new GregorianCalendar(year,month,dayOfMonth,hourOfDay,minute,0));
        jours = new ArrayList<>();
        List<String> namesList = Arrays.asList( "lundi", "mardi", "mercredi","jeudi","vendredi","samedi","dimanche");
        jours.addAll(namesList);
    }

    public void ajouterJour(int n){
        getCalendar().add(Calendar.DAY_OF_MONTH, n);
    }

    public void ajouterHeure(int n){
        getCalendar().add(Calendar.HOUR,n);}

    public void ajouterMinutes(int n){
        getCalendar().add(Calendar.MINUTE, n);
    }

    public String getJour(){
        String dayLongName = getCalendar().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        return dayLongName;
    }

    public int getJourDuMois(){ return getCalendar().get(Calendar.DAY_OF_MONTH);
    }

    public int getIntMois(){return getCalendar().get(Calendar.MONTH);}


    public int getHeure(){return getCalendar().get(Calendar.HOUR_OF_DAY);}
    public int getMinutes(){return getCalendar().get(Calendar.MINUTE);}

    public String getDate(){
        return sdf.format(getCalendar().getTime());
    }

    public void ajouterPartiel(Partiel p){
        partiels.add(p);

    }

    public ArrayList<Partiel> getPartiels(){
        return partiels;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
