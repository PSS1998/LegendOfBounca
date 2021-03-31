package com.example.cpsproject;

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
        return this.height - position.getY();
    }

    public double getDistanceFromDownSide(Vector position) {
        return position.getY();
    }

    public double getDistanceFromRightSide(Vector position) {
        return this.width - position.getX();
    }

    public double getDistanceFromLeftSide(Vector position) {
        return position.getX();
    }

    @Override
    public boolean hasCollision(Meshable object) {
        return false;
    }

    public Vector getVectorOfInteractionCollision(Transform transform, Collision collision) {
        double absoluteVelocity = transform.getVelocity().getAbsoluteValue();
        double velocityAngle = transform.getVelocity().getThetaIn2D();
        if (collision == Collision.DOWN || collision == Collision.UP) {
            return Vector.fromAbsoluteValueIn2D(-2 * absoluteVelocity * Math.sin(velocityAngle - this.theta), theta + Math.PI / 2);
        }
        if (collision == Collision.RIGHT || collision == Collision.LEFT) {
            return Vector.fromAbsoluteValueIn2D(-2 * absoluteVelocity * Math.sin(velocityAngle - this.theta), theta + Math.PI / 2);
        }
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
