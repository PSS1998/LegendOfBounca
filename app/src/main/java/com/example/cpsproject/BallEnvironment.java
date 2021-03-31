//package com.example.cpsproject;
//
//public class BallEnvironment {
//
//    private Transform transform;
//    private float gradient;
//    private float mass;
//    private static final double gravity = 9.8;
//    private static final double staticalFrictionConstant = 0.15;
//    private static final double dynamicFrictionConstant = 0.07;
//    private static float timePeriod = 100;
//    private float width;
//    private float length;
//
//    public BallEnvironment (Transform transform, float gradient, float mass, float width, float length) {
//        this.transform = transform;
//        this.gradient = gradient;
//        this.mass = mass;
//        this.width = width;
//        this.length = length;
//
//    }
//
////    public Vector calculateGravity () {
////        return new Vector(0 , (float) (- mass * gravity) );
////    }
////
////    public Vector calculateNormalForce () {
////        Vector forces = transform.getVelocity();
////
////        return new Vector(- mass * (float)( Math.cos(gradient) * Math.sin(gradient) * gravity), mass * (float) (Math.cos(gradient) * Math.cos(gradient) * gravity));
////    }
////
////    public Vector calculateStaticalFrictionAbs () {
////        return new Vector(mass * (float)( Math.cos(gradient) * Math.cos(gradient) * gravity * staticalFrictionConstant), mass * (float) (Math.cos(gradient) * Math.sin(gradient) * gravity * staticalFrictionConstant));
////    }
////
////    public Vector calculateDynamicFrictionAbs () {
////        return new Vector(mass * (float)( Math.cos(gradient) * Math.cos(gradient) * gravity * DynamicFrictionConstant), mass * (float) (Math.cos(gradient) * Math.sin(gradient) * gravity * DynamicFrictionConstant));
////    }
//
//
//    public Vector calculateGravity () {
//        return new Vector((float) ( mass * gravity * Math.cos(gradient)) , (float) (- mass * gravity * Math.sin(gradient)));
//    }
//
//    public Vector calculateNormalForce () {
//        return new Vector((float) ( -mass * gravity * Math.cos(gradient)), 0);
//    }
//
//    public Vector calculateStaticalFrictionAbs () {
//        return new Vector(0, mass * (float) (Math.cos(gradient)  * gravity * staticalFrictionConstant));
//    }
//
//    public Vector calculateDynamicFrictionAbs () {
//        return new Vector(0, mass * (float) (Math.cos(gradient)  * gravity * dynamicFrictionConstant));
//    }
//
//    public Transform updateBallTransform () {
//        Vector resultantForce = calculateResultantForce ();
//        Vector acceleration = calculateAcceleration(resultantForce);
//        Vector velocity = calculateVelocity(acceleration);
//        Vector position = calculatePosition(acceleration);
//
//        transform = new Transform(position, velocity, acceleration);
//        return transform;
//    }
//
//    public Vector calculateResultantForce () {
//
//        if (!hasCollision() && !isRolling()) {
//            return calculateGravity();
//        }
//        else if (hasCollision()) {
//            return calculateForceAfterCollision();
//        }
//
//        else if (isRolling()) {
//            if (isStopped())
//                return new Vector(Math.max(0, calculateGravity().getX() + calculateNormalForce().getX() - calculateStaticalFrictionAbs().getX()), Math.max(0, calculateGravity().getY() + calculateNormalForce().getY() + calculateStaticalFrictionAbs().getY()));
//            else
//                return new Vector(calculateGravity().getX() + calculateNormalForce().getX() - calculateDynamicFrictionAbs().getX(),  calculateGravity().getY() + calculateNormalForce().getY() - calculateDynamicFrictionAbs().getY());
//        }
//        return null;
//
//
//
//    }
//
//
//
//    public Vector calculateAcceleration (Vector force) {
//        return new Vector(force.getX()/mass, force.getY()/mass );
//    }
//
//    public Vector calculateVelocity (Vector acceleration) {
//        Vector currentVelocity = transform.getVelocity();
//        return new Vector(acceleration.getX() * timePeriod + currentVelocity.getX() , acceleration.getY() * timePeriod + currentVelocity.getY());
//    }
//
//    public Vector calculatePosition (Vector acceleration) {
//        Vector currentVelocity = transform.getVelocity();
//        Vector currentPosition = transform.getPosition();
//        return new Vector(0.5f * acceleration.getX() * (float)Math.pow(timePeriod, 2) + currentVelocity.getX() * timePeriod + currentPosition.getX(),0.5f * acceleration.getY() * (float)Math.pow(timePeriod, 2) + currentVelocity.getY() * timePeriod + currentPosition.getY());
//    }
//
//
//    public Vector calculateForceAfterCollision () {
//        return ...
//    }
//
//
//    public boolean hasCollision () {
//
//        Vector position = transform.getPosition();
//        Vector velocity = transform.getVelocity();
//        if (Math.abs(position.getX()) >= width || Math.abs(position.getY()) >= length){
//            return velocity.getX() != 0 && velocity.getY() != 0;
//        }
//        return false;
//
//    }
//
//    public boolean isStopped() {
//        Vector velocity = transform.getVelocity();
//        return velocity.getX() == 0 && velocity.getY() == 0;
//    }
//
//
//
//
//    public boolean isRolling () {
//        Vector position = transform.getPosition();
//        Vector velocity = transform.getVelocity();
//        if (Math.abs(position.getX()) >= width || Math.abs(position.getY()) >= length){
//            return velocity.getX() == 0 || velocity.getY() == 0;
//        }
//        return false;
//    }
//
//
//
//}