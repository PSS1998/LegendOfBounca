package com.example.cpsproject;

import android.widget.TextView;

import java.util.ArrayList;

public class GameManager implements Runnable {
    private static long DELTA_TIME = 1000;
    int counter = 0;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    public TextView textBox;
    public long lastUpdateTime;
    public long firstTime;

    public void createGameObjects(TextView viewBox, GameSensorListener sensorListener) {
        textBox = viewBox;
        Vector position = new Vector(100, 100, 0);
        Ball ball = new Ball("ball", 10, new Transform(position, Vector.nullVector()));
        gameObjects.add(ball);
        ball.textBox = textBox;
        Room room = new Room("room", 768, 1024, sensorListener);
        room.textBox = viewBox;
        gameObjects.add(room);
        ball.addEnvironment(room);
    }

    @Override
    public void run() {
        firstTime = System.nanoTime();
        while(counter < 10) {
            try {
                synchronized (this) {
                    long currentTime = System.nanoTime();
                    if (lastUpdateTime == 0)
                        lastUpdateTime = currentTime;
                    if ((currentTime - lastUpdateTime) / 1000000 < GameManager.DELTA_TIME)
                        Thread.sleep((currentTime - lastUpdateTime) / 1000000);
                    else {
                        double delta = 0.1;
                        for (GameObject gameObject : this.gameObjects)
                            gameObject.update(delta);
                        counter++;
                        lastUpdateTime = currentTime;
                    }
                }
            } catch (Exception e) {
                break;
//                textBox.setText(e.getMessage());
            }
        }
        String message = new String();
//        message.concat(String.valueOf(new Date()));
        message = message.concat("#what the hell#");
        message = message.concat(String.valueOf(lastUpdateTime));
        message = message.concat("$");
        message = message.concat(String.valueOf(firstTime));
        message = message.concat("$");
        textBox.setText(message);

    }
}
