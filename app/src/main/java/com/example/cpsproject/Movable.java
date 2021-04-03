package com.example.cpsproject;

public interface Movable {
    double STOPPED_VELOCITY_THRESHOLD = 0.001;
    Vector getVelocity();
    void move(Vector position);
}
