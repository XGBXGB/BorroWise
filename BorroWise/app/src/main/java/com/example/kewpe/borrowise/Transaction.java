package com.example.kewpe.borrowise;

/**
 * Created by kewpe on 10 Mar 2016.
 */
public abstract class Transaction {
    public static final String TABLE_NAME = "transaction";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CLASSIFICATION = "classification";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_RATE = "rate";

    protected int id;
    protected String classification;
    protected int userID;
    protected String type;
    protected int status;
    protected long startDate;
    protected long dueDate;
    protected double rate;

    public Transaction(String classification, int userID, String type, int status, long startDate, long dueDate, double rate) {
        this.classification = classification;
        this.userID = userID;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.rate = rate;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int user_id) {
        this.userID = userID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
