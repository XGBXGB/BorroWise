package com.example.shayanetan.borrowise.Models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by ShayaneTan on 3/12/2016.
 */
public class CustomDate {

    private int month;
    private int year;
    private int day;

    public CustomDate(){
        // get the supported ids for GMT-08:00 (Pacific Standard Time)
        String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
        // if no ids were returned, something is wrong. get out.
        if (ids.length == 0)
            System.exit(0);
        // create a Pacific Standard Time time zone
        SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);

        // set up rules for daylight savings time
        pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
        pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

        // create a GregorianCalendar with the Pacific Daylight time zone
        // and the current date and time
        Calendar calendar = new GregorianCalendar(pdt);
        this.year = calendar.get(Calendar.YEAR);
        this.day= calendar.get(Calendar.MONTH);
        this.month = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public String getCurrentDate(){
        return month + "/" + day + "/" + year;
    }

    public String formatDateCommas(String toParse){
       String result = "";
        try {
            SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
            Date dateObj = curFormater.parse(toParse);
            SimpleDateFormat postFormater = new SimpleDateFormat("MM/dd/yyyy");
            result = postFormater.format(dateObj);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String formatDateSlash(String toParse){

        String result = "";
        try {
            SimpleDateFormat curFormatter = new SimpleDateFormat("dd/MM/yyyy");
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
