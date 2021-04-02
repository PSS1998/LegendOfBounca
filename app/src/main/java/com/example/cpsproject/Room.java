package com.example.cpsproject;

import android.widget.TextView;

public class Room extends GameObject implements FrictionalSurface {
    private double STATIC_FRICTION_CONSTANT = 0.15f ;
    private double DYNAMIC_FRICTION_CONSTANT = 0.07f ;
    private Frame frame;
    private GameSensorListener sensorListener;
    public TextView textBox;
    int counter = 0;

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
//        textBox.setText(new String("Iman update"));
//                  .concat(String.valueOf(this.frame.getTheta())));
        this.frame.setTheta(sensorListener.getGradient());
        System.out.println("THETA: " + this.frame.getTheta());
    }
}
