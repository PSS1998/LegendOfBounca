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
import android.widget.TextView;

import java.util.Date;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private float velocityZ;
    private float gradient = 0;
    private float timestamp;
    private static final float NS2S = 1.0f / 1000000000.0f;


    Button resetButton, randomButton;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resetButton = findViewById(R.id.gyroscope_reset);
        randomButton = findViewById(R.id.gyroscope_random);
        text = findViewById(R.id.textGyroscope);

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

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = null;
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }

        SensorEventListener gyroscopeEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
                    if (timestamp == 0) {
                        timestamp = sensorEvent.timestamp;
                    }

                    float alpha = 0.8f;
                    velocityZ = alpha * velocityZ + (1 - alpha) * sensorEvent.values[2];

                    final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                    gradient += velocityZ * dT ;

                    timestamp = sensorEvent.timestamp;
                    System.out.println(gradient);

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }


        };
        if (sensorManager != null) {
            sensorManager.registerListener(gyroscopeEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            System.out.println("sensorManager is null");
        }
    }

    public float getGradient() {
        return gradient;
    }

    //    @Override
//    protected void onStop() {
//        // Unregister the listener
//        sensorManager.unregisterListener(this);
//        super.onStop();
//    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}