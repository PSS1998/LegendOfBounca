package com.example.cpsproject;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

public class Ball extends GameObject implements Weighable, Meshable, Movable {
    private double mass;
    private final Transform transform;
    private final double radius;
    private final ArrayList<Meshable> environmentObjects = new ArrayList<>();
    private final BallPainter painter;
    public TextView textBox;

    public Ball(Context context, String name, double radius, Transform transform) {
        super(name);
        this.radius = radius;
        this.transform = transform;
        this.painter = new BallPainter(context, this);
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
        return false;
//        if (object instanceof Room) {
//            Vector position = transform.getPosition();
//            Frame frame = ((Room) object).getFrame();
//            double distanceFromUpSide = frame.getDistanceFromUpSide(position);
//            double distanceFromRightSide = frame.getDistanceFromRightSide(position);
//            double distanceFromDownSide = frame.getDistanceFromDownSide(position);
//            double distanceFromLeftSide = frame.getDistanceFromLeftSide(position);
//            return distanceFromDownSide < radius || distanceFromUpSide < radius || distanceFromRightSide < radius || distanceFromLeftSide < radius;
//        }
//        return false;
    }

    private Vector calculateTotalForce() {
        return Vector.nullVector();
    }

    @Override
    void update(double deltaTime) {
        Vector velocityChange = calculateTotalForce().multi(deltaTime);
        Vector updatedVelocity = velocityChange.add(this.transform.getVelocity());
        Vector displacement = Vector.add(updatedVelocity, this.transform.getVelocity()).div(2).multi(deltaTime);
        this.transform.move(displacement);
        this.transform.setVelocity(updatedVelocity);
        for (Meshable meshable: environmentObjects)
            if (this.hasCollision(meshable)) {
                Vector interaction = meshable.getVectorOfInteractionCollision(this.transform, Collision.DOWN);
                this.transform.getVelocity().add(interaction);
            }
        painter.invalidate();
        painter.draw();
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

