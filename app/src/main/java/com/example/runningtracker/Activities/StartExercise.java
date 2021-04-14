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

public class StartExercise extends AppCompatActivity implements OnMapReadyCallback {


    GoogleMap googleMap;

    List<LatLng> list=new ArrayList<>();


    float DistanceTraveled=0.0f; //Total Distance Traveled

    //Variable to Store User Previous Location Before updating to new one
    // so that we can calculate distance traveled
    Location userPrevLocation=null;
    Button btnStartPause; //Button to Start Or Stop Exercise
    boolean isRunning=false; //Variable to track if User is Running Or Not
    TextView txtTime; //TextView to show time
    TextView txtDistance; //TextView to Show Distance Traveled

    // Icon Which is on Map we use this object to update icon location to our current location
    MarkerOptions mIcon;
    Marker marker;

    long time=0; //varible to store time elapsed

    Thread timerThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_exercise);
        // Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //Sync Map Calls back when map is ready to use onMapReady fun got called



        // Initialzing Views
        btnStartPause=findViewById(R.id.btnStartPause);
        txtTime=findViewById(R.id.txtTime);
        txtDistance=findViewById(R.id.txtDistanceTraveled);


        // OnClick Listener For Button Start/Stop
        btnStartPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(googleMap!=null) //check if map is ready or not
                {

                    if(!isRunning) //if user not running it means user started running now
                    {
                        //googleMap.clear();
                        /*

                        reset all values to default if they are not

                         */
                        DistanceTraveled=0.0f;
                        userPrevLocation=null;
                        txtDistance.setText("0 km");
                        txtTime.setText("00:00:00");
                        btnStartPause.setText("Stop");
                        list.clear();

                        //Calling fun startTimer to start a thread which updates time on textView
                        startTimer();
                    }
                    else
                    {
                        btnStartPause.setText("Start");
                        //release timer
                        stopTimer();
                        //Call ExerciseDetail Activity
                        goToDetailActivity();
                    }

                    // Update isRunning Value if it was true before make it false if false then make it true..
                    isRunning=!isRunning;
                }

            }
        });
    }

    private void goToDetailActivity()
    {

        //Call ExerciseDetail Activity
        // and pass time and distance traveled to that activity for further use
        Intent intent=new Intent(this,ExerciseDetail.class);
        intent.putExtra("TIME",time);
        intent.putExtra("Distance",DistanceTraveled);
        startActivity(intent);

    }



    // fun startTimer to start a thread which updates time on textView
    public  void startTimer()
    {


        time=0; //initially time is 0

        timerThread = new Thread()
        {
            @Override
            public void run() {
                try {
                    while(isRunning) //while user is running update time
                    {
                        runOnUiThread(new Runnable() //runOnUiThread this method is used to set value on txtTime

                        {
                            @Override
                            public void run()
                            {
                                time=time+1000; //add one second to time
                                String formate = TimeFormater.formate(time); //convert long time to hh:mm:ss
                                txtTime.setText(formate);
                            }
                        });
                        Thread.sleep(1000); //Stop Thread for 1 second (clocks tick after every one second)
                    }
                } catch (InterruptedException e)  //if any exception occur during thread sleep
                {
                    e.printStackTrace();
                }
            }
        };

        timerThread.start(); //start thread

    }
    public void stopTimer() //stop thred timer
    {
        timerThread=null;
    }



    private void getLocationPermission() //fun to as user give us permission to access his location
    {
        //check if permission is provided or not to get user location
        // if permission is provided then get user location
        // other wise ask for permission.
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getlocation(); //fun to get user location
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    11);
        }
    }

    private void getlocation()
    {

        //fun to get user location
        LocationManager locationManagerlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
         MyLocationListener locationListener = new MyLocationListener();
        try
        {

            locationManagerlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, // minimum time interval betweenupdates
                    0,locationListener);
        }
        catch (SecurityException e)
        {
            Toast.makeText(StartExercise.this,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();

        }


    }


    // Location Changed Listenr
    public class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location) //this method runs when user location gets changed
        {


            if(mIcon==null) //check if there is icon on map or not
            {
                // if icon is not set on map
                //then create a icon and set on map
                // also move map to user lcoation.
                mIcon = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location));
                googleMap.clear();
                marker=googleMap.addMarker(mIcon);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));

            }
            else
            {
                // if icon is already on map
                // then just update icon position to new location.
                marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            }

            if(isRunning) //if user is running
            {
                // if user is running then add new location to list
                // then call fun drawline() which draws a polygon of route on map
                // also call calculatedistance fun to caluclate distance.

                list.add(new LatLng(location.getLatitude(),location.getLongitude()));
                drawLine();
                calculateDistance(location);
            }
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.d("g53mdp", "onStatusChanged: " + provider + " " +status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("g53mdp", "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            /// if user disable gps then ask user to enable gps.
            Toast.makeText(StartExercise.this,"Please Enable GPS",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }


    }

    private void calculateDistance(Location newUserlocation)
    {

        // this method is used to calcualte distance traveled


        if (userPrevLocation != null)  //if we   have previous user stored location
        {


            //call  distanceTo mathod to get disatnce between two location objects
            float newDistanceTraveled = userPrevLocation.distanceTo(newUserlocation);

            //add new distance traveled to previous distance
            DistanceTraveled += newDistanceTraveled;

            //convert distance from Meters to KM  i.e KM=Meter/1000

            float distanceinKM=DistanceTraveled/1000;
            //update distance on txtview
            txtDistance.setText(distanceinKM+" KM");

        }

        //store user new location to previouslocation object
        userPrevLocation=newUserlocation;


    }

    private void drawLine()
    {

        //this method draws a polygon on map
        // black line which we see
        PolylineOptions polyopt = new PolylineOptions().width(14).color(Color.BLACK).geodesic(true);

        for (int i = 0; i < list.size(); i++)
        {
            LatLng point = list.get(i);
            polyopt.add(point);
        }
        googleMap.addPolyline(polyopt);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) //this method calls when map is ready to use
    {
        this.googleMap=googleMap;
        getLocationPermission(); //get user location and move map to that location
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    //this method calls when user gives permisson or deny
    {
        switch (requestCode)
        {
            case 11: //case 11 is our location request code
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //if user provided then get his location
                    getlocation();
                }
                else
                {
                    // if user didnt provided permission then again ask for permission
                    getLocationPermission();
                }

            }
        }
    }

    @Override
    public void onBackPressed()  //this method runs when user click on back button
    {
        super.onBackPressed();
    }
}