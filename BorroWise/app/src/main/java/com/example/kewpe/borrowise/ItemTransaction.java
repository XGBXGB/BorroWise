package com.example.kewpe.borrowise;

/**
 * Created by kewpe on 10 Mar 2016.
 */
public class ItemTransaction extends Transaction {
    public static final String TABLE_NAME = "item_transaction";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TRANSACTION_ID = "transaction_id";

    private String name;
    private String description;

    public ItemTransaction(String classification, int userID, String type, int status, long startDate, long dueDate, double rate,
                           String name, String description) {
        super(classification, userID, type, status, startDate, dueDate, rate);
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
