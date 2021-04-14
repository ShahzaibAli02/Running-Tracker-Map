package com.example.runningtracker.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runningtracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    // this function runs when user click on StartExercise button
    public void OnClickStartExercise(View view)
    {

        // get Location Manager Object
        final LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );


        // checking if user gps is On Or Not
        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
        {

            // Ask User To Open GPS
            Toast.makeText(MainActivity.this,"Please Enable GPS",Toast.LENGTH_LONG).show();


            //This intent will take user to Enable Gps Screen
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return;
        }

        // If Gps is enabled go to StartExercise Activity
        startActivity(new Intent(MainActivity.this,StartExercise.class));


    }


    //This function runs when user click on Saved EXercise button
    public void OnClickSavedExercise(View view)
    {
        // go to SavedExercises Activity
        startActivity(new Intent(MainActivity.this,SavedExercises.class));

    }
}