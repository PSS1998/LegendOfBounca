package com.example.cpsproject;

import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

public class Gyroscope extends GameSensorListener {
    private double velocityZ;
    private double gradient = 0;
    private double timestamp;
    private static final double NS2S = 1.0f / 1000000000.0f;

    static private Gyroscope gyroscope;


    private Gyroscope(SensorManager sensorManager) {
        super(sensorManager);
    }

    public static Gyroscope getInstance(SensorManager sensorManager) {
        if (gyroscope == null)
            gyroscope = new Gyroscope(sensorManager);
        return gyroscope;
    }

    public Sensor createSensor() {
        return super.sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    public double getGradient() {
        if (Math.abs(gradient) > Math.PI/2.0)
            System.out.println("error gradient");


        if (gradient < 0)
            return Math.PI + gradient;
        return gradient;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null) {
            if (timestamp == 0)
                timestamp = event.timestamp;
            double alpha = 0.8;
            final double dT = (event.timestamp - timestamp) * NS2S;
            velocityZ = alpha * velocityZ + (1 - alpha) * event.values[2];
            gradient += velocityZ * dT;
            timestamp = event.timestamp;
            System.out.println("gradient: " + gradient);
        }
    }
}
