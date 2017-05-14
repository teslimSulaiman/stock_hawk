package com.udacity.stockhawk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udacity.stockhawk.data.Contract.Quote;
import com.udacity.stockhawk.data.Contract.QuoteHistory;


class DbHelper extends SQLiteOpenHelper {


    private static final String NAME = "StockHawk.db";
    private static final int VERSION = 54;


    DbHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String builder = "CREATE TABLE " + Quote.TABLE_NAME + " ("
                + Quote._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Quote.COLUMN_SYMBOL + " TEXT NOT NULL, "
                + Quote.COLUMN_PRICE + " REAL NOT NULL, "
                + Quote.COLUMN_ABSOLUTE_CHANGE + " REAL NOT NULL, "
                + Quote.COLUMN_PERCENTAGE_CHANGE + " REAL NOT NULL, "
                + Quote.COLUMN_HISTORY + " TEXT NOT NULL, "
                + "UNIQUE (" + Quote.COLUMN_SYMBOL + ") ON CONFLICT REPLACE);";

        String historyBuilder = "CREATE TABLE " + QuoteHistory.TABLE_NAME + " ("
                + QuoteHistory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QuoteHistory.COLUMN_SYMBOL + " TEXT NOT NULL, "
                + QuoteHistory.COLUMN_DATE + " INTEGER NOT NULL, "
                + QuoteHistory.COLUMN_COUNTER + " INTEGER NOT NULL, "
                + QuoteHistory.COLUMN_CLOSE + " REAL NOT NULL );";
              //  + "UNIQUE (" + QuoteHistory.COLUMN_COUNTER + ") ON CONFLICT REPLACE);";


        db.execSQL(builder);
        db.execSQL(historyBuilder);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + Quote.TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + QuoteHistory.TABLE_NAME);

        onCreate(db);
    }
}
