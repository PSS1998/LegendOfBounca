package com.example.cpsproject;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class BallPainter {
    private final float radius;
    private final float scale;
    private final ImageView ball;

    public BallPainter(float radius) {
        this.ball = GyroscopeActivity.getBall();
        this.radius = radius;
        this.scale = this.radius / this.ball.getHeight();
        ball.setScaleX(this.scale);
        ball.setScaleY(this.scale);
        System.out.println("ball width: " + ball.getWidth());
        System.out.println("ball height: " + ball.getHeight());
    }

    public void draw(Vector position) {
        float x = (float) position.getX() - this.radius;
        float y = (float) position.getY() - this.radius;
        this.ball.setX(x);
        this.ball.setY(y);
    }
}
