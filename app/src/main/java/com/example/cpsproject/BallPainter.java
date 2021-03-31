package com.example.cpsproject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BallPainter {
    private int color = Color.BLUE;
    private final double radius;
    private final Paint paint;
    private final Canvas canvas;

    public BallPainter(double radius) {
        this.radius = radius;
        paint = new Paint();
        paint.setColor(color);
        canvas = new Canvas();
    }

    public void draw(Vector position) {
        float x = (float) position.getX();
        float y = (float) position.getY();
        this.canvas.drawCircle((float)x, (float)y, (float)this.radius, paint);
    }
}
