package com.example.cpsproject;

public class Transform {
    private Vector position;
    private Vector velocity;
    private Vector acceleration;

    public Transform (Vector position, Vector velocity, Vector acceleration) {
        this.acceleration = acceleration;
        this.velocity = velocity;
        this.position = position;
    }

    public Transform () {
        this.acceleration = new Vector(0,0,0);
        this.velocity = new Vector(0,0,0);
        this.position = new Vector(0,0,0);
    }


    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }

    public void move(Vector displacement) {
        this.position.add(displacement);
    }

}
