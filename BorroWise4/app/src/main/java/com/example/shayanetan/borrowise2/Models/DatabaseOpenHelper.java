package com.example.shayanetan.borrowise2.Models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by XGB on 3/11/2016.
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String SCHEMA = "borrowlend";
    private static DatabaseOpenHelper dbInstance;

    public static synchronized DatabaseOpenHelper getInstance(Context context) {

        if (dbInstance == null) {
            dbInstance = new DatabaseOpenHelper(context.getApplicationContext());
        }
        return dbInstance;
    }
    public DatabaseOpenHelper(Context context)
    {
        //super(context, name, cursorfactory, version);
        super(context, SCHEMA, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + User.TABLE_NAME + " ("
                + User.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + User.COLUMN_NAME + " TEXT, "
                + User.COLUMN_CONTACT_INFO + " TEXT, "
                + User.COLUMN_TOTAL_RATE + " REAL); ";
        String sql2 = "CREATE TABLE " + Transaction.TABLE_NAME + " ("
                + Transaction.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Transaction.COLUMN_CLASSIFICATION + " TEXT, "
                + Transaction.COLUMN_USER_ID + " INTEGER, "
                + Transaction.COLUMN_TYPE + " TEXT, "
                + Transaction.COLUMN_STATUS + " INTEGER, "
                + Transaction.COLUMN_START_DATE + " INTEGER, "
                + Transaction.COLUMN_DUE_DATE + " INTEGER, "
                + Transaction.COLUMN_RETURN_DATE + " INTEGER, "
                + Transaction.COLUMN_RATE + " REAL); ";
        String sql3 = "CREATE TABLE " + ItemTransaction.TABLE_NAME + " ("
                + ItemTransaction.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemTransaction.COLUMN_NAME + " TEXT, "
                + ItemTransaction.COLUMN_DESCRIPTION + " TEXT, "
                + ItemTransaction.COLUMN_PHOTOPATH + " TEXT, "
                + ItemTransaction.COLUMN_TRANSACTION_ID + " INTEGER, "
                + "FOREIGN KEY("+ItemTransaction.COLUMN_TRANSACTION_ID+") REFERENCES transactions(id)); ";
        String sql4 = "CREATE TABLE " + MoneyTransaction.TABLE_NAME + " ("
                + MoneyTransaction.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE + " REAL, "
                + MoneyTransaction.COLUMN_AMOUNT_DEFICIT + " REAL, "
                + MoneyTransaction.COLUMN_TRANSACTION_ID + " INTEGER, "
                + "FOREIGN KEY("+MoneyTransaction.COLUMN_TRANSACTION_ID+") REFERENCES transactions(id)); ";

        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //when version number is increased, this is called
        String sql = "DROP TABLE IF EXISTS " + User.TABLE_NAME+"; ";
        String sql2 = "DROP TABLE IF EXISTS " + Transaction.TABLE_NAME+"; ";
        String sql3 = "DROP TABLE IF EXISTS " + ItemTransaction.TABLE_NAME+"; ";
        String sql4 = "DROP TABLE IF EXISTS " + MoneyTransaction.TABLE_NAME+"; ";
        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);
        db.execSQL(sql4);
        onCreate(db);
    }

    /*
    *INSERT MODULE
    */
    public long insertUser(User u){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(User.COLUMN_NAME, u.getName());
        cv.put(User.COLUMN_CONTACT_INFO, u.getContactInfo());
        cv.put(User.COLUMN_TOTAL_RATE, u.getTotalRate());
        long id = db.insert(User.TABLE_NAME, null, cv);
        return id;
    }

    public long insertTransaction(Transaction t){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Transaction.COLUMN_CLASSIFICATION, t.getClassification());
        cv.put(Transaction.COLUMN_USER_ID, t.getUserID());
        cv.put(Transaction.COLUMN_TYPE, t.getType());
        cv.put(Transaction.COLUMN_STATUS, t.getStatus());
        cv.put(Transaction.COLUMN_START_DATE, t.getStartDate());
        cv.put(Transaction.COLUMN_DUE_DATE, t.getDueDate());
        cv.put(Transaction.COLUMN_RETURN_DATE, t.getReturnDate());
        cv.put(Transaction.COLUMN_RATE, t.getRate());

        long id = db.insert(Transaction.TABLE_NAME, null, cv);

        cv = new ContentValues();
        if(t.getClassification().equalsIgnoreCase(Transaction.ITEM_TYPE)){
            cv.put(ItemTransaction.COLUMN_NAME, ((ItemTransaction) t).getName());
            cv.put(ItemTransaction.COLUMN_DESCRIPTION, ((ItemTransaction)t).getDescription());
            cv.put(ItemTransaction.COLUMN_PHOTOPATH, ((ItemTransaction) t).getPhotoPath());
            cv.put(ItemTransaction.COLUMN_TRANSACTION_ID, id);
            db.insert(ItemTransaction.TABLE_NAME, null, cv);
        }else{
            cv.put(MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE, ((MoneyTransaction) t).getTotalAmountDue());
            cv.put(MoneyTransaction.COLUMN_AMOUNT_DEFICIT, ((MoneyTransaction) t).getAmountDeficit());
            cv.put(MoneyTransaction.COLUMN_TRANSACTION_ID, id);
            db.insert(MoneyTransaction.TABLE_NAME, null, cv);
        }
        return id;
    }
    /*
    *INSERT MODULE
    */


    /*
    *QUERY OBJECT MODULE
    */
    public User queryUser(int id)
    {
        User u = null;
        SQLiteDatabase db = getReadableDatabase();;
        Cursor c =   db.query(User.TABLE_NAME,
                null, // == *
                " " + User.COLUMN_ID + "= ? ", //WHERE _ = ?
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if(c.moveToFirst())
        {
            u = new User();
            u.setName(c.getString(c.getColumnIndex(User.COLUMN_NAME)));
            u.setContactInfo(c.getString(c.getColumnIndex(User.COLUMN_CONTACT_INFO)));
            u.setTotalRate(c.getDouble(c.getColumnIndex(User.COLUMN_TOTAL_RATE)));
            u.setId(id);
        }else{
            u = null;
        }

        return u;
    }

    public User queryUser(String name)
    {
        User u = null;
        SQLiteDatabase db = getReadableDatabase();;
        Cursor c =   db.query(User.TABLE_NAME,
                null, // == *
                " " + User.COLUMN_NAME + "= ? ", //WHERE _ = ?
                new String[]{name},
                null,
                null,
                null);

        if(c.moveToFirst())
        {
            u = new User();
            u.setName(c.getString(c.getColumnIndex(User.COLUMN_NAME)));
            u.setContactInfo(c.getString(c.getColumnIndex(User.COLUMN_CONTACT_INFO)));
            u.setTotalRate(c.getDouble(c.getColumnIndex(User.COLUMN_TOTAL_RATE)));
            u.setId(c.getInt(c.getColumnIndex(User.COLUMN_ID)));
        }else{
            u = null;
        }

        return u;
    }

    public Transaction queryTransaction(int id)
    {
        Transaction t = null;
        SQLiteDatabase db = getReadableDatabase();;
        Cursor c =   db.query(Transaction.TABLE_NAME,
                null, // == *
                " " + Transaction.COLUMN_ID + "= ? ", //WHERE _ = ?
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        if(c.moveToFirst())
        {
            if(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase("item")){
                Cursor c2 =   db.query(ItemTransaction.TABLE_NAME,
                        null, // == *
                        " " + ItemTransaction.COLUMN_TRANSACTION_ID + "= ? ", //WHERE _ = ?
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null);
                c2.moveToFirst();
                t = new ItemTransaction(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)),
                        c.getInt(c.getColumnIndex(Transaction.COLUMN_USER_ID)),
                        c.getString(c.getColumnIndex(Transaction.COLUMN_TYPE)),
                        c.getInt(c.getColumnIndex(Transaction.COLUMN_STATUS)),
                        c.getLong(c.getColumnIndex(Transaction.COLUMN_START_DATE)),
                        c.getLong(c.getColumnIndex(Transaction.COLUMN_DUE_DATE)),
                        c.getLong(c.getColumnIndex(Transaction.COLUMN_RETURN_DATE)),
                        c.getDouble(c.getColumnIndex(Transaction.COLUMN_RATE)),
                        c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_NAME)),
                        c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_DESCRIPTION)),
                        c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_PHOTOPATH)));
                t.setId(id);
            }else{
                Cursor c2 =   db.query(MoneyTransaction.TABLE_NAME,
                        null, // == *
                        " " + MoneyTransaction.COLUMN_TRANSACTION_ID + "= ? ", //WHERE _ = ?
                        new String[]{String.valueOf(id)},
                        null,
                        null,
                        null);
                c2.moveToFirst();
                t = new MoneyTransaction(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)),
                        c.getInt(c.getColumnIndex(Transaction.COLUMN_USER_ID)),
                        c.getString(c.getColumnIndex(Transaction.COLUMN_TYPE)),
                        c.getInt(c.getColumnIndex(Transaction.COLUMN_STATUS)),
                        c.getLong(c.getColumnIndex(Transaction.COLUMN_START_DATE)),
                        c.getLong(c.getColumnIndex(Transaction.COLUMN_DUE_DATE)),
                        c.getLong(c.getColumnIndex(Transaction.COLUMN_RETURN_DATE)),
                        c.getDouble(c.getColumnIndex(Transaction.COLUMN_RATE)),
                        c2.getDouble(c2.getColumnIndex(MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE)),
                        c2.getDouble(c2.getColumnIndex(MoneyTransaction.COLUMN_AMOUNT_DEFICIT)));
                t.setId(id);
            }
        }else{
            t = null;
        }
        return t;
    }
    /*
    *QUERY OBJECT MODULE
    */


    /*
    *QUERY CURSOR MODULE
    */
    public Cursor querryUsersType(String type, String status){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT "+User.COLUMN_ID+", "+User.COLUMN_NAME+", "+User.COLUMN_CONTACT_INFO+", "+User.COLUMN_TOTAL_RATE
                +" FROM "+User.TABLE_NAME
                +" WHERE "+User.COLUMN_ID+" IN ("
                +" SELECT "+Transaction.COLUMN_USER_ID
                +" FROM "+Transaction.TABLE_NAME
                +" WHERE "+Transaction.COLUMN_TYPE+"='"+type+"' AND "+Transaction.COLUMN_STATUS+" IN("+status+"))"
                , null);
        return cursor.moveToFirst() ? cursor : null;
    }


    public Cursor querryBorrowTransactionsJoinUser(String status){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_NAME + " AS Attribute1, " + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_DESCRIPTION + " AS Attribute2, "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_PHOTOPATH + " AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + ItemTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='borrow' AND "
                        + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") "
                        + " UNION "
                        + "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE + " AS Attribute1, " + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_AMOUNT_DEFICIT + " AS Attribute2, "
                        + "Null AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + MoneyTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='borrow' AND "
                        + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") "
                , null);
        return cursor.moveToFirst() ? cursor : null;
    }

    public Cursor querryBorrowTransactionsJoinUser(String status, String filter){

        SQLiteDatabase db = getReadableDatabase();
        String query = "";

        if(filter.equalsIgnoreCase(Transaction.ITEM_TYPE)){
            query =  "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                    + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                    + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                    + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                    + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_NAME + " AS Attribute1, " + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_DESCRIPTION + " AS Attribute2, "
                    + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_PHOTOPATH + " AS Attribute3, "
                    + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                    + " FROM " + Transaction.TABLE_NAME
                    + " INNER JOIN " + ItemTransaction.TABLE_NAME
                    + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_TRANSACTION_ID
                    + " INNER JOIN " + User.TABLE_NAME
                    + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                    + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='borrow' AND "
                    + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") ";
        }else{
            query = "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                    + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                    + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                    + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                    + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE + " AS Attribute1, " + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_AMOUNT_DEFICIT + " AS Attribute2, "
                    + "Null AS Attribute3, "
                    + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                    + " FROM " + Transaction.TABLE_NAME
                    + " INNER JOIN " + MoneyTransaction.TABLE_NAME
                    + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TRANSACTION_ID
                    + " INNER JOIN " + User.TABLE_NAME
                    + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                    + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='borrow' AND "
                    + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") ";
        }


        Cursor cursor = db.rawQuery(query , null);
        return cursor.moveToFirst() ? cursor : null;
    }


    public Cursor querryAllTransactionsJoinUser(String status){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_NAME + " AS Attribute1, " + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_DESCRIPTION + " AS Attribute2, "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_PHOTOPATH + " AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + ItemTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE "
                        + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") "
                        + " UNION "
                        + "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE + " AS Attribute1, " + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_AMOUNT_DEFICIT + " AS Attribute2, "
                        + "Null AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + MoneyTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE "
                        + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") "
                , null);
        return cursor.moveToFirst() ? cursor : null;
    }

    public Cursor querryLendTransactionsJoinUser(String status){

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_NAME + " AS Attribute1, " + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_DESCRIPTION + " AS Attribute2, "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_PHOTOPATH + " AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + ItemTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='lend' AND "
                        + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") "
                        + " UNION "
                        + "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE + " AS Attribute1, " + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_AMOUNT_DEFICIT + " AS Attribute2, "
                        + "Null AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + MoneyTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='lend' AND "
                        + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") "
                , null);
        return cursor.moveToFirst() ? cursor : null;
    }

    public Cursor querryLendTransactionsJoinUser(String status, String filter){

        SQLiteDatabase db = getReadableDatabase();
        String query = "";

        if(filter.equalsIgnoreCase(Transaction.ITEM_TYPE)){
            query =  "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                    + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                    + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                    + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                    + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_NAME + " AS Attribute1, " + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_DESCRIPTION + " AS Attribute2, "
                    + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_PHOTOPATH + " AS Attribute3, "
                    + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                    + " FROM " + Transaction.TABLE_NAME
                    + " INNER JOIN " + ItemTransaction.TABLE_NAME
                    + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_TRANSACTION_ID
                    + " INNER JOIN " + User.TABLE_NAME
                    + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                    + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='lend' AND "
                    + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") ";
        }else{
            query = "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                    + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                    + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                    + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                    + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE + " AS Attribute1, " + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_AMOUNT_DEFICIT + " AS Attribute2, "
                    + "Null AS Attribute3, "
                    + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name"
                    + " FROM " + Transaction.TABLE_NAME
                    + " INNER JOIN " + MoneyTransaction.TABLE_NAME
                    + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TRANSACTION_ID
                    + " INNER JOIN " + User.TABLE_NAME
                    + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                    + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='lend' AND "
                    + Transaction.TABLE_NAME + "." + Transaction.COLUMN_STATUS + " IN (" + status + ") ";
        }


        Cursor cursor = db.rawQuery(query , null);
        return cursor.moveToFirst() ? cursor : null;
    }


    public Cursor queryAllUsersC()
    {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(User.TABLE_NAME, null, null, null, null, null, null);

        if(c.moveToFirst())
        {
            do{
                User u = new User();
                u.setName(c.getString(c.getColumnIndex(User.COLUMN_NAME)));
                u.setContactInfo(c.getString(c.getColumnIndex(User.COLUMN_CONTACT_INFO)));
                u.setTotalRate(c.getDouble(c.getColumnIndex(User.COLUMN_TOTAL_RATE)));
                u.setId(c.getInt(c.getColumnIndex(User.COLUMN_ID)));
                users.add(u);
            }while(c.moveToNext());

        } else
        {
            users = null;
        }
        return c;
    }

    public Cursor queryAllTransactionsC()
    {
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Transaction.TABLE_NAME, null, null, null, null, null, null);

        if(c.moveToFirst())
        {
            do{

                Cursor c2;
                Transaction t;
                if(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase(Transaction.ITEM_TYPE)){
                    c2 =   db.query(ItemTransaction.TABLE_NAME,
                            null, // == *
                            " " + ItemTransaction.COLUMN_TRANSACTION_ID + "= ? ", //WHERE _ = ?
                            new String[]{String.valueOf(c.getInt(c.getColumnIndex(User.COLUMN_ID)))},
                            null,
                            null,
                            null);
                    c2.moveToFirst();
                    t = new ItemTransaction(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)),
                    c.getInt(c.getColumnIndex(Transaction.COLUMN_USER_ID)),
                    c.getString(c.getColumnIndex(Transaction.COLUMN_TYPE)),
                    c.getInt(c.getColumnIndex(Transaction.COLUMN_STATUS)),
                    c.getLong(c.getColumnIndex(Transaction.COLUMN_START_DATE)),
                    c.getLong(c.getColumnIndex(Transaction.COLUMN_DUE_DATE)),
                    c.getLong(c.getColumnIndex(Transaction.COLUMN_RETURN_DATE)),
                    c.getDouble(c.getColumnIndex(Transaction.COLUMN_RATE)),
                    c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_NAME)),
                    c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_DESCRIPTION)),
                    c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_PHOTOPATH)));
                }else{
                    c2 =   db.query(MoneyTransaction.TABLE_NAME,
                            null, // == *
                            " " + MoneyTransaction.COLUMN_TRANSACTION_ID + "= ? ", //WHERE _ = ?
                            new String[]{String.valueOf(c.getInt(c.getColumnIndex(User.COLUMN_ID)))},
                            null,
                            null,
                            null);
                    c2.moveToFirst();
                    t = new MoneyTransaction(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)),
                    c.getInt(c.getColumnIndex(Transaction.COLUMN_USER_ID)),
                    c.getString(c.getColumnIndex(Transaction.COLUMN_TYPE)),
                    c.getInt(c.getColumnIndex(Transaction.COLUMN_STATUS)),
                    c.getLong(c.getColumnIndex(Transaction.COLUMN_START_DATE)),
                    c.getLong(c.getColumnIndex(Transaction.COLUMN_DUE_DATE)),
                    c.getLong(c.getColumnIndex(Transaction.COLUMN_RETURN_DATE)),
                    c.getDouble(c.getColumnIndex(Transaction.COLUMN_RATE)),
                    c2.getDouble(c2.getColumnIndex(MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE)),
                    c2.getDouble(c2.getColumnIndex(MoneyTransaction.COLUMN_AMOUNT_DEFICIT)));
                }
                t.setId(c.getInt(c.getColumnIndex(User.COLUMN_ID)));
                transactions.add(t);
            }while(c.moveToNext());

        } else{
            transactions = null;
        }
        return c;
    }

    public Cursor queryAllLendTransactionsGivenUser(String user){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_NAME + " AS Attribute1, " + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_DESCRIPTION + " AS Attribute2, "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_PHOTOPATH + " AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name, "
                        + User.COLUMN_TOTAL_RATE
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + ItemTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='borrow' AND "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " LIKE '" + user + "'"
                        + " UNION "
                        + "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE + " AS Attribute1, " + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_AMOUNT_DEFICIT + " AS Attribute2, "
                        + "Null AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name, "
                        + User.COLUMN_TOTAL_RATE
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + MoneyTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='borrow' AND "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " LIKE '" + user + "'"
                , null);
        return cursor.moveToFirst() ? cursor : null;
    }


    public Cursor queryAllBorrowedTransactionsGivenUser(String user)
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_NAME + " AS Attribute1, " + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_DESCRIPTION + " AS Attribute2, "
                        + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_PHOTOPATH + " AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name, "
                        + User.COLUMN_TOTAL_RATE
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + ItemTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + ItemTransaction.TABLE_NAME + "." + ItemTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='lend' AND "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " LIKE '" + user+"'"
                        + " UNION "
                        + "SELECT " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + " AS _id, " + Transaction.COLUMN_CLASSIFICATION + ", "
                        + Transaction.COLUMN_USER_ID + ", " + Transaction.COLUMN_TYPE + ", "
                        + Transaction.COLUMN_STATUS + ", " + Transaction.COLUMN_START_DATE + ", "
                        + Transaction.COLUMN_DUE_DATE + ", " + Transaction.COLUMN_RETURN_DATE + ", " + Transaction.COLUMN_RATE + ", "
                        + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE + " AS Attribute1, " + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_AMOUNT_DEFICIT + " AS Attribute2, "
                        + "Null AS Attribute3, "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " AS name, "
                        + User.COLUMN_TOTAL_RATE
                        + " FROM " + Transaction.TABLE_NAME
                        + " INNER JOIN " + MoneyTransaction.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_ID + "=" + MoneyTransaction.TABLE_NAME + "." + MoneyTransaction.COLUMN_TRANSACTION_ID
                        + " INNER JOIN " + User.TABLE_NAME
                        + " ON " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_USER_ID + "=" + User.TABLE_NAME + "." + User.COLUMN_ID
                        + " WHERE " + Transaction.TABLE_NAME + "." + Transaction.COLUMN_TYPE + "='lend' AND "
                        + User.TABLE_NAME + "." + User.COLUMN_NAME + " LIKE '" + user +"'"
                , null);
        return cursor.moveToFirst() ? cursor : null;
    }
    /*
    *QUERY CURSOR MODULE
    */


    /*
    *QUERY ALL OBJECTS MODULE
    */
    public ArrayList<User> queryAllUsers()
    {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(User.TABLE_NAME, null, null, null, null, null, null);

        if(c.moveToFirst())
        {
            do{
                User u = new User();
                u.setName(c.getString(c.getColumnIndex(User.COLUMN_NAME)));
                u.setContactInfo(c.getString(c.getColumnIndex(User.COLUMN_CONTACT_INFO)));
                u.setTotalRate(c.getDouble(c.getColumnIndex(User.COLUMN_TOTAL_RATE)));
                u.setId(c.getInt(c.getColumnIndex(User.COLUMN_ID)));
                users.add(u);
            }while(c.moveToNext());

        } else
        {
            users = null;
        }
        return users;
    }

    public ArrayList<Transaction> queryAllTransactions()
    {
        ArrayList<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Transaction.TABLE_NAME, null, null, null, null, null, null);

        if(c.moveToFirst())
        {
            do{

                Cursor c2;
                Transaction t;
                if(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)).equalsIgnoreCase(Transaction.ITEM_TYPE)){
                    c2 =   db.query(ItemTransaction.TABLE_NAME,
                            null, // == *
                            " " + ItemTransaction.COLUMN_TRANSACTION_ID + "= ? ", //WHERE _ = ?
                            new String[]{String.valueOf(c.getInt(c.getColumnIndex(User.COLUMN_ID)))},
                            null,
                            null,
                            null);
                    c2.moveToFirst();
                    t = new ItemTransaction(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)),
                            c.getInt(c.getColumnIndex(Transaction.COLUMN_USER_ID)),
                            c.getString(c.getColumnIndex(Transaction.COLUMN_TYPE)),
                            c.getInt(c.getColumnIndex(Transaction.COLUMN_STATUS)),
                            c.getLong(c.getColumnIndex(Transaction.COLUMN_START_DATE)),
                            c.getLong(c.getColumnIndex(Transaction.COLUMN_DUE_DATE)),
                            c.getLong(c.getColumnIndex(Transaction.COLUMN_RETURN_DATE)),
                            c.getDouble(c.getColumnIndex(Transaction.COLUMN_RATE)),
                            c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_NAME)),
                            c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_DESCRIPTION)),
                            c2.getString(c2.getColumnIndex(ItemTransaction.COLUMN_PHOTOPATH)));
                }else{
                    c2 =   db.query(MoneyTransaction.TABLE_NAME,
                            null, // == *
                            " " + MoneyTransaction.COLUMN_TRANSACTION_ID + "= ? ", //WHERE _ = ?
                            new String[]{String.valueOf(c.getInt(c.getColumnIndex(User.COLUMN_ID)))},
                            null,
                            null,
                            null);
                    c2.moveToFirst();
                    t = new MoneyTransaction(c.getString(c.getColumnIndex(Transaction.COLUMN_CLASSIFICATION)),
                            c.getInt(c.getColumnIndex(Transaction.COLUMN_USER_ID)),
                            c.getString(c.getColumnIndex(Transaction.COLUMN_TYPE)),
                            c.getInt(c.getColumnIndex(Transaction.COLUMN_STATUS)),
                            c.getLong(c.getColumnIndex(Transaction.COLUMN_START_DATE)),
                            c.getLong(c.getColumnIndex(Transaction.COLUMN_DUE_DATE)),
                            c.getLong(c.getColumnIndex(Transaction.COLUMN_RETURN_DATE)),
                            c.getDouble(c.getColumnIndex(Transaction.COLUMN_RATE)),
                            c2.getDouble(c2.getColumnIndex(MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE)),
                            c2.getDouble(c2.getColumnIndex(MoneyTransaction.COLUMN_AMOUNT_DEFICIT)));
                }
                t.setId(c.getInt(c.getColumnIndex(User.COLUMN_ID)));
                transactions.add(t);
            }while(c.moveToNext());

        } else{
            transactions = null;
        }
        return transactions;
    }
    /*
    *QUERY ALL OBJECTS MODULE
    */


    /*
    *UPDATE OBJECT MODULE
    */
    public int updateUser(User updatedUser)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(User.COLUMN_NAME, updatedUser.getName());
        cv.put(User.COLUMN_CONTACT_INFO, updatedUser.getContactInfo());
        cv.put(User.COLUMN_TOTAL_RATE, updatedUser.getTotalRate());

        int id = db.update(updatedUser.TABLE_NAME, cv, " " + updatedUser.COLUMN_ID + "= ? ", new String[]{String.valueOf(updatedUser.getId())});

        return id;
    }

    public int updateTransaction(Transaction updatedTransaction)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Transaction.COLUMN_USER_ID, updatedTransaction.getUserID());
        cv.put(Transaction.COLUMN_TYPE, updatedTransaction.getType());
        cv.put(Transaction.COLUMN_STATUS, updatedTransaction.getStatus());
        cv.put(Transaction.COLUMN_START_DATE, updatedTransaction.getStartDate());
        cv.put(Transaction.COLUMN_DUE_DATE, updatedTransaction.getDueDate());
        cv.put(Transaction.COLUMN_RETURN_DATE, updatedTransaction.getReturnDate());
        cv.put(Transaction.COLUMN_RATE, updatedTransaction.getRate());
        Log.v("Update transID: ",""+updatedTransaction.getId());
        int id = db.update(updatedTransaction.TABLE_NAME, cv, " " + updatedTransaction.COLUMN_ID + "= ? ", new String[]{String.valueOf(updatedTransaction.getId())});
        Log.v("RETURNED BY DBUpdate:",""+id);
        cv = new ContentValues();
        if(updatedTransaction.getClassification().equalsIgnoreCase(Transaction.ITEM_TYPE)) {
            cv.put(ItemTransaction.COLUMN_NAME, ((ItemTransaction)updatedTransaction).getName());
            cv.put(ItemTransaction.COLUMN_DESCRIPTION, ((ItemTransaction)updatedTransaction).getDescription());
            cv.put(ItemTransaction.COLUMN_PHOTOPATH, ((ItemTransaction)updatedTransaction).getPhotoPath());
            db.update(ItemTransaction.TABLE_NAME, cv, " " + ItemTransaction.COLUMN_TRANSACTION_ID + "= ? ", new String[]{String.valueOf(updatedTransaction.getId())});
        }else{
            cv.put(MoneyTransaction.COLUMN_TOTAL_AMOUNT_DUE, ((MoneyTransaction)updatedTransaction).getTotalAmountDue());
            cv.put(MoneyTransaction.COLUMN_AMOUNT_DEFICIT, ((MoneyTransaction)updatedTransaction).getAmountDeficit());
            db.update(MoneyTransaction.TABLE_NAME, cv, " " + MoneyTransaction.COLUMN_TRANSACTION_ID + "= ? ", new String[]{String.valueOf(updatedTransaction.getId())});
        }
        return updatedTransaction.getId();
    }
    /*
    *UPDATE OBJECT MODULE
    */


    /*
    *DELETE OBJECT MODULE
    */
    public int deleteUser(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(User.TABLE_NAME, " " + User.COLUMN_ID + "= ? ", new String[]{String.valueOf(id)});
    }

    public int deleteTransaction(int id, String classification)
    {
        SQLiteDatabase db = getWritableDatabase();

        if(classification.equalsIgnoreCase(Transaction.ITEM_TYPE)) {
            db.delete(ItemTransaction.TABLE_NAME, " " + ItemTransaction.COLUMN_TRANSACTION_ID + "= ? ", new String[]{String.valueOf(id)});
        }
        else{
            db.delete(MoneyTransaction.TABLE_NAME, " " + MoneyTransaction.COLUMN_TRANSACTION_ID + "= ? ", new String[]{String.valueOf(id)});
        }
        return db.delete(Transaction.TABLE_NAME, " " + Transaction.COLUMN_ID + "= ? ", new String[]{String.valueOf(id)});
    }
    /*
    *DELETE OBJECT MODULE
    */

    public int checkUserIfExists(String name, String number){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c =   db.query(User.TABLE_NAME,
                null, // == *
                " " + User.COLUMN_NAME + "= ? AND "+User.COLUMN_CONTACT_INFO + "= ? ", //WHERE _ = ?
                new String[]{name, number},
                null,
                null,
                null);
        if(c.moveToFirst()){
            return c.getInt(c.getColumnIndex(User.COLUMN_ID));
        }else{
            return -1;
        }
    }

    public void updateUserRating(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c =   db.query(User.TABLE_NAME,
                null, // == *
                " " + User.COLUMN_ID + "= ? ", //WHERE _ = ?
                new String[]{String.valueOf(id)},
                null,
                null,
                null);

        double totalRate = 0;
        double totalCnt = 0;

        if(c.moveToFirst())
        {
            Cursor c2 =   db.query(Transaction.TABLE_NAME,
                    null, // == *
                    " " + Transaction.COLUMN_USER_ID + "= ? ", //WHERE _ = ?
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null);

            totalRate = 0;
            totalCnt = 0;

            while (c2.moveToNext())
            {
                totalRate += c2.getDouble(c2.getColumnIndex(Transaction.COLUMN_RATE));
                totalCnt++;
            }

            totalRate /= totalCnt;
            User newUser = new User(c.getString(c.getColumnIndex(User.COLUMN_NAME)), c.getString(c.getColumnIndex(User.COLUMN_CONTACT_INFO)), totalRate);
            newUser.setId(c.getInt(c.getColumnIndex(User.COLUMN_ID)));
            this.updateUser(newUser);
            //return totalRate;
        }else{
            totalRate = 0;
        }

    }
}

