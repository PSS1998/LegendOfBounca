package com.example.cpsproject;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class BallPainter extends View {
    private ImageView ball;

    public BallPainter(Context context) {
        super(context);
        ball = GyroscopeActivity.getBall();
    }

    public void draw(Vector position) {
        System.out.println(ball.getX() + " " + ball.getY());
        ball.setX((float) position.getX());
        ball.setY((float) position.getY());
    }
}
