package com.example.cpsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class GravityActivity extends AppCompatActivity implements SensorEventListener {

    Button resetButton, randomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resetButton = findViewById(R.id.gravity_reset);
        randomButton = findViewById(R.id.gravity_random);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/30/2021 Gravity reset action
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/30/2021 Gravity random action
            }
        });

        
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = null;
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }

        SensorEventListener gravityDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
                    System.out.println(": X: " + sensorEvent.values[0] + "; Y: " + sensorEvent.values[1] + "; Z: " + sensorEvent.values[2] + ";");
                    // TODO: 3/30/2021  
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        if (sensorManager != null) {
            sensorManager.registerListener(gravityDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            System.out.println("sensorManager is null");
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}