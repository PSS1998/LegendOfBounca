package com.example.cpsproject;

import android.view.View;

public class Frame implements Inclinable, Meshable {
    private int width;
    private int height;
    private double theta;

    public Frame(int width, int height) {
        this.width = width;
        this.height = height;
        this.theta = 0;
    }

    public int getWidth() {
        return GyroscopeActivity.w;
    }

    public int getHeight() {
        return GyroscopeActivity.h;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getDistanceFromUpSide(Vector position) {
        return position.getY() - GyroscopeActivity.p;
//        return this.height;
    }

    public double getDistanceFromDownSide(Vector position) {
        return GyroscopeActivity.h - position.getY();
    }

    public double getDistanceFromRightSide(Vector position) {
        return GyroscopeActivity.w - position.getX();
    }

    public double getDistanceFromLeftSide(Vector position) {
        return position.getX();
    }

    @Override
    public boolean hasCollision(Meshable object) {
        return false;
    }

    @Override
    public Vector getVectorOfInteractionCollision(Transform transform, Collision collision) {

        if (collision == Collision.DOWN || collision == Collision.UP)
            return new Vector(transform.getVelocity().getX(), -transform.getVelocity().getY(), 0);
//        if (collision == Collision.UP)
//            return Vector.fromAbsoluteValueIn2D(-2 * absoluteVelocity * Math.sin(velocityAngle - this.theta - Math.PI), theta + Math.PI * 1.5);
        if (collision == Collision.RIGHT || collision == Collision.LEFT)
            return new Vector(-transform.getVelocity().getX(), transform.getVelocity().getY(), 0);
//        if (collision == Collision.LEFT)
//            return Vector.fromAbsoluteValueIn2D(-2 * absoluteVelocity * Math.sin(velocityAngle - this.theta - Math.PI / 2), theta + Math.PI);
        return new Vector();
    }

    @Override
    public double getTheta() {
        return this.theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
