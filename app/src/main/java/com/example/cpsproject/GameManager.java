package com.example.cpsproject;

import android.content.Context;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class GameManager extends Thread {
    private static long DELTA_TIME = 5;
    int counter = 0;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    public TextView textBox;
    public long lastUpdateTime;
    public long firstTime;
    private Ball ball;
    private Room room;
    private View frame;

    public void createGameObjects(TextView viewBox, GameSensorListener sensorListener) {
        this.frame = GyroscopeActivity.getFrame();
        textBox = viewBox;
        float radius = GyroscopeActivity.getBall().getHeight() / 2.0f;
        System.out.println("radius: " + radius);
        this.ball = new Ball("ball", 150, new Transform());
        this.setRandomBallPosition();
        gameObjects.add(ball);
        ball.textBox = textBox;
        this.room = new Room("room", frame.getWidth(), frame.getHeight(), sensorListener);
        room.textBox = viewBox;
        gameObjects.add(room);
        ball.addEnvironment(room);
    }

    @Override
    public void run() {
        firstTime = System.nanoTime();
        String message = new String();
        while(counter < 5000) {
            try {
                long currentTime = System.nanoTime();
                if (lastUpdateTime == 0)
                    lastUpdateTime = currentTime;
                double delta = currentTime - lastUpdateTime;
//                System.out.println(delta);
                if (delta / 1000000 > GameManager.DELTA_TIME) {
                    counter++;
                    for (GameObject gameObject : this.gameObjects)
                        gameObject.update(delta / 1000000000.0);
                    lastUpdateTime = currentTime;
                }
                Thread.sleep(DELTA_TIME);
            } catch (Exception e) {
                message = message.concat("error:\n");
                message = message.concat(e.getMessage());
                break;
            }
        }
        message = message.concat("last time: ");
        message = message.concat(String.valueOf(lastUpdateTime / 1000000));
        message = message.concat("\nfirst time: ");
        message = message.concat(String.valueOf(firstTime / 1000000));
        message = message.concat("\ncounter: " + counter);
        textBox.setText(message);

    }
    public void setRandomBallPosition() {
        Vector position = new Vector();
        position.setX((Math.random() / 3.5 + 0.30) * frame.getWidth());
        position.setY((Math.random() / 3.5 + 0.30) * frame.getHeight());
        this.ball.getTransform().setPosition(position);
        System.out.println("iman");
        System.out.println(this.ball.getTransform().getPosition());
    }
}
