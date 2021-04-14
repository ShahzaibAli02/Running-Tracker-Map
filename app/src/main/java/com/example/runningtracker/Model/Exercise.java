package com.example.runningtracker.Model;

public class Exercise
{

    /*

    this is exercise model class


     */

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    private  String Time;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    private  String Date;
    private  String Distance;
    private  String Experience;
    private  String Note;

}
