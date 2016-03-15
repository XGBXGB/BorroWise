package com.example.shayanetan.borrowise2.Models;

/**
 * Created by kewpe on 10 Mar 2016.
 */
public class MoneyTransaction extends Transaction{
    public static final String TABLE_NAME = "money_transaction";
    public static final String COLUMN_TOTAL_AMOUNT_DUE = "total_amount_due";
    public static final String COLUMN_AMOUNT_DEFICIT= "amount_deficit";
    public static final String COLUMN_TRANSACTION_ID = "transaction_id";

    private double totalAmountDue;
    private double amountDeficit;


    public MoneyTransaction(){ super();}
    public MoneyTransaction(String classification, int userID, String type, int status, long startDate, long dueDate, long returnDate, double rate,
                            double totalAmountDue, double amountDeficit) {
        super(classification, userID, type, status, startDate, dueDate, returnDate, rate);
        this.totalAmountDue = totalAmountDue;
        this.amountDeficit = amountDeficit;
    }

    public double getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(double totalAmountDue) {
        this.totalAmountDue = totalAmountDue;
    }

    public double getAmountDeficit() {
        return amountDeficit;
    }

    public void setAmountDeficit(double amountDeficit) {
        this.amountDeficit = amountDeficit;
    }
}
