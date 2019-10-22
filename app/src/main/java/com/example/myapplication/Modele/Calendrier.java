package com.example.myapplication.Modele;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Calendrier {
    private Calendar date;

    public Calendrier(String nom){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar(2018,9,1,7,0,0);
    }
}
