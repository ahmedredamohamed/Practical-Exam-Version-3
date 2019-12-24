package com.example.practical3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static int BUS_SIZE = 27;

    private static String DATABASE_NAME = "practical3";
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table bus(id integer primary key,name text not null);");
        db.execSQL("create table seat(id integer primary key,available integer default (1),bus_id integer not null,foreign key (bus_id) references bus(id));");

        generateBusData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists bus");
        db.execSQL("drop table if exists seat");
        onCreate(db);
    }

    public Cursor fetchBuses(){
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from bus",null);
        cursor.moveToFirst();
        return  cursor;
    }

    public Cursor fetchBusData(int bus_id){
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from seat where bus_id = ?",new String[]{String.valueOf(bus_id)});
        cursor.moveToFirst();
        return  cursor;
    }

    public void Reserve(int seat_id,int bus_id){
        sqLiteDatabase = getWritableDatabase();
        ContentValues seat = new ContentValues();
        seat.put("available",0);
        sqLiteDatabase.update("seat",seat,"bus_id = ? and id = ?", new String[]{String.valueOf(bus_id),String.valueOf(seat_id)} );
    }

    private void generateBusData(SQLiteDatabase db){
        ContentValues bus = new ContentValues();
        ContentValues seat = new ContentValues();

        //first bus
        bus.put("name","Dahab");
        db.insert("bus",null,bus);
        for(int i = 0; i < BUS_SIZE; i++) {
            seat.put("bus_id", 1);
            db.insert("seat", null, seat);
        }

        //second bus
        bus.put("name","Hurgada");
        db.insert("bus",null,bus);
        for(int i = 0; i < BUS_SIZE; i++) {
            seat.put("bus_id", 2);
            db.insert("seat", null, seat);
        }


        //third bus
        bus.put("name","Sharm El-sheikh");
        db.insert("bus",null,bus);
        for(int i = 0; i < BUS_SIZE; i++) {
            seat.put("bus_id", 3);
            db.insert("seat", null, seat);
        }
    }

}
