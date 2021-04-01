package com.example.cpsproject;

import android.annotation.SuppressLint;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GyroscopeActivity extends AppCompatActivity {
    private GameManager gameManager;
    private Gyroscope gyroscope;

    Button resetButton;
    Button randomButton;
    @SuppressLint("StaticFieldLeak")
    static private ImageView ball;
    static private LinearLayout frame;
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
        ball = (ImageView) findViewById(R.id.ball_object);
        frame = (LinearLayout) findViewById(R.id.frame_layout);


//        frame = (LinearLayout)findViewById(R.id.frame_layout);
        ViewTreeObserver vto = frame.getViewTreeObserver();
//        int w = frame.getMeasuredWidth();
//        int h = frame.getMeasuredHeight();
//        System.out.println("w::: " + w + "  h:::" + h);


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.createGameObjects(textBox, gyroscope);
                gameManager.start();


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
//                gyroscope.start();
                gameManager.textBox = textBox;
//                gameManager.start();
            }
        } catch (Exception e) {
            textBox.setText(e.getMessage());
        }
    }

    static public ImageView getBall() {
        return ball;
    }

    public static LinearLayout getFrame() {
        return frame;
    }
}