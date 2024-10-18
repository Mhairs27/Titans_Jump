package com.example.titans_jump;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Character {
    private Bitmap bitmap;
    private float x, y;
    private float width, height;
    private float canvasHeight; // Store canvas height

    // Jump and gravity related variables
    private float velocityY = 0;   // Vertical velocity
    private final float GRAVITY = 1;   // Gravity pulling the character down
    private final float JUMP_STRENGTH = -15;   // Initial velocity for a jump
    private boolean isJumping = false;   // Flag to check if the character is in the air

    public Character(Context context) {
        // Load your character image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.character1);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        x = (context.getResources().getDisplayMetrics().widthPixels - width) / 2;  // Center horizontally
        y = (context.getResources().getDisplayMetrics().heightPixels - height) / 2; // Start position (adjust as needed)
    }

    public void draw(Canvas canvas) {
        canvasHeight = canvas.getHeight(); // Update canvas height
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public void update() {
        // Apply gravity to the vertical velocity if jumping or falling
        if (isJumping) {
            velocityY += GRAVITY; // Gravity effect
            y += velocityY; // Update position based on velocity

            // Example: Check if character reaches the bottom of the screen (ground)
            if (y + height > canvasHeight) {
                y = canvasHeight - height; // Stop at ground
                velocityY = 0; // Stop falling
                isJumping = false; // Character has landed
            }
        }
    }

    public void jump() {
        if (!isJumping) {
            velocityY = JUMP_STRENGTH; // Apply upward velocity for jump
            isJumping = true; // Set jumping flag
        }
    }

    // Other methods remain the same
}
