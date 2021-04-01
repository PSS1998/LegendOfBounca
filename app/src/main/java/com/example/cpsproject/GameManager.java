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
    private final Context context;

    public GameManager(Context context) {
        this.context = context;
    }

    public void createGameObjects(TextView viewBox, GameSensorListener sensorListener) {
        View frame = GyroscopeActivity.getFrame();
        textBox = viewBox;
        Vector position = new Vector();
        position.setX((Math.random() / 2.5 + 0.20) * frame.getWidth());
        position.setY((Math.random() / 2.5 + 0.20) * frame.getHeight());
//        System.out.println(position);
        Ball ball = new Ball("ball", 200, new Transform(position, Vector.nullVector()));
        gameObjects.add(ball);
        ball.textBox = textBox;
        Room room = new Room("room", frame.getWidth(), frame.getHeight(), sensorListener);
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
}
