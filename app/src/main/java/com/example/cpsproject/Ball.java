package com.example.cpsproject;

import android.content.Context;
import android.system.ErrnoException;
import android.widget.TextView;

import java.util.ArrayList;

public class Ball extends GameObject implements Weighable, Meshable, Movable {
    private final static float DISSIPATION_COEFFICIENT = 0;
    private double mass = 1;
    private final Transform transform;
    private final double radius;
    private final ArrayList<Meshable> environmentObjects = new ArrayList<>();
    private final BallPainter painter;
    public TextView textBox;
    static int state = 0;
    boolean stop = false;
    private Room room;
    private PhysicsRules physicsRules = new PhysicsRules();

    public Ball(String name, double radius, Transform transform) {
        super(name);
        this.radius = radius;
        this.transform = transform;
        this.painter = new BallPainter((float) radius);
    }

    public void addEnvironment(Room room) {
        this.room = room;
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
            return (distanceFromDownSide <= radius && this.getTransform().getVelocity().getY() > 0) || (distanceFromUpSide <= radius && this.getTransform().getVelocity().getY() < 0)  ||
                    (distanceFromRightSide <= radius && this.getTransform().getVelocity().getX() > 0) || (distanceFromLeftSide <= radius && this.getTransform().getVelocity().getX() < 0 );
        }
        return false;
    }

    private Collision detectCollision(Meshable object) {
        if (object instanceof Frame) {
            Vector position = transform.getPosition();
            Frame frame = (Frame) object;
            double distanceFromUpSide = frame.getDistanceFromUpSide(position);
            double distanceFromRightSide = frame.getDistanceFromRightSide(position);
            double distanceFromDownSide = frame.getDistanceFromDownSide(position);
            double distanceFromLeftSide = frame.getDistanceFromLeftSide(position);
            if (distanceFromDownSide <= radius)
                return Collision.DOWN;
            if (distanceFromUpSide <= radius)
                return Collision.UP;
            if (distanceFromRightSide <= radius)
                return Collision.RIGHT;
            if (distanceFromLeftSide <= radius)
                return Collision.LEFT;
        }
        return null;
    }

    private Vector calculateTotalForce() {
//        return physicsRules.calculateFreeFallForce(this, room.getFrame());
//        if (state == 0)
//            return new Vector(0, 300, 0).multi(mass);
//        else if (state == 1)
            return new Vector(250, 0, 0);
//        else if (state == 2)
//            return new Vector(0, -200, 0);
//        return new Vector(-300, 0, 0);
//        return Vector.nullVector();
    }

    @Override
    void update(double deltaTime) {
        Vector velocityChange = calculateTotalForce().div(mass).multi(deltaTime);
        Vector updatedVelocity = velocityChange.add(this.transform.getVelocity());
        Vector displacement = Vector.add(updatedVelocity, this.transform.getVelocity()).div(2).multi(deltaTime);
        this.transform.move(displacement);
        this.transform.setVelocity(updatedVelocity);
//        System.out.println("velocity " + this.transform.getVelocity());
        for (Meshable meshable: environmentObjects)
            if (this.hasCollision(meshable)) {
                Collision collision = detectCollision(meshable);
                handleCollision(collision);
                System.out.println("before collision" + this.transform.getVelocity());
                Vector interaction = meshable.getVectorOfInteractionCollision(this.transform, collision);
                transform.setVelocity(interaction);
//                Vector interaction = meshable.getVectorOfInteractionCollision(this.transform, detectCollision(meshable));
//                System.out.println("interaction: " + interaction);
//                this.transform.getVelocity().add(interaction);
                this.transform.getVelocity().multi(Math.sqrt(1 - DISSIPATION_COEFFICIENT));
                System.out.println("after collision" + this.transform.getVelocity());
            }
        painter.draw(this.transform.getPosition());
    }


    private void handleCollision(Collision collision) {
        Vector position = this.transform.getPosition();
        float width = room.getFrame().getWidth();
        float height = room.getFrame().getHeight();
        if (collision == Collision.DOWN)
            position.setY(height - this.radius);
        if (collision == Collision.UP)
            position.setY(this.radius + GyroscopeActivity.p);
        if (collision == Collision.RIGHT)
            position.setX(width - this.radius);
        if (collision == Collision.LEFT)
            position.setX(this.radius);
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

    public void setRandomPosition() {

    }
}

