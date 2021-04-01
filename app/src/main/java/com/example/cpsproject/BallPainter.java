package com.example.cpsproject;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class BallPainter {
    private final ImageView ball;

    public BallPainter() {
        ball = GyroscopeActivity.getBall();
    }

    public void draw(Vector position) {
        ball.setX((float) position.getX());
        ball.setY((float) position.getY());
    }
}
