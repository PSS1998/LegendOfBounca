package com.example.cpsproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class BallPainter extends View {
    private final int color = Color.BLUE;
    private final Paint paint;
    private final RectF ballBounds;
    private final Ball ball;
    private final Canvas canvas;

    public BallPainter(Context context, Ball ball) {
        super(context);
        this.ball = ball;
        paint = new Paint();
        paint.setColor(color);
        canvas = new Canvas();
        ballBounds = new RectF();
        setBackgroundColor(Color.YELLOW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Vector position = ball.getTransform().getPosition();
        float x = (float) position.getX();
        float y = (float) position.getY();
        float radius = (float) ball.getRadius();
        ballBounds.set(x - radius, y - radius, x + radius, y + radius);
        paint.setColor(color);
        canvas.drawOval(ballBounds, paint);
    }

    public void draw() {
        Vector position = ball.getTransform().getPosition();
        float x = (float) position.getX();
        float y = (float) position.getY();
        float radius = (float) ball.getRadius();
        ballBounds.set(x - radius, y - radius, x + radius, y + radius);
        paint.setColor(color);
        canvas.drawOval(ballBounds, paint);
    }
}
