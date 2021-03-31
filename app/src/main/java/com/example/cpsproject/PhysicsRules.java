package com.example.cpsproject;

import android.view.ContextMenu;
import android.view.Surface;

public class PhysicsRules {
    public Vector calculateFreeFallForce(Weighable object) {
        Vector accelerationGravity = new Vector(0, PhysicsConstants.g, 0);
        double weight = object.getWeight();
        return accelerationGravity.multi(weight);
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
        if (maximumStaticFrictionForce < motionForce)
            return normalForce * dynamicFrictionConstant;
        return maximumStaticFrictionForce;
    }

    private double calculateMotionForceOnInclinedSurface(Weighable object, Inclinable inclinedSurface) {
        double weight = object.getWeight();
        double theta = inclinedSurface.getTheta();
        return weight * PhysicsConstants.g * Math.sin(theta);
    }

    public Vector calculateForceOnInclined(Weighable object, Inclinable inclinedSurface) {
        double staticFriction = getStaticFrictionConstant(inclinedSurface);
        double dynamicFriction = getDynamicFrictionConstant(inclinedSurface);
        double normalForce = calculateNormalForceOnInclinedSurface(object, inclinedSurface);
        double motionForce = calculateMotionForceOnInclinedSurface(object, inclinedSurface);
        double frictionForce = calculateFrictionForceOnInclinedSurface(normalForce, motionForce, staticFriction, dynamicFriction);
        double totalForces = motionForce - frictionForce;
        return Vector.fromAbsoluteValueIn2D(totalForces, inclinedSurface.getTheta());
    }
}
