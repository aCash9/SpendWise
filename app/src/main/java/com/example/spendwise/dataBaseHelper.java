package com.example.spendwise;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;


public class dataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "HistoryDatabase1.2";
    public static final String TRANSACTION_LIST = "TransactionList";
    public static final String KEY_AMOUNT = "Amount";
    public static final String KEY_NOTE = "Note";
    public static final String KEY_DATE = "Date";
    public static final String KEY_TYPE = "Type";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_CATEGORY = "keyCategory";


    public dataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTransactionList = "CREATE TABLE " + TRANSACTION_LIST + " ( " + KEY_AMOUNT + " INTEGER, " + KEY_NOTE + " TEXT, " + KEY_DATE + " TEXT, " + KEY_TYPE + " TEXT, " + KEY_CATEGORY + " TEXT, " + KEY_BALANCE + " INTEGER )";
        db.execSQL(createTransactionList);

        Log.d("mytag", "onCreate: database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean addItem(TransactionDetails item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_AMOUNT, item.getAmount());
        cv.put(KEY_NOTE, item.getNote());
        cv.put(KEY_DATE, item.getDate());
        cv.put(KEY_TYPE, item.getType());
        cv.put(KEY_CATEGORY, item.getCategory());
        String tempBalance = getLastRowData(KEY_BALANCE);
        Log.d("mytag", "addTransaction: got the lastrowData ");

        //updating the balance after every transaction
        int newBalance;
        if (tempBalance == null) {
            if (item.getType().equals("add"))
                newBalance = item.getAmount();
            else {
                newBalance = -item.getAmount();
            }
        } else {
            if (item.getType().equals("add")) {
                newBalance = Integer.parseInt(tempBalance) + item.getAmount();
            } else
                newBalance = Integer.parseInt(tempBalance) - item.getAmount();
        }

        cv.put(KEY_BALANCE, newBalance);
        long insert = db.insert(TRANSACTION_LIST, null, cv);

        return insert != -1;
    }

    public List<TransactionDetails> getData() {
        Log.d("mytag", "getData: IN");
        List<TransactionDetails> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM " + TRANSACTION_LIST;
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.moveToFirst()) {
            do {
                TransactionDetails item = new TransactionDetails();
                item.setAmount(cursor.getInt(0));
                item.setNote(cursor.getString(1));
                item.setDate(cursor.getString(2));
                item.setType(cursor.getString(3));
                item.setCategory(cursor.getString(4));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("mytag", "getData: list generated");
        return list;
    }

    public PriorityQueue<categoryDetails> getCategoryData() {
        List<TransactionDetails> list = getData();
        int fAv = 0, shopping = 0, bills = 0, health = 0, entertainment = 0, transport = 0, others = 0, food = 0;
        for (TransactionDetails item : list) {
            int amt = item.getAmount();
            String cat = item.getCategory();
            if (cat.equals("Fruits and Vegetables")) {
                fAv += item.getAmount();
            } else if (cat.equals("Shopping")) {
                shopping += item.getAmount();
            } else if (cat.equals("Bills")) {
                bills += item.getAmount();
            } else if (cat.equals("Health")) {
                health += item.getAmount();
            } else if (cat.equals("Entertainment")) {
                entertainment += item.getAmount();
            } else if (cat.equals("Transport")) {
                transport += item.getAmount();
            } else if (cat.equals("Others")) {
                others += item.getAmount();
            } else if (cat.equals("Food")) {
                food += item.getAmount();
            }

        }
        PriorityQueue<categoryDetails> pq = new PriorityQueue<>(new categoryComparator());
        categoryDetails FAV = new categoryDetails("Fruits and Vegetables", fAv);
        categoryDetails SHOPPPING = new categoryDetails("Shopping", shopping);
        categoryDetails BILLS = new categoryDetails("Bills", bills);
        categoryDetails HEALTH = new categoryDetails("Health", health);
        categoryDetails ENTERTAINMENT = new categoryDetails("Entertainment", entertainment);
        categoryDetails TRANSPORT = new categoryDetails("Transport", transport);
        categoryDetails FOOD = new categoryDetails("Food", food);
        categoryDetails OTHERS = new categoryDetails("Others", others);
        pq.add(FAV);
        pq.add(SHOPPPING);
        pq.add(BILLS);
        pq.add(HEALTH);
        pq.add(ENTERTAINMENT);
        pq.add(TRANSPORT);
        pq.add(FOOD);
        pq.add(OTHERS);

        return pq;
    }

    static class categoryComparator implements Comparator<categoryDetails> {

        @Override
        public int compare(categoryDetails o1, categoryDetails o2) {
            if (o1.getCategoryAmount() < o2.getCategoryAmount())
                return 1;
            else if (o1.getCategoryAmount() > o2.getCategoryAmount())
                return -1;
            return 0;
        }
    }

    public String getLastRowData(String columnName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT " + columnName + " FROM " + TRANSACTION_LIST + " WHERE ROWID = (SELECT MAX(ROWID) FROM " + TRANSACTION_LIST + ")";
        Cursor cursor = db.rawQuery(queryString, null);

        String data = null;
        if (cursor.moveToFirst()) {
            data = cursor.getString(0);
        }
        cursor.close();
        return data;
    }

    public boolean deleteEntry(TransactionDetails item) {
        SQLiteDatabase db = this.getWritableDatabase();

        int updatedValue = Integer.parseInt(getLastRowData(KEY_BALANCE));
        // Delete the item
        String whereClause = KEY_AMOUNT + " = ? AND " + KEY_DATE + " = ?";
        String[] whereArgs = {String.valueOf(item.getAmount()), item.getDate()};
        int rowsDeleted = db.delete(TRANSACTION_LIST, whereClause, whereArgs);
        Log.d("mytag", "deleteEntry: deleted " + rowsDeleted + " rows");


        if (item.getType().equals("add")) {
            updatedValue -= item.getAmount();
        } else {
            updatedValue += item.getAmount();
        }
        updateLastEntryOfColumn(KEY_BALANCE, String.valueOf(updatedValue));
        return rowsDeleted > 0;
    }

    public boolean updateLastEntryOfColumn(String columnName, String newValue) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Step 1: Identify the last row
        String queryLastRow = "SELECT * FROM " + TRANSACTION_LIST + " ORDER BY " + KEY_BALANCE + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(queryLastRow, null);

        if (cursor.moveToFirst()) {
            // Step 2: Update the desired column in the last row
            @SuppressLint("Range") int lastRowId = cursor.getInt(cursor.getColumnIndex(KEY_BALANCE));

            ContentValues values = new ContentValues();
            values.put(columnName, newValue);

            String whereClause = KEY_BALANCE + " = ?";
            String[] whereArgs = {String.valueOf(lastRowId)};

            int rowsAffected = db.update(TRANSACTION_LIST, values, whereClause, whereArgs);
            Log.d("mytag", "updateLastEntryOfColumn: updated " + rowsAffected + " rows");

            return rowsAffected > 0;
        }

        cursor.close();
        db.close();
        return false;
    }
}
