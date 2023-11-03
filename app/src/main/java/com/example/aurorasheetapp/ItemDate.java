package com.example.aurorasheetapp;

public class ItemDate {
    int year, month, day;
    public ItemDate(int day, int month, int year){
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public ItemDate(String time){
        String splice[] = time.split( "[\\s-/]+" );
        this.year = Integer.parseInt(splice[2]);
        this.month = Integer.parseInt(splice[1]);
        this.day = Integer.parseInt(splice[0]);
    }
    public void setDate(String time){
        String splice[] = time.split( "[\\s-/]+" );
        this.year = Integer.parseInt(splice[2]);
        this.month = Integer.parseInt(splice[1]);
        this.day = Integer.parseInt(splice[0]);
    }
    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String toString(){

        return day + "-" + month + "-" + year;
    }
}