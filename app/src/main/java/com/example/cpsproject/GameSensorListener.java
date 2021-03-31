package com.example.cpsproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

abstract public class GameSensorListener implements SensorEventListener {
    protected SensorManager sensorManager;

    public GameSensorListener(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    abstract public Sensor createSensor();
    abstract public double getGradient();

    @Override
    abstract public void onSensorChanged(SensorEvent event);

    public void start() {
        Sensor sensor = createSensor();
        this.sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
