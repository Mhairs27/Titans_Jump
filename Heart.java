package com.example.titans_jump;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Heart {
    private float x, y;
    private Bitmap bitmap;

    public Heart(float x, float y, Context context) {
        this.x = x;
        this.y = y;
        // Load the heart bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart); // Use your heart image resource
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getWidth() {
        return bitmap.getWidth(); // Get width of the bitmap
    }

    public int getHeight() {
        return bitmap.getHeight(); // Get height of the bitmap
    }
}
