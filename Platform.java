package com.example.titans_jump;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Platform {
    private float x, y;
    private Bitmap bitmap;
    private boolean hasObstacle;

    public Platform(float x, float y, Context context, int drawableRes) {
        this.x = x;
        this.y = y;
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), drawableRes);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public boolean hasObstacle() {
        return hasObstacle;
    }

    public void setObstacle(boolean hasObstacle) {
        this.hasObstacle = hasObstacle;
    }
}
