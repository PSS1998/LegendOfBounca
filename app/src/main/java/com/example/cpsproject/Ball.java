package com.example.cpsproject;

import android.content.Context;
import android.os.Trace;
import android.system.ErrnoException;
import android.widget.TextView;

import java.util.ArrayList;

public class Ball extends GameObject implements Weighable, Meshable, Movable {
    private final static float DISSIPATION_COEFFICIENT = 0.1f;
    private final static float EPSILON_COEFFICIENT = 10.0f;
    private final static float IGNORED_VELOCITY = 0.001f;
    private double mass = 0.01;
    private final Transform transform;
    private final double radius;
    private final ArrayList<Meshable> environmentObjects = new ArrayList<>();
    private final BallPainter painter;
    public TextView textBox;
    private Collision rollingOn;
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
    public boolean hasCollision(Meshable object) {
        if (object instanceof Frame) {
            Vector position = transform.getPosition();
            Frame frame = (Frame) object;
            double distanceFromUpSide = frame.getDistanceFromUpSide(position);
            double distanceFromRightSide = frame.getDistanceFromRightSide(position);
            double distanceFromDownSide = frame.getDistanceFromDownSide(position);
            double distanceFromLeftSide = frame.getDistanceFromLeftSide(position);

            System.out.println(distanceFromDownSide + " % " + distanceFromUpSide + " % " + distanceFromLeftSide + " % " + distanceFromRightSide);
            return (distanceFromDownSide <= radius  && this.getTransform().getVelocity().getY() > IGNORED_VELOCITY) ||
                    (distanceFromUpSide <= radius && this.getTransform().getVelocity().getY() < -IGNORED_VELOCITY)  ||
                    (distanceFromRightSide <= radius && this.getTransform().getVelocity().getX() > IGNORED_VELOCITY) ||
                    (distanceFromLeftSide <= radius && this.getTransform().getVelocity().getX() < -IGNORED_VELOCITY);
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
            if (distanceFromDownSide < radius)
                return Collision.DOWN;
            if (distanceFromUpSide < radius)
                return Collision.UP;
            if (distanceFromRightSide < radius)
                return Collision.RIGHT;
            if (distanceFromLeftSide < radius)
                return Collision.LEFT;
        }
        return null;
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

    private boolean isRolling (Meshable object) {
        if (object instanceof Frame) {
            Vector position = transform.getPosition();
            Frame frame = (Frame) object;
            double distanceFromUpSide = frame.getDistanceFromUpSide(position);
            double distanceFromRightSide = frame.getDistanceFromRightSide(position);
            double distanceFromDownSide = frame.getDistanceFromDownSide(position);
            double distanceFromLeftSide = frame.getDistanceFromLeftSide(position);
            double theta = this.getRoom().getFrame().getTheta();
            System.out.println(distanceFromDownSide + " % " + distanceFromUpSide + " % " + distanceFromLeftSide + " % " + distanceFromRightSide);
            return (distanceFromDownSide <= radius && Math.abs(this.getTransform().getVelocity().getY()) < EPSILON_COEFFICIENT && Math.cos(theta) > 0 ) ||
                    (distanceFromUpSide <= radius && Math.abs(this.getTransform().getVelocity().getY()) < EPSILON_COEFFICIENT && Math.cos(theta + Math.PI) > 0) ||
                    (distanceFromRightSide <= radius && Math.abs(this.getTransform().getVelocity().getX()) < EPSILON_COEFFICIENT && Math.cos(theta + Math.PI/2) > 0) ||
                    (distanceFromLeftSide <= radius && Math.abs(this.getTransform().getVelocity().getX()) < EPSILON_COEFFICIENT && Math.cos(theta - Math.PI/2) > 0);
        }
        return false;
    }

    private void detectRolling (Meshable object) {
        if (object instanceof Frame) {
            Vector position = transform.getPosition();
            Frame frame = (Frame) object;
            double distanceFromUpSide = frame.getDistanceFromUpSide(position);
            double distanceFromRightSide = frame.getDistanceFromRightSide(position);
            double distanceFromDownSide = frame.getDistanceFromDownSide(position);
            double distanceFromLeftSide = frame.getDistanceFromLeftSide(position);
            System.out.println(distanceFromDownSide + " % " + distanceFromUpSide + " % " + distanceFromLeftSide + " % " + distanceFromRightSide);
            double theta = this.getRoom().getFrame().getTheta();
            if (distanceFromDownSide <= radius && this.getTransform().getVelocity().getY() == 0 && Math.cos(theta) > 0)
                this.rollingOn = Collision.DOWN;
            else if (distanceFromUpSide <= radius && this.getTransform().getVelocity().getY() == 0 && Math.cos(theta + Math.PI) > 0)
                this.rollingOn = Collision.UP;
            else if (distanceFromRightSide <= radius && this.getTransform().getVelocity().getX() == 0 && Math.cos(theta + Math.PI/2) > 0)
                this.rollingOn = Collision.RIGHT;
            else if (distanceFromLeftSide <= radius && this.getTransform().getVelocity().getX() == 0 && Math.cos(theta - Math.PI/2) > 0)
                this.rollingOn = Collision.LEFT;
        }
    }

    private Vector calculateTotalForce() {
        detectRolling(this.room.getFrame());
        if (rollingOn != null) {
            float surfaceGradient = findCollisionSurfaceGradient(this.rollingOn);
            this.rollingOn = null;
            return physicsRules.calculateForceOnInclined(this, room.getFrame(), surfaceGradient);
        }
        else
            return physicsRules.calculateFreeFallForce(this, room.getFrame());
    }

    @Override
    void update(double deltaTime) {
        Vector velocityChange = calculateTotalForce().div(mass).multi(deltaTime);
        Vector updatedVelocity = velocityChange.add(this.transform.getVelocity());
        Vector displacement = Vector.add(updatedVelocity, this.transform.getVelocity()).div(2).multi(deltaTime);
        this.transform.move(displacement);
        this.transform.setVelocity(updatedVelocity);
        System.out.println("velocityy" + this.transform.getVelocity());
        for (Meshable meshable: environmentObjects)
            if (this.hasCollision(meshable)) {
                Collision collision = detectCollision(meshable);
                System.out.println("collision name: " + collision.name());
                handleCollision(collision);
                System.out.println("before collision" + this.transform.getVelocity());
                Vector beforeVelocity = this.getTransform().getVelocity();
                Vector interaction = meshable.getVectorOfInteractionCollision(this.transform, collision);
                transform.setVelocity(interaction);

                this.transform.getVelocity().multi(Math.sqrt(1 - DISSIPATION_COEFFICIENT));
                if (physicsRules.calculateKineticEnergy(this) < EPSILON_COEFFICIENT)
                    this.transform.setVelocity(Vector.nullVector());
                System.out.println("after collision" + this.transform.getVelocity() + "value:"  + (Math.pow(beforeVelocity.getAbsoluteValue(),2) - Math.pow(this.transform.getVelocity().getAbsoluteValue(),2))* mass);
            }
        this.rollingOn = null;
        Trace.beginSection("move-ball");
        painter.draw(this.transform.getPosition());
        Trace.endSection();
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

