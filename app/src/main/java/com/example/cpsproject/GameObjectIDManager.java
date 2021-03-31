package com.example.cpsproject;

public class GameObjectIDManager {
    private static int counter = 0;

    static public String generateNewId() {
        return Integer.toString(counter++);
    }
}
