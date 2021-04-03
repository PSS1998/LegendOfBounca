package com.example.cpsproject;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
    private Gravity gravity;

    Button resetButton;
    Button randomButton;
    @SuppressLint("StaticFieldLeak")
    static private ImageView ball;
    static private LinearLayout frame;
    private TextView textBox;
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
        textBox = findViewById(R.id.textGyroscope);
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gravity = Gravity.getInstance(sensorManager);
        gyroscope = Gyroscope.getInstance(sensorManager);
//                Gyroscope.getInstance(sensorManager);
        ball = (ImageView) findViewById(R.id.ball_object);
        frame = (LinearLayout) findViewById(R.id.frame_layout);
        frame.setBackgroundColor(Color.YELLOW);





//        frame = (LinearLayout)findViewById(R.id.frame_layout);
        ViewTreeObserver vto = frame.getViewTreeObserver();
//        int w = frame.getMeasuredWidth();
//        int h = frame.getMeasuredHeight();
//        System.out.println("w::: " + w + "  h:::" + h);


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
                    System.out.println("frame size:" + w + "h:" + h + "p:" + p);
                    gameManager.createGameObjects(textBox, gyroscope);
                    gameManager.start();
                    gyroscope.start();
                    //gravity.start();
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
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        try {
            if (sensorManager != null) {
                gameManager.textBox = textBox;
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