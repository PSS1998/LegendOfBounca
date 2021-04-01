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

import java.util.Random;

public class GravityActivity extends AppCompatActivity {

    public static double DISSIPATION_COEFFICIENT = .1;
    public static double MASS = 0.01;
    public static double MU_S = .15;
    public static double MU_K = .07;

    public static int SPEED_MULTIPLIER_CONSTANT = 1;
    public static int ACCELERATION_MULTIPLIER_CONSTANT = 5;
    public static float SPEED_THRESHOLD_TO_STOP = 10*ACCELERATION_MULTIPLIER_CONSTANT;
    public static float SPEED_THRESHOLD_TO_IGNORE = 2*ACCELERATION_MULTIPLIER_CONSTANT;

    public static float NS2US = 1.0f / 1000.0f; // ns to microsecond
    public static float US2S = 1.0f / 1000000.0f; // microsecond to second
    public static int READ_SENSOR_RATE = 20; // sensor read rate in microsecond
    public static int REFRESH_VIEW_RATE = 20000; // refresh View rate in microsecond

    private float readSensorTimestamp = 0;
    private float refreshViewTimestamp = 0;

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

        resetButton.setOnClickListener(view -> resetGame());
        randomButton.setOnClickListener(view -> randomVelocity());


        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = null;
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        }

        SensorEventListener gravityDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent != null) {
//                    System.out.println(": X: " + sensorEvent.values[0] + "; Y: " + sensorEvent.values[1] + "; Z: " + sensorEvent.values[2] + ";");
//                    System.out.println(": x: " + x + "; y: " + y + "; vx: " + vx + ";" + "; vy: " + vy + ";"+ "; fx: " + fx + "; fy: " + fy + ";");
                    float dT = (sensorEvent.timestamp - readSensorTimestamp) * NS2US;
                    if (dT > READ_SENSOR_RATE) {
                        double gravityX = sensorEvent.values[0];
                        double gravityY = sensorEvent.values[1];

                        if (gameStarted) {
                            calculateMovment(dT, gravityX, gravityY);
                        }
                        readSensorTimestamp = sensorEvent.timestamp;
                    }
                    dT = (sensorEvent.timestamp - refreshViewTimestamp) * NS2US;
                    if (dT > REFRESH_VIEW_RATE && gameStarted) {
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

    private void calculateMovment(float dT, double gravityX, double gravityY) {
        final double time_slice = dT * US2S;

        calculateForce(gravityX, gravityY);

        // calculate Accelaration
        double ax = fx * ACCELERATION_MULTIPLIER_CONSTANT;
        double ay = fy * ACCELERATION_MULTIPLIER_CONSTANT;

        // calculate Position
        double newX = (0.5) * ax * Math.pow(time_slice, 2) + vx * time_slice + x;
        double newY = (0.5) * ay * Math.pow(time_slice, 2) + vy * time_slice + y;
        x = (newX >= RIGHTEST_POSITION) ? RIGHTEST_POSITION : (float) ((newX <= 0) ? 0 : newX);
        y = (newY >= BOTTOMMOST_POSITION) ? BOTTOMMOST_POSITION : (float) ((newY <= TOPMOST_POSITION) ? TOPMOST_POSITION : newY);

        // calculate Velocity
        double newVX = ax * time_slice + vx;
        double newVY = ay * time_slice + vy;
        newVX = ((x == RIGHTEST_POSITION) && (Math.abs(newVX) < SPEED_THRESHOLD_TO_IGNORE)) ? 0 : newVX;
        newVX = ((x == 0) && (Math.abs(newVX) < SPEED_THRESHOLD_TO_IGNORE)) ? 0 : newVX;
        newVY = ((y == BOTTOMMOST_POSITION) && (Math.abs(newVY) < SPEED_THRESHOLD_TO_IGNORE)) ? 0 : newVY;
        newVY = ((y == TOPMOST_POSITION) && (Math.abs(newVY) < SPEED_THRESHOLD_TO_IGNORE)) ? 0 : newVY;

        calculateCollision(newX, newY, newVX, newVY);
    }

    private void calculateCollision(double newX, double newY, double newVX, double newVY) {
        int collision = hasCollision(newX, newY, vx, vy);
        if(collision > 0) {
            if ((collision == 1) || (collision == 2)) {
                if(Math.abs(newVX) < SPEED_THRESHOLD_TO_STOP) {
                    vx = 0;
                    vy = newVY;
                    if(collision == 1)
                        x = RIGHTEST_POSITION;
                    if(collision == 2)
                        x = 0;
                }
                else {
                    vx = -newVX * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                    vy = newVY * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                }
            }
            if ((collision == 3) || (collision == 4)) {
                if(Math.abs(newVY) < SPEED_THRESHOLD_TO_STOP) {
                    vy = 0;
                    vx = newVX;
                    if(collision == 3)
                        y = BOTTOMMOST_POSITION;
                    if(collision == 4)
                        y = TOPMOST_POSITION;
                }
                else {
                    vx = newVX * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                    vy = -newVY * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
//                    System.out.println("ENERGY DISSIPATION: after contact vy: " + vy + "; before contact VY: " + newVY);
                }
            }
            if(((collision == 1) && ((collision == 3) || (collision == 4))) || ((collision == 2) && ((collision == 3) || (collision == 4)))) {
                if((Math.abs(newVX) < SPEED_THRESHOLD_TO_STOP) || (Math.abs(newVY) < SPEED_THRESHOLD_TO_STOP)) {
                    if (Math.abs(newVX) < SPEED_THRESHOLD_TO_STOP) {
                        vx = 0;
                        if (collision == 1)
                            x = RIGHTEST_POSITION;
                        if (collision == 2)
                            x = 0;
                        vy = newVY;
                    }
                    if (Math.abs(newVY) < SPEED_THRESHOLD_TO_STOP) {
                        vy = 0;
                        if (collision == 3)
                            y = BOTTOMMOST_POSITION;
                        if (collision == 4)
                            y = TOPMOST_POSITION;
                        vx = newVX;
                    }
                }
                else {
                    vx = -newVX * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                    vy = -newVY * Math.sqrt(1 - DISSIPATION_COEFFICIENT);
                }
            }
        }
        else{
            vx = newVX;
            vy = newVY;
        }
    }

    private void calculateForce(double gravityX, double gravityY) {
        double newGravityY;
        double newGravityX;

        if((vy == 0) && ((y == BOTTOMMOST_POSITION) || (y == TOPMOST_POSITION) || (x == 0) || (x == RIGHTEST_POSITION))) {
            if (vx == 0) {
                if (Math.abs(gravityY) > Math.abs(gravityX * MU_S)) {
                    newGravityY = Math.abs(gravityY) - Math.abs(gravityX * MU_S);
                    fx = (gravityY > 0) ? newGravityY : -newGravityY;
//                        System.out.println("Static Friction: fy: " + fy + "; gravityX: " + gravityX);
                } else
                    fx = 0;
            } else {
                if (Math.abs(gravityY) > Math.abs(gravityX * MU_K)) {
                    newGravityY = Math.abs(gravityY) - Math.abs(gravityX * MU_K);
                    fx = (gravityY > 0) ? newGravityY : -newGravityY;
//                        System.out.println("Dynamic Friction: fy: " + fy + "; gravityX: " + gravityX);
                } else
                    fx = 0;
            }
        }
        else {
            fx = gravityY;
        }
        if((vx == 0) && ((y == BOTTOMMOST_POSITION) || (y == TOPMOST_POSITION) || (x == 0) || (x == RIGHTEST_POSITION))) {
            if (vy == 0) {
                if (Math.abs(gravityX) > Math.abs(gravityY * MU_S)) {
                    newGravityX = Math.abs(gravityX) - Math.abs(gravityX * MU_S);
                    fy = (gravityX > 0) ? newGravityX : -newGravityX;
//                        System.out.println("Static Friction: fy: " + fy + "; gravityX: " + gravityX);
                } else
                    fy = 0;
            } else {
                if (Math.abs(gravityX) > Math.abs(gravityY * MU_K)) {
                    newGravityX = Math.abs(gravityX) - Math.abs(gravityX * MU_K);
                    fy = (gravityX > 0) ? newGravityX : -newGravityX;
//                        System.out.println("Dynamic Friction: fy: " + fy + "; gravityX: " + gravityX);
                } else
                    fy = 0;
            }
        }
        else {
            fy = gravityX;
        }
    }

    private void resetGame() {
        findViewById(R.id.movingObject).setVisibility(View.VISIBLE);

        View layout = findViewById(R.id.layout);
        int layoutRight = layout.getRight();
        int layoutBottom = layout.getBottom();

        View floor = findViewById(R.id.floor);
        int floorHeight = floor.getHeight();

        View toolbar1 = findViewById(R.id.toolbar);
        int ceilingHeight = toolbar1.getHeight();

        int movingObjectWidth = movingObject.getWidth();
        int movingObjectHeight = movingObject.getHeight();

        RIGHTEST_POSITION = layoutRight - movingObjectWidth;
        BOTTOMMOST_POSITION = layoutBottom - floorHeight - movingObjectHeight;
        TOPMOST_POSITION = 0 + ceilingHeight;

        RIGHTEST_POSITION = RIGHTEST_POSITION/SPEED_MULTIPLIER_CONSTANT;
        BOTTOMMOST_POSITION = BOTTOMMOST_POSITION/SPEED_MULTIPLIER_CONSTANT;
        TOPMOST_POSITION = TOPMOST_POSITION/SPEED_MULTIPLIER_CONSTANT;

        System.out.println("size of frame r:" + RIGHTEST_POSITION + " b:" + BOTTOMMOST_POSITION);

        Random r = new Random();
        int i1 = r.nextInt(RIGHTEST_POSITION-2)+1;
        int i2 = r.nextInt(BOTTOMMOST_POSITION-TOPMOST_POSITION-2)+TOPMOST_POSITION+1;

        x = i1;
        y = i2;

        vx = 0;
        vy = 0;

        gameStarted = true;
    }

    private void randomVelocity() {
        float xTEMP = movingObject.getX();
        float yTEMP = movingObject.getY();

        View layout = findViewById(R.id.layout);
        int layoutRight = layout.getRight();
        int layoutBottom = layout.getBottom();

        Random r = new Random();
        int i1 = r.nextInt((500/SPEED_MULTIPLIER_CONSTANT)*ACCELERATION_MULTIPLIER_CONSTANT-(50/SPEED_MULTIPLIER_CONSTANT)*ACCELERATION_MULTIPLIER_CONSTANT)+(50/SPEED_MULTIPLIER_CONSTANT)*ACCELERATION_MULTIPLIER_CONSTANT;
        int i2 = r.nextInt((500/SPEED_MULTIPLIER_CONSTANT)*ACCELERATION_MULTIPLIER_CONSTANT-(50/SPEED_MULTIPLIER_CONSTANT)*ACCELERATION_MULTIPLIER_CONSTANT)+(50/SPEED_MULTIPLIER_CONSTANT)*ACCELERATION_MULTIPLIER_CONSTANT;

        if (xTEMP > layoutRight/2) {
            vx = -i1;
        }
        else{
            vx = i1;
        }
        if (yTEMP > layoutBottom/2) {
            vy = -i2;
        }
        else{
            vy = i2;
        }
    }

    public void moveObject() {
        movingObject.setX((float) SPEED_MULTIPLIER_CONSTANT*x);
        movingObject.setY((float) SPEED_MULTIPLIER_CONSTANT*y);
    }

    public int hasCollision(double newX, double newY, double newVX, double newVY) {
        if((newX >= RIGHTEST_POSITION) && (newVX > 0)) {
            return 1;
        }
        if((newX <= 0) && (newVX < 0)) {
            return 2;
        }
        if((newY >= BOTTOMMOST_POSITION) && (newVY > 0)) {
            return 3;
        }
        if((newY <= TOPMOST_POSITION) && (newVY < 0)) {
            return 4;
        }
        return 0;
    }

}