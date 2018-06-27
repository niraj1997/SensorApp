package com.example.nirajvadhaiya.btp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.le.AdvertisingSetParameters;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {
    Thread thread,t;
    public static final String s = "...";
    LocationUpdaterService l;
    GPSTracker gps;
    boolean bo=false;
    TextView lati,longi,distance;
    private TextView batteryTxt;
    double latitude,longitude,dist;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            batteryTxt.setText(String.valueOf(level) + "%");
        }
    };


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        lati = (TextView) findViewById(R.id.latitude);
        longi = (TextView)findViewById(R.id.longitude);
        distance = (TextView)findViewById(R.id.distance);

        batteryTxt = (TextView)findViewById(R.id.batteryTxt);

        bo =false;
      //  l=new LocationUpdaterService();

        gps = new GPSTracker(MainActivity.this);

        isStoragePermissionGranted();

        startOn();

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            } else {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

      private void startOn(){


        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true)
                {

                    if(gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        long time = gps.getTime();


                        dist = distance(latitude,longitude,23.1911,72.6295);

                        Log.d("!!!!!!!!!!!!!","lat:: "+latitude+"............."+"long:: "+longitude+"............"+time);


                        if((latitude-23.18 <=0 && longitude-72.62<=0) || dist<1 && !bo) {
                            bo =true;
                            thread.interrupt();
                            Intent intent = new Intent(MainActivity.this, AccGraphNew.class);
                            startActivity(intent);
                        }

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