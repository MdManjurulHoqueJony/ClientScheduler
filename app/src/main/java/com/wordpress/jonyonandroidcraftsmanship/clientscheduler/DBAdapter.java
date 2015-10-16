package com.wordpress.jonyonandroidcraftsmanship.clientscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBAdapter {
    private DBHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBAdapter(Context context) {
        this.context = context;
        dbHelper=new DBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public long insertSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CLIENT_NAME, schedule.getClientName());
        values.put(DBHelper.SCHEDULE_DATE, schedule.getScheduleDate());
        values.put(DBHelper.START_TIME, schedule.getStartTime());
        values.put(DBHelper.END_TIME, schedule.getEndTime());
        values.put(DBHelper.LOCATION_ADDRESS, schedule.getLocationAddress());

        long inserted = database.insert(DBHelper.TABLE_NAME, null, values);

        return inserted;
    }

    public ArrayList<Schedule> getAllSchedules() {
        ArrayList<Schedule> allSchedules = null;

        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null,
                null, null);

        if (cursor != null && cursor.getCount() > 0) {
            int size = cursor.getCount();
            cursor.moveToFirst();
            allSchedules = new ArrayList<Schedule>();

            for (int i = 0; i < size; i++) {
                String clientName = cursor.getString(cursor
                        .getColumnIndex(DBHelper.CLIENT_NAME));
                String scheduleDate = cursor.getString(cursor
                        .getColumnIndex(DBHelper.SCHEDULE_DATE));
                String startTime = cursor.getString(cursor
                        .getColumnIndex(DBHelper.START_TIME));
                String endTime = cursor.getString(cursor
                        .getColumnIndex(DBHelper.END_TIME));
                String locationAddress = cursor.getString(cursor
                        .getColumnIndex(DBHelper.LOCATION_ADDRESS));
                allSchedules.add(new Schedule(clientName,scheduleDate,startTime,endTime,locationAddress));
                cursor.moveToNext();
            }
        }

        cursor.close();
        return allSchedules;
    }

    public long updateSchedule(int position,Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.ID_FIELD, position);
        values.put(DBHelper.CLIENT_NAME, schedule.getClientName());
        values.put(DBHelper.SCHEDULE_DATE, schedule.getScheduleDate());
        values.put(DBHelper.START_TIME, schedule.getStartTime());
        values.put(DBHelper.END_TIME, schedule.getEndTime());
        values.put(DBHelper.LOCATION_ADDRESS, schedule.getLocationAddress());

        long updated = database.update(DBHelper.TABLE_NAME,values,DBHelper.ID_FIELD + "=" + position, null);
        return updated;
    }

    public boolean isDatabaseEmpty(){
        boolean isEmpty=true;
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null,
                null, null);
        if (cursor != null && cursor.getCount() > 0) {
            isEmpty=false;
        }
        return isEmpty;
    }

    public int clear() {
        return database.delete(DBHelper.TABLE_NAME, null, null);
    }
}
