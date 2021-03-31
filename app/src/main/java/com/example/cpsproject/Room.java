package com.example.cpsproject;

public class Room extends GameObject implements FrictionalSurface {
    private double STATIC_FRICTION_CONSTANT = 0.15f;
    private double DYNAMIC_FRICTION_CONSTANT = 0.07f;
    private Frame frame;
    private GameSensorListener sensorListener;

    public Room(String name, int width, int height, GameSensorListener sensorListener) {
        super(name);
        this.frame = new Frame(width, height);
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
