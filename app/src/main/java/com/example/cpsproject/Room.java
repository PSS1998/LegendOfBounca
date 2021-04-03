package com.example.cpsproject;

import android.widget.TextView;

public class Room extends GameObject implements FrictionalSurface {
    private final double STATIC_FRICTION_CONSTANT = 0.15f;
    private final double DYNAMIC_FRICTION_CONSTANT = 0.07f;
    private final Frame frame;
    private final GameSensorListener sensorListener;

    public Room(String name, int width, int height, int top, GameSensorListener sensorListener) {
        super(name);
        this.frame = new Frame(width, height, top);
        this.sensorListener = sensorListener;
    }

    public Frame getFrame() {
        return frame;
    }

    @Override
    public double getStaticFrictionConstant() {
        return this.STATIC_FRICTION_CONSTANT;
    }

    @Override
    public double getDynamicFrictionConstant() {
        return this.DYNAMIC_FRICTION_CONSTANT;
    }

    @Override
    void update(double deltaTime) {
        this.frame.setTheta(sensorListener.getGradient());
    }
}
