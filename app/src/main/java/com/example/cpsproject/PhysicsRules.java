package com.example.cpsproject;

public class PhysicsRules {
    public Vector calculateFreeFallForce(Weighable object, Inclinable inclinedSurface) {
        Vector accelerationGravity = new Vector(0, PhysicsConstants.g, 0);
        double weight = object.getWeight();
        System.out.println("theta: " + inclinedSurface.getTheta());
        return accelerationGravity.multi(weight).rotate2D((float)inclinedSurface.getTheta()).round();
    }

    private double getStaticFrictionConstant(Inclinable inclinedSurface) {
        if (inclinedSurface instanceof FrictionalSurface) {
            return ((FrictionalSurface) inclinedSurface).getStaticFrictionConstant();
        }
        return 0;
    }

    private double getDynamicFrictionConstant(Inclinable inclinedSurface) {
        if (inclinedSurface instanceof FrictionalSurface) {
            return ((FrictionalSurface) inclinedSurface).getDynamicFrictionConstant();
        }
        return 0;
    }

    public double calculateNormalForceOnInclinedSurface(Weighable object, Inclinable inclinedSurface) {
        double theta = inclinedSurface.getTheta();
        double weight = object.getWeight();
        return weight * PhysicsConstants.g * Math.cos(theta);
    }

    private double calculateFrictionForceOnInclinedSurface(double normalForce, double motionForce, double staticFrictionConstant, double dynamicFrictionConstant) {
        double maximumStaticFrictionForce = Math.min(normalForce * staticFrictionConstant, motionForce);
        System.out.println("max is : " + maximumStaticFrictionForce  + " motion force : " + motionForce) ;
        if (maximumStaticFrictionForce < motionForce)
            return normalForce * dynamicFrictionConstant;
        return maximumStaticFrictionForce;
    }

    private double calculateMotionForceOnInclinedSurface(Weighable object, Inclinable inclinedSurface) {
        double weight = object.getWeight();
        double theta = inclinedSurface.getTheta();
        return weight * PhysicsConstants.g * Math.sin(theta);
    }

    public Vector calculateForceOnInclined(Weighable object, Inclinable inclinedSurface, float surfaceGradient) {
        double staticFriction = getStaticFrictionConstant(inclinedSurface);
        double dynamicFriction = getDynamicFrictionConstant(inclinedSurface);
        double normalForce = Math.abs(calculateNormalForceOnInclinedSurface(object, inclinedSurface));
        double motionForce = Math.abs(calculateMotionForceOnInclinedSurface(object, inclinedSurface));
        double frictionForce = Math.abs(calculateFrictionForceOnInclinedSurface(normalForce, motionForce, staticFriction, dynamicFriction));
        double totalForces = Math.max(motionForce - frictionForce , 0);
        System.out.println("total force:" + totalForces + " motion:" + motionForce + " friction:" + frictionForce + " theta:" + inclinedSurface.getTheta());
        if(Math.sin(inclinedSurface.getTheta()) > 0)
            return Vector.fromAbsoluteValueIn2D(totalForces,surfaceGradient).round();
        else
            return Vector.fromAbsoluteValueIn2D(totalForces,surfaceGradient).multi(-1).round();
//        return Vector.fromAbsoluteValueIn2D(totalForces, surfaceGradient * (Math.sin(inclinedSurface.getTheta()) > 0 ? 1: 0));
    }

    public double calculateKineticEnergy(Weighable object) {
        if (object instanceof Movable) {
            Vector velocity = ((Movable) object).getVelocity();
            return 0.5 * Math.pow(velocity.getAbsoluteValue(), 2) * object.getWeight();
        }
        return 0;
    }
}
