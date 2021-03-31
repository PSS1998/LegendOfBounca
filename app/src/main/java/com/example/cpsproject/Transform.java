package com.example.cpsproject;

public class Transform {
    private Vector position;
    private Vector velocity;

    public Transform (Vector position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    public Transform () {
        this.velocity = new Vector(0,0,0);
        this.position = new Vector(0,0,0);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public void move(Vector displacement) {
        this.position.add(displacement);
    }
}
