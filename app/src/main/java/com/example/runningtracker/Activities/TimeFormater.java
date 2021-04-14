package com.example.runningtracker.Activities;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

public class TimeFormater
{


    //TimeFormater class is used to format time convert long -> to meaning full time hh:mm:ss
    public static  String formate(long time)
    {
        @SuppressLint("DefaultLocale")
        String format = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));

        return  format;
    }

}
