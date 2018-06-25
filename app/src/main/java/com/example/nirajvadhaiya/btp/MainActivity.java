package com.example.nirajvadhaiya.btp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.le.AdvertisingSetParameters;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    Thread thread,t;
    public static final String s = "...";
    LocationUpdaterService l;
    GPSTracker gps;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  l=new LocationUpdaterService();

        gps = new GPSTracker(MainActivity.this);

        startOn();


    }


      private void startOn(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        if( thread!=null ){
            thread.interrupt();
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){



                    if(gps.canGetLocation()) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();
                        long time = gps.getTime();

                       Log.d("!!!!!!!!!!!!!",latitude+"............."+longitude+"............"+time);
                    }
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    public void goToAccGraph(View view){
        Intent intent = new Intent(this, AccGraphNew.class);
        startActivity(intent);
    }

    public void goToGyroGraph(View view){
        Intent intent = new Intent(this, GyroGraph.class);
        startActivity(intent);
    }




}