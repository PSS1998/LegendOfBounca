package com.example.cpsproject;

public interface Meshable {
    void detectCollision(Meshable object);
    Vector getVectorOfInteractionCollision(Transform transform, Collision collision);
}
