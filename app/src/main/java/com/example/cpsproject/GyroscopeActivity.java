package com.example.cpsproject;

import android.annotation.SuppressLint;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class GyroscopeActivity extends AppCompatActivity {
    private GameManager gameManager;
    private Gyroscope gyroscope;
    private Gravity gravity;

    Button resetButton;
    Button randomButton;
    @SuppressLint("StaticFieldLeak")
    static private ImageView ball;
    public static int w, h, p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameManager = new GameManager();
        setContentView(R.layout.activity_gyroscope);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        resetButton = findViewById(R.id.gyroscope_reset);
        randomButton = findViewById(R.id.gyroscope_random);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravity = Gravity.getInstance(sensorManager);
        gyroscope = Gyroscope.getInstance(sensorManager);
        ball = (ImageView) findViewById(R.id.ball_object);

        resetButton.setOnClickListener(new View.OnClickListener() {
            boolean isStarted = false;
            @Override
            public void onClick(View view) {
                if (!isStarted) {
                    View layout = findViewById(R.id.main_layout);
                    int layoutRight = layout.getRight();
                    int layoutBottom = layout.getBottom();

                    View floor = findViewById(R.id.floor);
                    int floorHeight = floor.getHeight();
                    int ceilingHeight = toolbar.getHeight();
                    w = layoutRight;
                    h = layoutBottom - floorHeight;
                    p = ceilingHeight;
                    gameManager.createGameObjects(gyroscope);
                    gameManager.start();
                    gyroscope.start();
                    resetButton.setText("Restart");
                    isStarted = true;
                } else {
                    gameManager.setRandomBallPosition();
                }
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameManager.setRandomBallVelocity();
            }
        });

    }

    @Override
    protected void onStop() {
        gyroscope.stop();
        gameManager.stopGame();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    static public ImageView getBall() {
        return ball;
    }
}