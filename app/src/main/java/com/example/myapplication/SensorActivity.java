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

public class SensorActivity extends Fragment implements SensorEventListener {

    private static final String TAG = "SensorActivity";
    
    private TextView tv_steps;
    private TextView tv_distance;
    private TextView tv_timer;
    private Button btn_start;
    private Button btn_stop;
    private Button btn_reset;
    private Button btn_save;

    private SensorManager sensorManager;

    private boolean running = false;
    private int seconds = 0;
    private boolean isRun;
    private boolean wasRun;
    private float currentValue, newValue, actualSteps;
    private boolean startButtonClicked = false;
    private boolean stopButtonClicked = false;
    private String saveTime;


    private double MagnitudePrevious = 0;
    private Integer stepCount = 0;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRestApiCommunicator = retrofit.create(RestAPICommunication.class);

        wasRun = isRun;
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopButtonClicked = true;
                isRun = false;
                sensorManager.unregisterListener(SensorActivity.this);

            }
        });

        btn_reset = (Button)v.findViewById(R.id.resetBtn);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRun = false;
                seconds = 0;
                tv_steps.setText("0");
                tv_distance.setText("0");
                sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonClicked = true;
                isRun = true;
                running = true;
                Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

                if (countSensor != null){
                    sensorManager.registerListener(SensorActivity.this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
                } else {
                    //  sensorManager.registerListener(SensorActivity.this, countSensor, SensorManager.SENSOR_DELAY_FASTEST);
                    //Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_steps.setText(String.valueOf(actualSteps));
                System.out.println(actualSteps);
                System.out.println();
                saveWorkout();
            }
        });

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

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

        if (startButtonClicked) {

            currentValue = event.values[0];

            float distance = (float) (currentValue * 78) / (float) 100000;

            if (running) {

                //tv_steps.setText(String.valueOf(currentValue));
                tv_distance.setText(String.valueOf(distance) + " km");
            }
            startButtonClicked = false;
        }
        if (stopButtonClicked){
            newValue = event.values[0];
            actualSteps = newValue - currentValue;
            startButtonClicked = false;



        }
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

                        //Intent intent = new Intent(LogInFragment.this, MainActivity.class);

                        //startActivity(intent);
                    }
                    else{
                        makeToast("save workout failed");
                        Log.d(TAG,response.toString());
                    }
                }
                else
                    makeToast("save workout failed");
                Log.d(TAG,response.toString());
                //btnLock();
            }

            @Override
            public void onFailure(Call<ResponseSaveWorkout> call, Throwable t) {
                makeToast(t.getMessage());
                // btnLock();
            }
        });
    }

    private RequestSaveWorkout buildRequest(){
        //return new RequestSaveWorkout(
        //       Integer.parseInt(String.valueOf(actualSteps)),
         //       saveTime);

        //For testing
        return new RequestSaveWorkout(2000, "20:20");
    }

    private void makeToast(String msg){
        Toast.makeText(getContext() , msg, Toast.LENGTH_LONG).show();
    }
}
