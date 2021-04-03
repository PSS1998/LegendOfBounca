package com.example.cpsproject;

import android.content.Context;
import android.system.ErrnoException;
import android.widget.TextView;

import java.util.ArrayList;

public class Ball extends GameObject implements Weighable, Meshable, Movable {
    private final static float DISSIPATION_COEFFICIENT = 0.1f;
    private final static float IGNORED_VELOCITY = 0.001f;
    private final static float MOVEMENT_COEFFICIENT = 40f;
    private final double mass = 0.01;
    private final Transform transform;
    private final double radius;
    private final ArrayList<Meshable> environmentObjects = new ArrayList<>();
    private final BallPainter painter;
    private Collision rollingOn;
    private Collision collision;
    private Room room;

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

    public Room getRoom () {
        return room;
    }

    @Override
    public Vector getVelocity() {
        return this.transform.getVelocity();
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
    public void detectCollision(Meshable object) {
        if (object instanceof Frame) {
            Vector position = transform.getPosition();
            Frame frame = (Frame) object;
            double distanceFromUpSide = frame.getDistanceFromUpSide(position);
            double distanceFromRightSide = frame.getDistanceFromRightSide(position);
            double distanceFromDownSide = frame.getDistanceFromDownSide(position);
            double distanceFromLeftSide = frame.getDistanceFromLeftSide(position);
            if (distanceFromDownSide < radius)
                this.collision = Collision.DOWN;
            if (distanceFromUpSide < radius)
                this.collision = Collision.UP;
            if (distanceFromRightSide < radius)
                this.collision = Collision.RIGHT;
            if (distanceFromLeftSide < radius)
                this.collision = Collision.LEFT;
        }
    }

    private float findCollisionSurfaceGradient(Collision collision) {
        switch (collision){
            case UP:
                return (float) -Math.PI;
            case DOWN:
                return (float) Math.PI;
            case LEFT:
                return (float) (Math.PI/2.0);
            case RIGHT:
                return (float) (-Math.PI/2.0);
            default:
                return 0;
        }
    }

    private void detectRolling (Meshable object) {
        this.rollingOn = null;
        if (object instanceof Frame) {
            Vector position = transform.getPosition();
            Frame frame = (Frame) object;
            double distanceFromUpSide = frame.getDistanceFromUpSide(position);
            double distanceFromRightSide = frame.getDistanceFromRightSide(position);
            double distanceFromDownSide = frame.getDistanceFromDownSide(position);
            double distanceFromLeftSide = frame.getDistanceFromLeftSide(position);
            double theta = this.getRoom().getFrame().getTheta();
            if (distanceFromDownSide <= radius && this.getTransform().getVelocity().getY() == 0 && Math.cos(theta) > 0)
                this.rollingOn = Collision.DOWN;
            else if (distanceFromUpSide <= radius && this.getTransform().getVelocity().getY() == 0 && Math.cos(theta + Math.PI) > 0)
                this.rollingOn = Collision.UP;
            else if (distanceFromRightSide <= radius && this.getTransform().getVelocity().getX() == 0 && Math.cos(theta - Math.PI/2) > 0)
                this.rollingOn = Collision.RIGHT;
            else if (distanceFromLeftSide <= radius && this.getTransform().getVelocity().getX() == 0 && Math.cos(theta + Math.PI/2) > 0)
                this.rollingOn = Collision.LEFT;
        }
    }

    private Vector calculateTotalForce() {
        detectRolling(this.room.getFrame());
        if (this.rollingOn != null) {
            float surfaceGradient = findCollisionSurfaceGradient(this.rollingOn);
            return PhysicsRules.calculateForceOnInclined(this, room.getFrame(), surfaceGradient);
        }
        else
            return PhysicsRules.calculateFreeFallForce(this, room.getFrame());
    }

    @Override
    void update(double deltaTime) {
        Vector velocityChange = calculateTotalForce().div(mass).multi(deltaTime);
        Vector updatedVelocity = velocityChange.add(this.transform.getVelocity());
        Vector displacement = Vector.add(updatedVelocity, this.transform.getVelocity()).div(2).multi(deltaTime);
        this.move(displacement);
        this.transform.setVelocity(updatedVelocity);
        for (Meshable meshable: environmentObjects) {
            detectCollision(meshable);
            if (this.collision != null)
                handleCollision(meshable);
        }
        painter.draw(this.transform.getPosition());
    }

    private void setOnTheSurface(Collision collision) {
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

    private void handleCollision(Meshable meshable) {
        setOnTheSurface(this.collision);
        Vector interaction = meshable.getVectorOfInteractionCollision(this.transform, this.collision);
        transform.setVelocity(interaction);
        this.transform.getVelocity().multi(Math.sqrt(1 - DISSIPATION_COEFFICIENT));
        if (PhysicsRules.calculateKineticEnergy(this) < PhysicsConstants.EPSILON_ENERGY)
            this.transform.setVelocity(Vector.nullVector());
        this.collision = null;

    }

    public Transform getTransform() {
        return transform;
    }

    @Override
    public void move(Vector displacement) {
        this.transform.move(displacement.multi(MOVEMENT_COEFFICIENT));
    }
}

