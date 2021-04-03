package com.example.cpsproject;

public interface Movable {
    double STOPPED_VELOCITY_THRESHOLD = 0.001;
    boolean isStopped();
    Vector getVelocity();
    void move(Vector position);
}
