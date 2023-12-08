package com.example.myapplication.DatabaseHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.Model.Hike;
import com.example.myapplication.Model.Observation;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hike_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_HIKES = "hikes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HIKE_NAME = "hike_name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LOCATION_FROM = "location_from";
    private static final String COLUMN_LOCATION_TO = "location_to";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LENGTH = "length";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_IS_PARKING = "is_parking";

    private static final String TABLE_OBSERVATION = "observations";
    private static final String COLUMN_OB_ID = "ob_id";
    private static final String COLUMN_OB_HIKE_ID= "ob_hike_id";
    private static final String COLUMN_OB_NAME = "ob_name";
    private static final String COLUMN_OB_TIME = "ob_time";
    private static final String COLUMN_OB_IMAGE = "ob_image";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_HIKES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HIKE_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_LOCATION_FROM + " TEXT, " +
                COLUMN_LOCATION_TO + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_LENGTH + " TEXT, " +
                COLUMN_LEVEL + " TEXT, " +
                COLUMN_IS_PARKING + " TEXT, " +
                COLUMN_DURATION + " TEXT" + ");";

        db.execSQL(createTableQuery);

        String createTableObQuery = "CREATE TABLE " + TABLE_OBSERVATION + " (" +
                COLUMN_OB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_OB_HIKE_ID + " INTEGER, " +
                COLUMN_OB_NAME + " TEXT, " +
                COLUMN_OB_TIME + " TEXT, " +
                COLUMN_OB_IMAGE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_OB_HIKE_ID + ") REFERENCES " + TABLE_HIKES + "(" + COLUMN_ID + ") ON DELETE CASCADE" +
                ");";

        db.execSQL(createTableObQuery);
    }

    public int plan_hike(String hikeName, String description, 
    String locationFrom, String locationTo, String date, 
    String length, String duration,String level,
    String is_parking) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HIKE_NAME, hikeName);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_LOCATION_FROM, locationFrom);
        values.put(COLUMN_LOCATION_TO, locationTo);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LENGTH, length);
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_LEVEL, level);
        values.put(COLUMN_IS_PARKING, is_parking);

        int insertedId=(int) db.insert(TABLE_HIKES, null, values);
        db.close();
        return  insertedId;
    }

    public void add_observation(String observationName,int hikeId,String time, String image) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues ob_values = new ContentValues();
        ob_values.put(COLUMN_OB_NAME, observationName);
        ob_values.put(COLUMN_OB_HIKE_ID, hikeId);
        ob_values.put(COLUMN_OB_TIME, time);
        ob_values.put(COLUMN_OB_IMAGE, image);

        db.insert(TABLE_OBSERVATION, null, ob_values);
        db.close();

    }

    public void delete_observation(int obId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_OBSERVATION, COLUMN_OB_ID + "=?", new String[]{String.valueOf(obId)});
        db.close();
    }

    @SuppressLint("Range")
    public List<Observation> getAllObservationsByHikeId(int hikeId) {
        List<Observation> observationList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_OBSERVATION + " WHERE " + COLUMN_OB_HIKE_ID + " = " + hikeId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Observation observation = new Observation();
                observation.setOb_id(cursor.getInt(cursor.getColumnIndex(COLUMN_OB_ID)));
                observation.setOb_hike_id(cursor.getInt(cursor.getColumnIndex(COLUMN_OB_HIKE_ID)));
                observation.setOb_name(cursor.getString(cursor.getColumnIndex(COLUMN_OB_NAME)));
                observation.setOb_time(cursor.getString(cursor.getColumnIndex(COLUMN_OB_TIME)));
                observation.setOb_image(cursor.getString(cursor.getColumnIndex(COLUMN_OB_IMAGE)));

                observationList.add(observation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return observationList;
    }

    public void editObservationById(int obId, int obHikeId, String obName, String obTime, String obImage) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_OB_ID, obId);
        values.put(COLUMN_OB_HIKE_ID, obHikeId);
        values.put(COLUMN_OB_NAME, obName);
        values.put(COLUMN_OB_TIME, obTime);
        values.put(COLUMN_OB_IMAGE, obImage);

        String whereClause = COLUMN_OB_ID + " = ?";
        String[] whereArgs = {String.valueOf(obId)};

        db.update(TABLE_OBSERVATION, values, whereClause, whereArgs);
        db.close();
    }

    @SuppressLint("Range")
    public Observation getObservationById(int obId) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = COLUMN_OB_ID + " = ?";
        String[] selectionArgs = { String.valueOf(obId) };

        Cursor cursor = db.query(TABLE_OBSERVATION, null, selection, selectionArgs, null, null, null);

        Observation ob = null;

        if (cursor.moveToFirst()) {
            ob = new Observation();
            ob.setOb_id(cursor.getInt(cursor.getColumnIndex(COLUMN_OB_ID)));
            ob.setOb_hike_id(cursor.getInt(cursor.getColumnIndex(COLUMN_OB_HIKE_ID)));
            ob.setOb_name(cursor.getString(cursor.getColumnIndex(COLUMN_OB_NAME)));
            ob.setOb_time(cursor.getString(cursor.getColumnIndex(COLUMN_OB_TIME)));
            ob.setOb_image(cursor.getString(cursor.getColumnIndex(COLUMN_OB_IMAGE)));
        }
        cursor.close();
        db.close();
        return ob;
    }

    @SuppressLint("Range")
    public List<Hike> search_hike(String name, String date) {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_HIKES + " WHERE 1";

        if (!name.isEmpty()) {
            query += " AND " + COLUMN_HIKE_NAME + " LIKE '%" + name + "%'";
        }
        if (!date.isEmpty()) {
            query += " AND " + COLUMN_DATE + " = '" + date + "'";
        }

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                hike.setHikeName(cursor.getString(cursor.getColumnIndex(COLUMN_HIKE_NAME)));
                hike.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                hike.setLocationFrom(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_FROM)));
                hike.setLocationTo(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_TO)));
                hike.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                hike.setLength(cursor.getString(cursor.getColumnIndex(COLUMN_LENGTH)));
                hike.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)));
                hike.setLevel(cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)));
                hike.setParking(cursor.getString(cursor.getColumnIndex(COLUMN_IS_PARKING)));

                hikeList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return hikeList;
    }


    public void deleteHike(int hikeId) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_HIKES, COLUMN_ID + "=?", new String[]{String.valueOf(hikeId)});
            db.delete(TABLE_OBSERVATION, COLUMN_OB_HIKE_ID + "=?", new String[]{String.valueOf(hikeId)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void deleteAllHike (){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_OBSERVATION,null,null);
        db.delete(TABLE_HIKES,null,null);
        db.close();
    }

    public void editHikeById(int hikeId, String hikeName, 
    String description, String locationFrom, 
    String locationTo, String date, 
    String length, String duration, 
    String level, String is_parking) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_HIKE_NAME, hikeName);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_LOCATION_FROM, locationFrom);
        values.put(COLUMN_LOCATION_TO, locationTo);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LENGTH, length);
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_LEVEL, level);
        values.put(COLUMN_IS_PARKING, is_parking);

        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(hikeId)};

        db.update(TABLE_HIKES, values, whereClause, whereArgs);
        db.close();
    }


    @SuppressLint("Range")
    public Hike getHikeById(int hikeId) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(hikeId) };

        Cursor cursor = db.query(TABLE_HIKES, null, selection, selectionArgs, null, null, null);

        Hike hike = null;

        if (cursor.moveToFirst()) {
            hike = new Hike();
            hike.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            hike.setHikeName(cursor.getString(cursor.getColumnIndex(COLUMN_HIKE_NAME)));
            hike.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            hike.setLocationFrom(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_FROM)));
            hike.setLocationTo(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_TO)));
            hike.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            hike.setLength(cursor.getString(cursor.getColumnIndex(COLUMN_LENGTH)));
            hike.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)));
            hike.setLevel(cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)));
            hike.setParking(cursor.getString(cursor.getColumnIndex(COLUMN_IS_PARKING)));
        }

        cursor.close();
        db.close();
        return hike;
    }


    @SuppressLint("Range")
    public List<Hike> getAllHikes() {
        List<Hike> hikeList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_HIKES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Hike hike = new Hike();
                hike.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                hike.setHikeName(cursor.getString(cursor.getColumnIndex(COLUMN_HIKE_NAME)));
                hike.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                hike.setLocationFrom(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_FROM)));
                hike.setLocationTo(cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION_TO)));
                hike.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                hike.setLength(cursor.getString(cursor.getColumnIndex(COLUMN_LENGTH)));
                hike.setDuration(cursor.getString(cursor.getColumnIndex(COLUMN_DURATION)));
                hike.setLevel(cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)));
                hike.setParking(cursor.getString(cursor.getColumnIndex(COLUMN_IS_PARKING)));

                hikeList.add(hike);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return hikeList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
