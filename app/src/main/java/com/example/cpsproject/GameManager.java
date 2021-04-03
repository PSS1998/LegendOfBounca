package com.example.cpsproject;

import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;

public class GameManager extends Thread {
    private static long DELTA_TIME = 5;
    private final ArrayList<GameObject> gameObjects = new ArrayList<>();
    public long lastUpdateTime;
    public long firstTime;
    private Ball ball;
    private Room room;

    public void createGameObjects(GameSensorListener sensorListener) {
        float radius = GyroscopeActivity.getBall().getHeight() / 2.0f;
        this.ball = new Ball("ball", radius, new Transform());
        gameObjects.add(ball);
        this.room = new Room("room", GyroscopeActivity.w, GyroscopeActivity.h, GyroscopeActivity.p, sensorListener);
        gameObjects.add(room);
        ball.addEnvironment(room);
        this.setRandomBallPosition();
         GyroscopeActivity.getBall().setVisibility(View.VISIBLE);
    }

    @Override
    public void run() {
        firstTime = System.nanoTime();
        while(true) {
            try {
                long currentTime = System.nanoTime();
                if (lastUpdateTime == 0)
                    lastUpdateTime = currentTime;
                double delta = currentTime - lastUpdateTime;
                if (delta / 1000000 > GameManager.DELTA_TIME) {
                    for (GameObject gameObject : this.gameObjects)
                        gameObject.update(delta / 1000000000.0);
                    lastUpdateTime = currentTime;
                }
                Thread.sleep(DELTA_TIME);
            } catch (Exception e) {
                break;
            }
        }

    }

    public void setRandomBallPosition() {
        Vector position = new Vector();
        Frame frame  = this.room.getFrame();
        position.setX((Math.random() / 3.5 + 0.357) * frame.getWidth());
        position.setY((Math.random() / 3.5 + 0.357) * (frame.getHeight() - frame.getTop()) + frame.getTop());
        this.ball.getTransform().setPosition(position);
        this.ball.getTransform().setVelocity(new Vector(0,0,0));
    }

    public void setRandomBallVelocity() {
        Vector velocity = new Vector();
        velocity.setX(0);
        velocity.setY(-(Math.random() * 10 + 5));
        velocity = velocity.rotate2D((float)ball.getRoom().getFrame().getTheta());
        this.ball.getTransform().setVelocity(velocity);
    }

    public void stopGame() {
        this.gameObjects.clear();
    }
}
