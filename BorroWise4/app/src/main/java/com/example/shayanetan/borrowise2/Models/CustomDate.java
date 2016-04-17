package com.example.shayanetan.borrowise2.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public class CustomDate {

    private int month;
    private int year;
    private int day;

    public CustomDate(){
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public String formatDateCommas(String toParse){
       String result = "";
        try {
            SimpleDateFormat curFormatter = new SimpleDateFormat("MM/dd/yyyy");
            Date dateObj = curFormatter.parse(toParse);
            SimpleDateFormat postFormatter = new SimpleDateFormat("MMM dd, yyyy");
            result = postFormatter.format(dateObj);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String formatDateSlash(String toParse){

        String result = "";
        try {
            SimpleDateFormat curFormatter = new SimpleDateFormat("MM/dd/yyyy");
            Date dateObj = curFormatter.parse(toParse);
            SimpleDateFormat postFormatter = new SimpleDateFormat("MM/dd/yyyy");
            result = postFormatter.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }



}
