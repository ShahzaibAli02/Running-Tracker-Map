package com.example.runningtracker.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.runningtracker.Adapter.ExerciseAdapter;
import com.example.runningtracker.DBHelper;
import com.example.runningtracker.Model.Exercise;
import com.example.runningtracker.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SavedExercises extends AppCompatActivity
{

    /*

    This activity show All Previous Saved Exercises in RecyclerView

     */

    RecyclerView recycleView; //our recyclerview to show a list of exercises
    DBHelper dbHelper; //database object
    List<Exercise > list; //list containg data about exercises
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_exercises);


        dbHelper=new DBHelper(this);  //initializign db object
        // initialing views
        recycleView=findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(this));


        // getting list of exercise from database and storing in our Arraylist
        list=dbHelper.getlistExercises();


        if(list.isEmpty()) // if list is empty
        {

            // show not found message to user
            Snackbar.make(findViewById(android.R.id.content),"No Saved Exercise Found", BaseTransientBottomBar.LENGTH_LONG).show();
        }
        else
        {
            // if list has data then pass this data to adapter and set that adapter to our recyclerview
            recycleView.setAdapter(new ExerciseAdapter(list,this));
        }


    }

    @Override
    public void onBackPressed()  //this method runs when user click on back button
    {
        super.onBackPressed();
        finish();
    }
}