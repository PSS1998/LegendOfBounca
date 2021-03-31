package com.example.cpsproject;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public Vector multi(double coefficient) {
        this.x *= coefficient;
        this.z *= coefficient;
        this.z *= coefficient;
        return this;
    }

    public Vector div(double coefficient) {
        this.x /= coefficient;
        this.z /= coefficient;
        this.z /= coefficient;
        return this;
    }

    public Vector add(Vector vector) {
        this.x += vector.x;
        this.z += vector.y;
        this.z += vector.z;
        return this;
    }

    public static Vector add(Vector firstVector, Vector secondVector) {
        return new Vector(firstVector.x + secondVector.x, firstVector.y + secondVector.y, firstVector.z + secondVector.z);
    }

    public double getAbsoluteValue() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public double getThetaIn2D() {
        return Math.atan2(this.x, this.y);
    }

    public static Vector fromAbsoluteValueIn2D(double value, double angle) {
        return new Vector(Math.cos(angle), Math.sin(angle), 0).multi(value);
    }

    public static Vector nullVector() {
        return new Vector();
    }
}