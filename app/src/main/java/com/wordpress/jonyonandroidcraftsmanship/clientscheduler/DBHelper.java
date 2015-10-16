package com.wordpress.jonyonandroidcraftsmanship.clientscheduler;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "dbClientSchedule";
    public static final int VERSION = 1;

    public static final String TABLE_NAME = "tblSchedule";
    public static final String ID_FIELD = "_id";
    public static final String CLIENT_NAME = "clientName";
    public static final String SCHEDULE_DATE = "schedule";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String LOCATION_ADDRESS = "locationAddress";

    public static final String TABLE_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + ID_FIELD + " INTEGER PRIMARY KEY, " + CLIENT_NAME
            + " TEXT, " + SCHEDULE_DATE + " TEXT, " + START_TIME + " TEXT, " + END_TIME
            + " TEXT, " + LOCATION_ADDRESS + " TEXT)";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TABLE SQL", TABLE_SQL);
        db.execSQL(TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
