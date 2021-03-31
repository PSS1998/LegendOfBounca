package com.example.cpsproject;

public interface Meshable {
    boolean hasCollision(Meshable object);
    Vector getVectorOfInteractionCollision(Transform transform, Collision collision);
}
