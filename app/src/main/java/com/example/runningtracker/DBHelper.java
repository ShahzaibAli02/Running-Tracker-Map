package com.example.runningtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.runningtracker.Model.Exercise;
import com.example.runningtracker.Model.Other;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "RunningTracker.db";
    private static final int DATABASE_VERSION = 3;


    //query to Create Table exercise
    private static final String CREATE_TABLE_EXERCISES = "CREATE TABLE exercise (_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,Time VARCHAR(128) NOT NULL,Distance VARCHAR(128) NOT NULL,Experience VARCHAR(128),Note VARCHAR(128),Date VARCHAR(128));";

    //query to Create Table others
    private static final String CREATE_TABLE_OTHER ="CREATE TABLE others (_id INTEGER,BestTime VARCHAR(128) NOT NULL,DistanceMonth VARCHAR(128) NOT NULL);";



    //Constructor
    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_TABLE_EXERCISES);
        sqLiteDatabase.execSQL(CREATE_TABLE_OTHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS exercise" );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS others" );
        onCreate(sqLiteDatabase);
    }



    public  boolean insertExercise(Exercise exercise)
    {
        //this method is used to insert exercise data in database
        //exercise object contains data abotu the exercise
        //like Time,Distance etc


        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Time",exercise.getTime());
        contentValues.put("Distance",exercise.getDistance());
        contentValues.put("Experience",exercise.getExperience());
        contentValues.put("Note",exercise.getNote());
        contentValues.put("Date",getDate());
        return  writableDatabase.insert("exercise",null,contentValues)>0;

    }

    private String getDate()
    {
        // this method is used to get current date
        return new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public  boolean insertOther(Other other)
    {


        //this method is used to insert Other data in database
        //exercise object contains data about the exercise
        //like BestTime,DistanceMonth etc
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("_id","1");
        contentValues.put("BestTime",String.valueOf(other.getMyBestTime()));
        contentValues.put("DistanceMonth",String.valueOf(other.getDistanceMonth()));

        // first try to update value in othertable
        int updateVal = writableDatabase.update("others", contentValues, "_id=1", null);

        // if its get updated then it means there was previous data in table
        // which gets updated
        if(updateVal>0)
        {
            return true;
        }
        else
        {

            //if the data didnt gets update
            // it means table was empty
            // then we need to use insert query to insert data
            return  writableDatabase.insert("others",null,contentValues)>0;
        }

    }


    public  Other getOther()
    {

        //this method is used to get Other data
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        Other other=new Other();
        Cursor cursor = readableDatabase.rawQuery("select * from others where _id=1;",null);
        //loop through all rows to get data
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {

            other.setMyBestTime(Long.parseLong(cursor.getString(1)));
            other.setDistanceMonth(Float.parseFloat(cursor.getString(2)));

        }
        cursor.close();
        return other;
    }

    public   List<Exercise> getlistExercises()
    {

        //this method is used to get exercise list  data
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from exercise;",null);
        List<Exercise> list=new ArrayList<>();
        //loop through all rows to get data
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            Exercise exercise=new Exercise();
            exercise.setTime(cursor.getString(cursor.getColumnIndex("Time")));
            exercise.setDistance(cursor.getString(cursor.getColumnIndex("Distance")));
            exercise.setExperience(cursor.getString(cursor.getColumnIndex("Experience")));
            exercise.setNote(cursor.getString(cursor.getColumnIndex("Note")));
            exercise.setDate(cursor.getString(cursor.getColumnIndex("Date")));
            list.add(exercise);

        }
        cursor.close();
        return list;
    }




}