package com.example.cpsproject;

import android.content.Context;
import android.view.ContextMenu;
import android.widget.TextView;

import java.util.ArrayList;

public class GameManager extends Thread {
    private static long DELTA_TIME = 1000;
    int counter = 0;
    int parallelCounter = 0;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    public TextView textBox;
    public long lastUpdateTime;
    public long firstTime;
    private final Context context;

    public GameManager(Context context) {
        this.context = context;
    }

    public void createGameObjects(TextView viewBox, GameSensorListener sensorListener) {
        textBox = viewBox;
        Vector position = new Vector(100, 100, 0);
        Ball ball = new Ball(context, "ball", 300, new Transform(position, Vector.nullVector()));
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
        String message = new String();
        while(counter < 4) {
            try {
                synchronized (this) {
                    parallelCounter++;
                    long currentTime = System.nanoTime();
                    if (lastUpdateTime == 0)
                        lastUpdateTime = currentTime;
                    if ((currentTime - lastUpdateTime) / 1000000 < GameManager.DELTA_TIME)
                        Thread.sleep((currentTime - lastUpdateTime) / 1000000);
                    else {
                        counter++;
                        double delta = 0.1;
                        for (GameObject gameObject : this.gameObjects)
                            gameObject.update(delta);
                        lastUpdateTime = currentTime;
                    }
                }
            } catch (Exception e) {
                message = message.concat(e.getMessage());
                message = message.concat("error\n");
                break;
//                break;
//                textBox.setText(e.getMessage());
            }
        }
        message = message.concat("last time: ");
        message = message.concat(String.valueOf(lastUpdateTime / 1000000));
        message = message.concat("\nfirst time: ");
        message = message.concat(String.valueOf(firstTime / 1000000));
        message = message.concat("\nparallelCounter: " + parallelCounter);
        textBox.setText(message);

    }
}
