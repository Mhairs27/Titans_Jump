package com.example.titans_jump;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends View {
    private Paint paint;
    private float score = 0, timer = 30;
    private List<Platform> platforms;
    private List<Cloud> clouds;
    private List<Heart> hearts;
    private Random random = new Random();
    private int screenHeight, screenWidth;
    private boolean gameRunning = true;
    private Bitmap background;
    private Bitmap characterBitmap;
    private Rect characterRect;
    private final float BOUNCE_STRENGTH = -10;
    private float characterVelocityY = 0;
    private boolean isJumping = false;
    private final float GRAVITY = 1, JUMP_STRENGTH = -20;
    private int bestScore = 0;
    private Handler handler = new Handler();
    private Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            updateGame();
            handler.postDelayed(this, 16); // 60 FPS
        }
    };
    private float scrollSpeed = 5f, scrollOffset = 0f;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private void init() {
        paint = new Paint();
        platforms = new ArrayList<>();
        clouds = new ArrayList<>();
        hearts = new ArrayList<>();
        background = BitmapFactory.decodeResource(getResources(), R.drawable.game_bg);
        characterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.character1);
        generatePlatformsAndClouds();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        if (screenWidth > 0 && screenHeight > 0) {
            generatePlatformsAndClouds();
            characterRect = new Rect(getWidth() / 2 - 100, getHeight() - 250, getWidth() / 2 + 100, getHeight() - 50);
        }
    }

    private void generatePlatformsAndClouds() {
        int numRows = 6;
        float rowSpacing = screenHeight / numRows;
        platforms.clear();
        clouds.clear();
        hearts.clear();

        // Define how many platforms per row and calculate spacing
        int platformsPerRow = 3;
        float platformSpacing = screenWidth / (platformsPerRow + 1); // +1 to add space on both sides

        for (int row = 0; row < numRows; row++) {
            float rowY = rowSpacing * row + 100;
            int greenPlatformColumn = random.nextInt(platformsPerRow);

            for (int column = 0; column < platformsPerRow; column++) {
                float platformX = platformSpacing * (column + 1); // Add 1 to column for spacing

                if (column == greenPlatformColumn) {
                    Platform platform = new Platform(platformX, rowY, getContext(), R.drawable.p_green);
                    if (random.nextBoolean()) platform.setObstacle(true);
                    platforms.add(platform);
                } else {
                    clouds.add(new Cloud(platformX, rowY - 40, getContext(), R.drawable.p_white));
                }
            }

            // Add hearts above the green platform randomly
            if (random.nextBoolean()) {
                hearts.add(new Heart(platformSpacing * (greenPlatformColumn + 1) + 20, rowY - 100, getContext()));
            }
        }
    }

    private void updateGameLogic() {
        timer -= 0.016f;
        if (timer <= 0) gameRunning = false;
        if (isJumping) {
            characterVelocityY += GRAVITY;
            characterRect.offset(0, Math.round(characterVelocityY));
            boolean landed = false;
            for (Platform platform : platforms) {
                if (characterRect.bottom >= platform.getY() && characterRect.bottom <= platform.getY() + 20 &&
                        characterRect.left < platform.getX() + platform.getWidth() &&
                        characterRect.right > platform.getX()) {
                    characterRect.offsetTo(characterRect.left, Math.round(platform.getY() - characterRect.height()));
                    characterVelocityY = BOUNCE_STRENGTH;
                    isJumping = false;
                    landed = true;
                    if (platform.hasObstacle()) gameRunning = false;
                    break;
                }
            }
            if (!landed) {
                scrollPlatforms();
                score += scrollSpeed / 10;
                if (characterRect.top > screenHeight) gameRunning = false;
            }
        }
    }

    private void scrollPlatforms() {
        for (Platform platform : platforms) {
            platform.setY(platform.getY() + scrollSpeed);
            if (platform.getY() > screenHeight) {
                platform.setY(-platform.getHeight());
                platform.setX(random.nextInt(screenWidth - platform.getWidth()));
                platform.setObstacle(random.nextBoolean());
            }
        }
        for (Cloud cloud : clouds) {
            cloud.setY(cloud.getY() + scrollSpeed);
            if (cloud.getY() > screenHeight) {
                cloud.setY(-cloud.getHeight());
                cloud.setX(random.nextInt(screenWidth - cloud.getWidth()));
            }
        }
        for (Heart heart : hearts) {
            heart.setY(heart.getY() + scrollSpeed);
            if (heart.getY() > screenHeight) {
                heart.setY(-heart.getHeight());
                heart.setX(random.nextInt(screenWidth - heart.getWidth()));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the background
        canvas.drawBitmap(background, 0, 0, null);

        // Draw the clouds
        for (Cloud cloud : clouds) {
            cloud.draw(canvas);
        }

        // Draw the platforms
        for (Platform platform : platforms) {
            platform.draw(canvas);
        }

        // Draw the hearts (collectibles) with resized bitmaps
        for (Heart heart : hearts) {
            // Resize the heart bitmap before drawing it
            Bitmap scaledHeartBitmap = Bitmap.createScaledBitmap(heart.getBitmap(), 50, 50, false);
            canvas.drawBitmap(scaledHeartBitmap, heart.getX(), heart.getY(), null);
        }

        // Draw the character
        canvas.drawBitmap(characterBitmap, null, characterRect, null);

        // Draw the best score
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("Best Score: " + bestScore, 50, 50, paint); // Display best score

        // Draw the current score
        canvas.drawText("Score: " + Math.round(score), 50, 100, paint); // Display current score

        // Draw the timer progress bar
        drawTimerProgress(canvas);
    }

    private void drawTimerProgress(Canvas canvas) {
        float progressBarWidth = screenWidth, progressBarHeight = 50, progress = (timer / 30) * progressBarWidth;
        paint.setColor(Color.GRAY);
        canvas.drawRect(0, screenHeight - progressBarHeight, progressBarWidth, screenHeight, paint);
        paint.setColor(timer > 0 ? Color.GREEN : Color.RED);
        canvas.drawRect(0, screenHeight - progressBarHeight, progress, screenHeight, paint);
    }

    public void updateGame() {
        if (gameRunning) {
            updateGameLogic();
            invalidate();
        } else {
            stopGame();
        }
    }

    public void startGame() {
        gameRunning = true;
        handler.post(gameLoop);
    }

    public void stopGame() {
        handler.removeCallbacks(gameLoop);
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public int getScore() {
        return Math.round(score);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isJumping) {
                // Increment the score for every jump
                score += 1;  // Increase score by 1 point

                characterVelocityY = JUMP_STRENGTH;
                isJumping = true;

                // Get the nearest platform above
                Platform nearestPlatform = getNearestPlatformAbove();
                if (nearestPlatform != null) {
                    // Move the character to the nearest platform
                    characterRect.offsetTo((int) nearestPlatform.getX(), Math.round(nearestPlatform.getY() - characterRect.height()));

                    // Stop jumping once the character reaches the platform
                    characterVelocityY = 0;  // Stop vertical movement
                    isJumping = false;       // Reset the jumping flag
                }
            }
            invalidate();
        }
        return true;
    }

    private Platform getNearestPlatformAbove() {
        Platform closestPlatformAbove = null;
        float closestDistance = Float.MAX_VALUE;

        for (Platform platform : platforms) {
            if (platform.getY() < characterRect.top) {
                float distance = characterRect.top - platform.getY();
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestPlatformAbove = platform;
                }
            }
        }
        return closestPlatformAbove;
    }
}
