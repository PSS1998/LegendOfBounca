package com.example.cpsproject;

import android.content.Context;
import android.system.ErrnoException;
import android.widget.TextView;

import java.util.ArrayList;

public class Ball extends GameObject implements Weighable, Meshable, Movable {
    private final static float DISSIPATION_COEFFICIENT = 0.1f;
    private double mass;
    private final Transform transform;
    private final double radius;
    private final ArrayList<Meshable> environmentObjects = new ArrayList<>();
    private final BallPainter painter;
    public TextView textBox;
    static int state = 0;
    boolean stop = false;

    public Ball(String name, double radius, Transform transform) {
        super(name);
        this.radius = radius;
        this.transform = transform;
        this.painter = new BallPainter((float) radius);
    }

    public void addEnvironment(Room room) {
        environmentObjects.add(room.getFrame());
    }

    @Override
    public double getWeight() {
        return this.mass;
    }

    @Override
    public Vector getVectorOfInteractionCollision(Transform transform, Collision collision) {
        return Vector.nullVector();
    }

    @Override
    public boolean hasCollision(Meshable object) {
        if (object instanceof Frame) {
            Vector position = transform.getPosition();
            Frame frame = (Frame) object;
            double distanceFromUpSide = frame.getDistanceFromUpSide(position);
            double distanceFromRightSide = frame.getDistanceFromRightSide(position);
            double distanceFromDownSide = frame.getDistanceFromDownSide(position);
            double distanceFromLeftSide = frame.getDistanceFromLeftSide(position);
            System.out.println(distanceFromDownSide + " % " + distanceFromUpSide + " % " + distanceFromLeftSide + " % " + distanceFromRightSide);
            return distanceFromDownSide < radius || distanceFromUpSide < radius || distanceFromRightSide < radius || distanceFromLeftSide < radius;
        }
        return false;
    }

    private Vector calculateTotalForce() {
        if (state == 0)
            return new Vector(0, 20, 0);
        else if (state == 1)
            return new Vector(20, 0, 0);
        else if (state == 2)
            return new Vector(0, -20, 0);
        return new Vector(-20, 0, 0);
//        return Vector.nullVector();
    }

    @Override
    void update(double deltaTime) {
        Vector velocityChange = calculateTotalForce().multi(deltaTime);
        Vector updatedVelocity = velocityChange.add(this.transform.getVelocity());
        Vector displacement =
//                new Vector(0, 3, 0);
                Vector.add(updatedVelocity, this.transform.getVelocity()).div(2).multi(deltaTime);
        this.transform.move(displacement);
        this.transform.setVelocity(updatedVelocity);
        for (Meshable meshable: environmentObjects)
            if (this.hasCollision(meshable)) {
                Vector interaction = meshable.getVectorOfInteractionCollision(this.transform, Collision.DOWN);
                System.out.println("interaction: " + interaction);
                this.transform.getVelocity().add(interaction);
                this.transform.getVelocity().multi(Math.sqrt(DISSIPATION_COEFFICIENT));
            }
//        System.out.println(this.transform.getPosition());
        painter.draw(this.transform.getPosition());
    }

    public double getRadius() {
        return radius;
    }

    public Transform getTransform() {
        return transform;
    }

    @Override
    public boolean isStopped() {
        return transform.getVelocity().getAbsoluteValue() < Movable.STOPPED_VELOCITY_THRESHOLD;
    }

    @Override
    public void move(Vector position) {
        this.transform.setPosition(position);
    }
}

