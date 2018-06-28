package com.example.nirajvadhaiya.btp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.Calendar;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class AccGraphNew extends AppCompatActivity implements SensorEventListener{
    private static final String TAG = "Acc_main";

    //DatabaseReference mRef;
    int cnt=0;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private LineChart mChart,yChart,zChart;
    private Thread thread;
    private boolean plotData = true;
    TextView tvX,tvY,tvZ;

    String filePath=android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + java.io.File.separator+"Analysisss.csv";

    //String filePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Analysis.csv";
//    java.io.File f = new java.io.File(filePath );
    java.io.File f=null;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_graph_new);

        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        if( mAccelerometer != null ){
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        tvX = (TextView)findViewById(R.id.tvX);
        tvY = (TextView)findViewById(R.id.tvY);
        tvZ = (TextView)findViewById(R.id.tvZ);

        //First Chart
        mChart = (LineChart)findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(true);
        mChart.getDescription().setText("Real Time Accelerometer!");

        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(false);
        mChart.setBackgroundColor(Color.DKGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.GREEN);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.RED);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.CYAN);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(50f);
        leftAxis.setAxisMinimum(-50f);
        leftAxis.setDrawGridLines(true);

        YAxis rigthAxis = mChart.getAxisRight();
        rigthAxis.setEnabled(false);

        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setDrawBorders(false);

        //Second Chart
        yChart = (LineChart)findViewById(R.id.chart2);
        yChart.getDescription().setEnabled(true);
        yChart.getDescription().setText("Real Time Accelerometer!");

        yChart.setTouchEnabled(false);
        yChart.setDragEnabled(false);
        yChart.setScaleEnabled(false);
        yChart.setDrawGridBackground(false);
        yChart.setPinchZoom(false);
        yChart.setBackgroundColor(Color.DKGRAY);

        LineData data2 = new LineData();
        data2.setValueTextColor(Color.WHITE);
        yChart.setData(data2);

        Legend l2 = yChart.getLegend();
        l2.setForm(Legend.LegendForm.LINE);
        l2.setTextColor(Color.GREEN);

        XAxis xl2 = yChart.getXAxis();
        xl2.setTextColor(Color.RED);
        xl2.setDrawGridLines(true);
        xl2.setAvoidFirstLastClipping(true);
        xl2.setEnabled(true);

        YAxis leftAxis2 = yChart.getAxisLeft();
        leftAxis2.setTextColor(Color.CYAN);
        leftAxis2.setDrawGridLines(false);
        leftAxis2.setAxisMaximum(50f);
        leftAxis2.setAxisMinimum(-50f);
        leftAxis2.setDrawGridLines(true);

        YAxis rigthAxis2 = yChart.getAxisRight();
        rigthAxis2.setEnabled(false);

        yChart.getAxisLeft().setDrawGridLines(false);
        yChart.getXAxis().setDrawGridLines(false);
        yChart.setDrawBorders(false);

        //Third Chart

        zChart = (LineChart)findViewById(R.id.chart3);
        zChart.getDescription().setEnabled(true);
        zChart.getDescription().setText("Real Time Accelerometer!");

        zChart.setTouchEnabled(false);
        zChart.setDragEnabled(false);
        zChart.setScaleEnabled(false);
        zChart.setDrawGridBackground(false);
        zChart.setPinchZoom(false);
        zChart.setBackgroundColor(Color.DKGRAY);

        LineData data3 = new LineData();
        data3.setValueTextColor(Color.WHITE);
        zChart.setData(data3);

        Legend lz = zChart.getLegend();
        lz.setForm(Legend.LegendForm.LINE);
        lz.setTextColor(Color.GREEN);

        XAxis xlz = zChart.getXAxis();
        xlz.setTextColor(Color.RED);
        xlz.setDrawGridLines(true);
        xlz.setAvoidFirstLastClipping(true);
        xlz.setEnabled(true);

        YAxis leftAxisz = zChart.getAxisLeft();
        leftAxisz.setTextColor(Color.CYAN);
        leftAxisz.setDrawGridLines(false);
        leftAxisz.setAxisMaximum(50f);
        leftAxisz.setAxisMinimum(-50f);
        leftAxisz.setDrawGridLines(true);

        YAxis rigthAxisz = zChart.getAxisRight();
        rigthAxisz.setEnabled(false);

        zChart.getAxisLeft().setDrawGridLines(false);
        zChart.getXAxis().setDrawGridLines(false);
        zChart.setDrawBorders(false);


        startOn();
    }

    private void startOn(){
        if( thread!=null ){
            thread.interrupt();
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    plotData=true;
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

    private void addEntry(SensorEvent event)throws java.io.IOException{
        //First Chart
        LineData data = mChart.getData();
        if( data != null ){
            ILineDataSet set = data.getDataSetByIndex(0);
            if(set ==null){
                set=createSet(1);
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(), (int) event.values[0]), 0);//x Axis
            tvX.setText(""+event.values[0]);
            Calendar c = Calendar.getInstance();
            Log.d("x:",""+event.values[0]+"___________"+c.getTime());
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(50);
            mChart.moveViewToX(data.getEntryCount());
        }

        //Second Chart
        LineData data2 = yChart.getData();
        if( data2 != null ){
            ILineDataSet set = data2.getDataSetByIndex(0);
            if(set ==null){
                set=createSet(2);
                data2.addDataSet(set);
                Log.d("info-> ", "createSet completed!");
            }
            data2.addEntry(new Entry(set.getEntryCount(), (int) event.values[1]), 0);//y Axis
            tvY.setText(""+event.values[1]);
            data2.notifyDataChanged();
            yChart.notifyDataSetChanged();
            yChart.setVisibleXRangeMaximum(50);
            yChart.moveViewToX(data2.getEntryCount());
        }

        //Third Chart
        LineData data3 = zChart.getData();
        if( data3 != null ){
            ILineDataSet set = data3.getDataSetByIndex(0);
            if(set ==null){
                set=createSet(3);
                data3.addDataSet(set);
            }
            data3.addEntry(new Entry(set.getEntryCount(), (int) event.values[2]), 0);//x Axis
            tvZ.setText(""+event.values[2]);
            data3.notifyDataChanged();
            zChart.notifyDataSetChanged();
            zChart.setVisibleXRangeMaximum(50);
            zChart.moveViewToX(data.getEntryCount());
        }

        Log.d("woohoo!!","????????????????//////////////////");


        String[] da={""+event.values[0],""+event.values[0],""+event.values[0]};

        Log.d("!!!!!!!!!!!!!!!!!!!",da[0]+"*******************");

        // File exist


        com.opencsv.CSVWriter writer;


        //java.io.File f=null;
        if(f!=null && f.exists()){
            java.io.FileWriter mFileWriter = new java.io.FileWriter(filePath , true);
            writer = new com.opencsv.CSVWriter(mFileWriter);
        }
        else {
            writer = new com.opencsv.CSVWriter(new java.io.FileWriter(filePath));
            Log.d("1","this is msg...");
            f = new java.io.File(filePath );
        }

        Log.d("woohoo!!",filePath+"************"+f.exists()+"*********************"+f.isDirectory()+"*****************");


        //String[] da={""+event.values[0],""+event.values[0],""+event.values[0]};
        writer.writeNext(da);

        Log.d("lol:","Successful.................................");
        writer.close();

    }

    private LineDataSet createSet(int i){
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        Log.d("info-> ", "createSet Entered! " + i + "...");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(1f);
        set.setColor(Color.MAGENTA);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.1f);
        return set;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if( plotData ){
            try{
                addEntry(sensorEvent);
            }catch(Exception e){}

            plotData=false;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onDestroy() {
        mSensorManager.unregisterListener(this);
        thread.interrupt();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if( thread!=null ){
            thread.interrupt();
        }
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onBackPressed() {

        String[] recipients = {"nirajvadhaiya@gmail.com","gauravlad21@gmail.com","csharp.pramod@gmail.com"};

        SendEmailAsyncTask emailAsyncTask = new SendEmailAsyncTask();
//emailAsyncTask.activity = this;
        String id = "slotter2018@gmail.com";
        String pass = "Abcd12345.";
        emailAsyncTask.m = new Mail(id, pass);
        emailAsyncTask.m.set_from(id);
        emailAsyncTask.m.setBody("Congratulations!\nData of Accelerometer has been collected sccessfully!\n\n\n\n\nRegards\nLAD & NIRAJ\n" );
        emailAsyncTask.m.set_to(recipients);
        emailAsyncTask.m.set_subject("live sensor data");
        try {
//            Toast.makeText(context.getApplicationContext(), "Attachment Passed",Toast.LENGTH_SHORT).show();
            emailAsyncTask.m.addAttachment(""+android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + java.io.File.separator+"Analysisss.csv", "AccData.csv");
            Log.d("o","Attachment Passed.........................................................");
        }
        catch (Exception e) {
//            Toast.makeText(context.getApplicationContext(), "Attachment Failed",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        emailAsyncTask.execute();
        Log.d("o","message Passed...RIP............+++++++++++++++++.............................................");


        super.onBackPressed();
    }
}


class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
    Mail m;
//    register_new activity;

    public SendEmailAsyncTask() {}

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (m.send()) {
                Log.d("****EMAIL CHECK***", "Email sent.");

            } else {
                Log.d("****EMAIL CHECK***", "Email failed to send.");
            }

            return true;
        } catch (AuthenticationFailedException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
            e.printStackTrace();
            Log.d("****EMAIL CHECK***", "Authentication failed.");
            return false;
        } catch (MessagingException e) {
            Log.e(SendEmailAsyncTask.class.getName(), "Email failed");
            e.printStackTrace();
            Log.d("****EMAIL CHECK***", "Email failed to send.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("****EMAIL CHECK***", "Unexpected error occured.");
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
//        activity.progressDialog.dismiss();
        Log.d("****EMAIL CHECK***", "Progress Dialog dismissed.");
//        activity.successDialog();
//        activity.clearDetails();
    }
}