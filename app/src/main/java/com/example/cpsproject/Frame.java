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
        return width;
    }

    public int getHeight() {
        return height;
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
        double absoluteVelocity = transform.getVelocity().getAbsoluteValue();
        double velocityAngle = transform.getVelocity().getThetaIn2D();
        float differenceAngle = (float)(velocityAngle - this.theta);
        return transform.getVelocity().multi(-1).rotate2D(differenceAngle);
//        if (collision == Collision.DOWN)
//            return Vector.fromAbsoluteValueIn2D(-2 * absoluteVelocity * Math.sin(velocityAngle - this.theta), theta + Math.PI / 2);
//        if (collision == Collision.UP)
//            return Vector.fromAbsoluteValueIn2D(-2 * absoluteVelocity * Math.sin(velocityAngle - this.theta - Math.PI), theta + Math.PI * 1.5);
//        if (collision == Collision.RIGHT)
//            return Vector.fromAbsoluteValueIn2D(-2 * absoluteVelocity * Math.sin(velocityAngle - this.theta + Math.PI / 2), theta);
//        if (collision == Collision.LEFT)
//            return Vector.fromAbsoluteValueIn2D(-2 * absoluteVelocity * Math.sin(velocityAngle - this.theta - Math.PI / 2), theta + Math.PI);
//        return new Vector();
    }

    @Override
    public double getTheta() {
        return this.theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }
}
