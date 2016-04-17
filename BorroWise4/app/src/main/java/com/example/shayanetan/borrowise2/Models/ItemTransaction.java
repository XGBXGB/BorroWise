package com.example.shayanetan.borrowise2.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by kewpe on 10 Mar 2016.
 */
public class ItemTransaction extends Transaction {
    public static final String TABLE_NAME = "item_transaction";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PHOTOPATH = "photo_path";
    public static final String COLUMN_TRANSACTION_ID = "transaction_id";

    public static final BitmapFactory.Options bmpOptions  = new BitmapFactory.Options();

    private String name;
    private String description;
    private String photoPath;

    public ItemTransaction(String classification, int userID, String type, int status, long startDate, long dueDate, long returnDate, double rate,
                           String name, String description, String photoPath) {
        super(classification, userID, type, status, startDate, dueDate, returnDate, rate);
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
