package com.example.cpsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Trace;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Random;

public class GravityActivity extends AppCompatActivity {

    public double DISSIPATION_COEFFICIENT = .1;
    public double MASS = 0.01;
    public double MU_S = .15;
    public double MU_K = .07;

    private int SPEED_MULTIPLIER_CONSTANT = 1;
    private int ACCELERATION_MULTIPLIER_CONSTANT = 1;
    private float SPEED_THRESHOLD_TO_STOP = 10*ACCELERATION_MULTIPLIER_CONSTANT;
    private float SPEED_THRESHOLD_TO_IGNORE = 0.1*ACCELERATION_MULTIPLIER_CONSTANT;

    private float NS2US = 1.0f / 1000.0f; // ns to microsecond
    private float US2S = 1.0f / 1000000.0f; // microsecond to second
    private int READ_SENSOR_RATE = 20000; // sensor read rate in microsecond
    private int REFRESH_VIEW_RATE = 20000; // refresh View rate in microsecond

    private float lastReadSensorTimestamp = 0;
    private float lastRefreshViewTimestamp = 0;

    private float x;
    private float y;

    private double vx = 0;
    private double vy = 0;

    private double fx;
    private double fy;

    private int RIGHTEST_POSITION;
    private int BOTTOMMOST_POSITION;
    private int TOPMOST_POSITION;

    private boolean inGame = false;
    private View ball;

    Button resetButton, randomButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gravity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.ball = findViewById(R.id.ball);

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
                    float dT = (sensorEvent.timestamp - lastReadSensorTimestamp) * NS2US;
                    if (dT > READ_SENSOR_RATE) {
//                        Trace.beginSection("read-sensor-value");
                        double gravityX = sensorEvent.values[0];
                        double gravityY = sensorEvent.values[1];
//                        Trace.endSection();
                        if (inGame) {
                            calculateMovment(dT, gravityX, gravityY);
                        }
                        lastReadSensorTimestamp = sensorEvent.timestamp;
                    }
                    dT = (sensorEvent.timestamp - lastRefreshViewTimestamp) * NS2US;
                    if (dT > REFRESH_VIEW_RATE && inGame) {
//                        Trace.beginSection("move-ball");
                        moveBall();
//                        Trace.endSection();
                        lastRefreshViewTimestamp = sensorEvent.timestamp;
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
        final double time = dT * US2S;

        calculateForce(gravityX, gravityY);

        // calculate Accelaration
        double ax = fx * ACCELERATION_MULTIPLIER_CONSTANT;
        double ay = fy * ACCELERATION_MULTIPLIER_CONSTANT;

        // calculate Position
        double newX = (1/2) * ax * Math.pow(time, 2) + vx * time + x;
        double newY = (1/2) * ay * Math.pow(time, 2) + vy * time + y;
        x = (newX >= RIGHTEST_POSITION) ? RIGHTEST_POSITION : (float) ((newX <= 0) ? 0 : newX);
        y = (newY >= BOTTOMMOST_POSITION) ? BOTTOMMOST_POSITION : (float) ((newY <= TOPMOST_POSITION) ? TOPMOST_POSITION : newY);

        // calculate Velocity
        double newVX = ax * time + vx;
        double newVY = ay * time + vy;
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
//                        System.out.println("Static Friction: fx: " + fx + "; gravityY: " + gravityY + "; gravityX: " + gravityX);
                } else
                    fx = 0;
            } else {
                if (Math.abs(gravityY) > Math.abs(gravityX * MU_K)) {
                    newGravityY = Math.abs(gravityY) - Math.abs(gravityX * MU_K);
                    fx = (gravityY > 0) ? newGravityY : -newGravityY;
//                        System.out.println("Dynamic Friction: fx: " + fx + "; gravityY: " + gravityY + "; gravityX: " + gravityX);
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
        findViewById(R.id.ball).setVisibility(View.VISIBLE);

        View layout = findViewById(R.id.layout);
        int layoutRight = layout.getRight();
        int layoutBottom = layout.getBottom();

        View floor = findViewById(R.id.floor);
        int floorHeight = floor.getHeight();

        View toolbar1 = findViewById(R.id.toolbar);
        int ceilingHeight = toolbar1.getHeight();

        int movingObjectWidth = ball.getWidth();
        int movingObjectHeight = ball.getHeight();

        RIGHTEST_POSITION = layoutRight - movingObjectWidth;
        BOTTOMMOST_POSITION = layoutBottom - floorHeight - movingObjectHeight;
        TOPMOST_POSITION = 0 + ceilingHeight;

        RIGHTEST_POSITION = RIGHTEST_POSITION/SPEED_MULTIPLIER_CONSTANT;
        BOTTOMMOST_POSITION = BOTTOMMOST_POSITION/SPEED_MULTIPLIER_CONSTANT;
        TOPMOST_POSITION = TOPMOST_POSITION/SPEED_MULTIPLIER_CONSTANT;

        Random r = new Random();
        int i1 = r.nextInt(RIGHTEST_POSITION-2)+1;
        int i2 = r.nextInt(BOTTOMMOST_POSITION-TOPMOST_POSITION-2)+TOPMOST_POSITION+1;

        x = i1;
        y = i2;

        vx = 0;
        vy = 0;

        inGame = true;
    }

    private void randomVelocity() {
        float xTEMP = ball.getX();
        float yTEMP = ball.getY();

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

    public void moveBall() {
        ball.setX((float) SPEED_MULTIPLIER_CONSTANT*x);
        ball.setY((float) SPEED_MULTIPLIER_CONSTANT*y);
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
