package com.example.cpsproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {

    Button resetButton, randomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resetButton = findViewById(R.id.gyroscope_reset);
        randomButton = findViewById(R.id.gyroscope_random);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/30/2021 Gyroscope reset action
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/30/2021 Gyroscope random action
            }
        });

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = null;
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }

        SensorEventListener gyroscopeDetector = new SensorEventListener() {
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
            sensorManager.registerListener(gyroscopeDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
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