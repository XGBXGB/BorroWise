package com.example.shayanetan.borrowise.Models;

/**
 * Created by kewpe on 10 Mar 2016.
 */
public class User {
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_CONTACT_INFO = "contact_info";
    public static final String COLUMN_TOTAL_RATE = "total_rate";

    private int id;
    private String name;
    private String contactInfo;
    private double totalRate;

    public User(){

    }

    public User(String name, String contactInfo, double totalRate) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.totalRate = totalRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public double getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(double totalRate) {
        this.totalRate = totalRate;
    }
}
