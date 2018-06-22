package com.example.nirajvadhaiya.btp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    Thread thread;
    public static final String s = "...";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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