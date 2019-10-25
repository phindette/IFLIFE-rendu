package com.example.myapplication.Modele;

import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@Entity
public class Date {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "month")
    private int month;

    @ColumnInfo(name = "dayOfMonth")
    private int dayOfMonth;

    @ColumnInfo(name = "hourOfDay")
    private int hourOfDay;

    @ColumnInfo(name = "minute")
    private int minute;

    public Date(int year, int month, int dayOfMonth, int hourOfDay,int minute ){
        setYear(year);
        setMonth(month);
        setDayOfMonth(dayOfMonth);
        setHourOfDay(hourOfDay);
        setMinute(minute);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
