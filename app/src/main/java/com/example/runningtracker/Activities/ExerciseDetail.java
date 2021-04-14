package com.example.runningtracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.runningtracker.DBHelper;
import com.example.runningtracker.Model.Exercise;
import com.example.runningtracker.Model.Other;
import com.example.runningtracker.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class ExerciseDetail extends AppCompatActivity
{



    // this activity is used to show exercise details


    // Views Used
    TextView txtTime;
    TextView txtDistance;
    TextView txtBestTime;
    TextView txtDistanceToday;
    TextView txtDistanceMonth;
    RadioGroup radioGroup;
    EditText editTextUserNote;
    DBHelper objDB;
    Other other;

    float DistanceMonth; // variable to contain distance
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);



        // initializing views
        txtTime=findViewById(R.id.txtTime);
        txtDistance=findViewById(R.id.txtDistanceTraveled);
        txtBestTime=findViewById(R.id.txtbestTime);
        txtDistanceToday=findViewById(R.id.txtHFIRT);
        txtDistanceMonth=findViewById(R.id.txtHFIRTM);
        radioGroup=findViewById(R.id.radioGrop);
        editTextUserNote=findViewById(R.id.editTextUserNote);


        // creating database object
        objDB=new DBHelper(this);



        setVals(); //calling method to show values on textviews

    }

    private void setVals()
    {


        // Getting data passed by previous activity
        Long TodayTime=getIntent().getExtras().getLong("TIME",0);
        float TodayDistance=getIntent().getExtras().getFloat("Distance",0f);



        // formating and showing values on textviews
        txtTime.setText(TimeFormater.formate(TodayTime));
        txtDistance.setText(String.format("%s KM", TodayDistance / 1000));
        txtDistanceToday.setText(String.format("%s KM", TodayDistance / 1000));


        // get dat from database in Other Object
        other=objDB.getOther();

         DistanceMonth=other.getDistanceMonth()+TodayDistance; //Total Distance = Previous Distance And Distance Today Traveld



        // Logic to Check best time
        if(other.getMyBestTime()>0 &&  other.getMyBestTime()<TodayTime)
        {
            //TimeFormater class is used to format time convert long -> to meaning full time hh:mm:ss
            txtBestTime.setText("No , your best time is "+ TimeFormater.formate(other.getMyBestTime()));
        }
        else
        {

            txtBestTime.setText("Yes , your best time is "+ TimeFormater.formate(TodayTime));
            other.setMyBestTime(TodayTime);
        }

        other.setDistanceMonth(DistanceMonth);
        txtDistanceMonth.setText(String.format("%s KM", DistanceMonth / 1000));


    }


    // this method runs when user click on button save
    public void onClickSave(View view)
    {

        // creating exercise object
        Exercise exercise=new Exercise();
        exercise.setDistance(txtDistance.getText().toString());
        exercise.setTime(txtTime.getText().toString());

        // checking if usernote is empty or note
        if(TextUtils.isEmpty(editTextUserNote.getText()))
        {
            exercise.setNote("No Note");
        }
        else
            exercise.setNote(editTextUserNote.getText().toString());

        // getting value of exeprience from radio button and saving in exercise object
        exercise.setExperience(((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString());


        // insert exercise and other data to database and check if its success or not
        if(objDB.insertOther(other) && objDB.insertExercise(exercise))
        {
            // if success show success snackbar

            Snackbar.make(findViewById(android.R.id.content),"Successfully Saved", BaseTransientBottomBar.LENGTH_LONG).show();

        }
        else
        {
            // if not success  show Failed snackbar
            Snackbar.make(findViewById(android.R.id.content),"Failed To Save", BaseTransientBottomBar.LENGTH_LONG).show();
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}