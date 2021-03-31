package com.example.cpsproject;

import android.annotation.SuppressLint;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GyroscopeActivity extends AppCompatActivity {
    private GameManager gameManager;
    private Gyroscope gyroscope;

    Button resetButton;
    Button randomButton;
    @SuppressLint("StaticFieldLeak")
    static private ImageView ball;
    private TextView textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameManager = new GameManager(this);
        setContentView(R.layout.activity_gyroscope);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resetButton = findViewById(R.id.gyroscope_reset);
        randomButton = findViewById(R.id.gyroscope_random);
        textBox = findViewById(R.id.textGyroscope);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroscope = Gyroscope.getInstance(sensorManager);
        ball = (ImageView) findViewById(R.id.ball_objects2);
        if (ball == null)
            System.out.println("kheili khari");
        gameManager.createGameObjects(textBox, gyroscope);

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

    }

    @Override
    protected void onStop() {
        gyroscope.stop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            if (sensorManager != null) {
                gyroscope.start();
                gameManager.textBox = textBox;
                gameManager.start();
            }
        } catch (Exception e) {
            textBox.setText(e.getMessage());
        }
    }

    static public ImageView getBall() {
        return ball;
    }
}