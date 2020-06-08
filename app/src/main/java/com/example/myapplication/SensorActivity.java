package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.data.ClientInformation;
import com.example.myapplication.server.RequestSaveWorkout;
import com.example.myapplication.server.ResponseSaveWorkout;
import com.example.myapplication.server.RestAPICommunication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.myapplication.server.ServerData.BASE_URL;


import java.util.Locale;

// SensorActivity is the class that utilizes the sensors of the device to make it possible to calculate the steps and distance for our application.

public class SensorActivity extends Fragment implements SensorEventListener {

    private static final String TAG = "SensorActivity";

    //UI variables
    private TextView tv_steps;
    private TextView tv_distance;
    private TextView tv_timer;
    private Button btn_start;
    private Button btn_stop;
    private Button btn_reset;
    private Button btn_save;

    //Sensormanager that let us access the devices sensors
    private SensorManager sensorManager;

    //variables
    private boolean running = false;
    private int seconds = 0;
    private boolean isRun;
    private boolean wasRun;
    private float currentValue, newValue, actualSteps;
    private String saveTime;
    private long steps = 0;

    //Implemented for the demo, visual effect of a button thats being clicked
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;

    //Used for communication with the REST API
    private RestAPICommunication mRestApiCommunicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sensor, container, false);

        tv_steps = (TextView)v.findViewById(R.id.tv_steps);
        tv_distance = (TextView)v.findViewById(R.id.tv_distance);
        tv_timer = (TextView)v.findViewById(R.id.tv_time);
        btn_start = (Button)v.findViewById(R.id.startBtn);
        btn_stop = (Button)v.findViewById(R.id.stopBtn);
        btn_save = (Button)v.findViewById(R.id.saveBtn);
        btn_reset = (Button)v.findViewById(R.id.resetBtn);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRestApiCommunicator = retrofit.create(RestAPICommunication.class);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        wasRun = isRun;

        //When button stop is pressed the sensor is inactivated with unregisterlistener
        //to stop count the steps
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                getNewSteps();
                isRun = false;
                sensorManager.unregisterListener(SensorActivity.this);
                actualSteps = newValue - currentValue;
            }
        });

        //Resets the values before a new workout
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                isRun = false;
                seconds = 0;
                steps = 0;
                tv_steps.setText("0");
                tv_distance.setText("0");
                sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            }
        });

        //When button start is pressed the sensormanager is activated with registerlistener
        //to start the sensorprocess and in this case its stepcounter
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                isRun = true;
                running = true;
                Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

                if (countSensor != null){
                    sensorManager.registerListener(SensorActivity.this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
                } else {
                    //  sensorManager.registerListener(SensorActivity.this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
                    //Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
                }

                getCurrentSteps();
            }
        });

        //When the workout is done the save button is pressed to save the workout
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                tv_steps.setText(String.valueOf(actualSteps));
                System.out.println(actualSteps);
                System.out.println();
                saveWorkout();
            }
        });



        if (savedInstanceState != null) {

            seconds
                    = savedInstanceState
                    .getInt("seconds");
            isRun
                    = savedInstanceState
                    .getBoolean("running");
            wasRun
                    = savedInstanceState
                    .getBoolean("wasRunning");
        }
        runTimer();


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (wasRun) {
            isRun = true;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       steps++;
    }

    //getCurrentSteps and getNewSteps are methods for resetting the number of steps from the sensor for every new workout

    public void getCurrentSteps(){
        currentValue = steps;
        System.out.println("CURRENT VALUE: " + currentValue);
    }

    public void getNewSteps(){
        newValue = steps;
        System.out.println("NEW VALUE: " + newValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSaveInstanceState(
            Bundle savedInstanceState)
    {
        savedInstanceState
                .putInt("seconds", seconds);
        savedInstanceState
                .putBoolean("running", isRun);
        savedInstanceState
                .putBoolean("wasRunning", wasRun);
    }

    //This method handles the timer that gives us the time for the entire workout from start to stop
    private void runTimer()
    {
        final Handler handler
                = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run()
            {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                String time
                        = String
                        .format(Locale.getDefault(),
                                "%d:%02d:%02d", hours,
                                minutes, secs);
                saveTime = time;
                tv_timer.setText(saveTime);

                if (isRun) {
                    seconds++;
                }

                handler.postDelayed(this, 1000);
            }
        });
    }

    //The saveWorkout method saves the workout that has been done and pushes it to the cloud.
    private void saveWorkout(){
        Call<ResponseSaveWorkout> call = mRestApiCommunicator.saveWorkout(
                buildRequest(), ClientInformation.getInstance().getToken());
        call.enqueue(new Callback<ResponseSaveWorkout>() {
            @Override
            public void onResponse(Call<ResponseSaveWorkout> call, Response<ResponseSaveWorkout> response) {
                ResponseSaveWorkout responseSaveWorkout = response.body();
                if( responseSaveWorkout != null){
                    if( responseSaveWorkout.getStatus().equals("success")) {
                        Log.d(TAG, "onResponse: status: " + responseSaveWorkout.getStatus());
                        Log.d(TAG, "onResponse: message: " + responseSaveWorkout.getMessage());
                        Log.d(TAG, "onResponse: distance " + responseSaveWorkout.getDistance());

                        tv_distance.setText(String.valueOf(responseSaveWorkout.getDistance()));
                    }
                    else{
                        makeToast("save workout failed");
                        Log.d(TAG,response.toString());
                    }
                }
                else{
                    makeToast("save workout failed");
                }
                Log.d(TAG,response.toString());

            }

            @Override
            public void onFailure(Call<ResponseSaveWorkout> call, Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }

    private RequestSaveWorkout buildRequest(){
        int steps = (int) actualSteps;
        return new RequestSaveWorkout(steps, saveTime);

        //For testing
        //return new RequestSaveWorkout(2000, "20:20");
    }

    private void makeToast(String msg){
        Toast.makeText(getContext() , msg, Toast.LENGTH_LONG).show();
    }
}
