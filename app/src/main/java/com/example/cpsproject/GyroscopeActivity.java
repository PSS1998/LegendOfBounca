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

import android.service.autofill.CharSequenceTransformation;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class GyroscopeActivity extends AppCompatActivity {
    private final GameManager gameManager = new GameManager();
    private Gyroscope gyroscope;

    Button resetButton;
    Button randomButton;
    private TextView textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resetButton = findViewById(R.id.gyroscope_reset);
        randomButton = findViewById(R.id.gyroscope_random);
        textBox = findViewById(R.id.textGyroscope);

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
        try {
            if (sensorManager != null) {
                gyroscope = Gyroscope.getInstance(sensorManager);
                gameManager.createGameObjects(textBox, gyroscope);
                gyroscope.start();
                gameManager.textBox = textBox;
                gameManager.run();
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStop() {
        gyroscope.stop();
        super.onStop();
    }
}