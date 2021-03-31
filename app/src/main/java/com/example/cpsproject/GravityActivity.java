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

import java.util.Random;

public class GravityActivity extends AppCompatActivity {

    public static double DISSIPATION_COEFFICIENT = .1;
    public static double MASS = 0.01;
    public static double MU_S = .15;
    public static double MU_K = .07;

    public static float NS2US = 1.0f / 1000.0f; // ns to microsecond
    public static float US2S = 1.0f / 1000000.0f; // microsecond to second
    public static int READ_SENSOR_RATE = 20; // sensor read rate in microsecond
    public static int UPDATE_VIEW_RATE = 15 * 1000; // refresh View rate in microsecond
    public static double GRAVITY_CONSTANT = 9.80665;

    private float readSensorTimestamp = 1;
    private float refreshViewTimestamp = 1;

    private float x;
    private float y;

    private double vx = 0;
    private double vy = 0;

    private double fx;
    private double fy;

    public int RIGHTEST_POSITION;
    private int BOTTOMMOST_POSITION;
    private int TOPMOST_POSITION;

    private boolean gameStarted = false;
    private View movingObject;

    Button resetButton, randomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.movingObject = findViewById(R.id.movingObject);

        resetButton = findViewById(R.id.gravity_reset);
        randomButton = findViewById(R.id.gravity_random);

        resetButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO: 3/30/2021 Gravity reset action
                findViewById(R.id.movingObject).setVisibility(View.VISIBLE);

                View layout = findViewById(R.id.layout);
                int layoutRight = layout.getRight();
                int layoutBottom = layout.getBottom();

                View floor = findViewById(R.id.floor);
                int floorHeight = floor.getHeight();

                View toolbar = findViewById(R.id.toolbar);
                int ceilingHeight = toolbar.getHeight();

                int movingObjectWidth = movingObject.getWidth();
                int movingObjectHeight = movingObject.getHeight();

                RIGHTEST_POSITION = layoutRight - movingObjectWidth;
                BOTTOMMOST_POSITION = layoutBottom - floorHeight - movingObjectHeight;
                TOPMOST_POSITION = 0 + ceilingHeight;

                Random r = new Random();
                int i1 = r.nextInt(RIGHTEST_POSITION);
                int i2 = r.nextInt(BOTTOMMOST_POSITION-TOPMOST_POSITION)+TOPMOST_POSITION;

                x = i1;
                y = i2;

                vx = 0;
                vy = 0;

                gameStarted = true;
            }
        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 3/30/2021 Gravity random action
                x = movingObject.getX();
                y = movingObject.getY();

                View layout = findViewById(R.id.layout);
                int layoutRight = layout.getRight();
                int layoutBottom = layout.getBottom();

                Random r = new Random();
                int i1 = r.nextInt(500-50)+50;
                int i2 = r.nextInt(500-50)+50;

                if (x > layoutRight/2){
                    vx = -i1;
                }
                else{
                    vx = i1;
                }
                if (y > layoutBottom/2){
                    vy = -i2;
                }
                else{
                    vy = i2;
                }
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
                    float dT = (sensorEvent.timestamp - readSensorTimestamp) * NS2US;
                    if (dT > READ_SENSOR_RATE) {
                        double gravityX = sensorEvent.values[0];
                        double gravityY = sensorEvent.values[1];
                        double gravityZ = sensorEvent.values[2];

                        if (gameStarted) {
                            final double time_slice = dT * US2S;

                            if(vy == 0) {
                                if(vx == 0) {
                                    if(gravityY > (gravityX * MU_S))
                                        fx = gravityY - (gravityX * MU_S);
                                    else
                                        fx = 0;
                                }
                                else {
                                    if(gravityY > (gravityX * MU_K))
                                        fx = gravityY - (gravityX * MU_K);
                                    else
                                        fx = 0;
                                }
                            }
                            else {
                                fx = gravityY;
                            }
                            if(vx == 0) {
                                if(vy == 0) {
                                    if(gravityX > (gravityY * MU_S))
                                        fy = gravityX - (gravityY * MU_S);
                                    else
                                        fy = 0;
                                }
                                else {
                                    if(gravityX > (gravityY * MU_K))
                                        fy = gravityX - (gravityY * MU_K);
                                    else
                                        fy = 0;
                                }
                            }
                            else {
                                fy = gravityX;
                            }

                            double ax = fx / MASS;
                            double ay = fy / MASS;

                            double newX = (0.5) * ax * Math.pow(time_slice, 2) + vx * time_slice + x;
                            double newY = (0.5) * ay * Math.pow(time_slice, 2) + vy * time_slice + y;
                            x = (newX >= RIGHTEST_POSITION) ? RIGHTEST_POSITION : (float) ((newX <= 0) ? 0 : newX);
                            y = (newY >= BOTTOMMOST_POSITION) ? BOTTOMMOST_POSITION : (float) ((newY <= TOPMOST_POSITION) ? TOPMOST_POSITION : newY);

                            double newVX = ax * time_slice + vx;
                            double newVY = ay * time_slice + vy;

                            if((newX >= RIGHTEST_POSITION || newX <= 0) || (newY >= BOTTOMMOST_POSITION || newY <= TOPMOST_POSITION)) {
                                if (newX >= RIGHTEST_POSITION || newX <= 0) {
                                    vx = -newVX * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                                    vy = newVY * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                                }
                                if (newY >= BOTTOMMOST_POSITION || newY <= TOPMOST_POSITION) {
                                    vx = newVX * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                                    vy = -newVY * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                                }
                                if((newX >= RIGHTEST_POSITION || newX <= 0) && (newY >= BOTTOMMOST_POSITION || newY <= TOPMOST_POSITION)){
                                    vx = -newVX * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                                    vy = -newVY * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                                }
                            }
                            else{
                                vx = newVX;
                                vy = newVY;
                            }

                        }
                        readSensorTimestamp = sensorEvent.timestamp;
                    }
                    dT = (sensorEvent.timestamp - refreshViewTimestamp) * NS2US;
                    if (dT > UPDATE_VIEW_RATE && gameStarted) {
                        moveObject();
                        refreshViewTimestamp = sensorEvent.timestamp;
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        if (sensorManager != null) {
            sensorManager.registerListener(gravityDetector, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            System.out.println("sensorManager is null");
        }

    }

    public void moveObject() {
        movingObject.setX((float) x);
        movingObject.setY((float) y);
    }

}