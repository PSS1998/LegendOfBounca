package com.example.cpsproject;

public class GameManager {
    private Ball ball;
    private Room room;

    public void start() {
        ball = new Ball("ball", 10, new Transform());
        room = new Room("room", 768, 1024, null);
    }
}
