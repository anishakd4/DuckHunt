package com.developer.anishakd4.duckhunt.GamingObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.developer.anishakd4.duckhunt.GameUtils.GameObject;
import com.developer.anishakd4.duckhunt.R;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.Utilities;
import com.developer.anishakd4.duckhunt.Utils.Vector2D;

public class UserIndicator extends GameObject {

    private int round;
    private Paint paint;
    private boolean isGameOver = false;
    private boolean nextRound = false;
    private Bitmap gameOver;
    private int frame = 0;

    public UserIndicator(int level, Context context) {
        setLevel(level);

        layer = GameConstants.FOREGROUND;

        position = new Vector2D((float) (0.18 * Utilities.SCREEN_WIDTH), (float) ((1 - 0.14) * Utilities.SCREEN_HEIGHT));

        gameOver = BitmapFactory.decodeResource(context.getResources(), R.drawable.game_over);
        gameOver = Bitmap.createScaledBitmap(gameOver, gameOver.getWidth() / 2, gameOver.getHeight() / 2, false);

        paint = new Paint();
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.RIGHT);
    }

    public void gameOver() {
        isGameOver = true;
    }

    public void nextRound() {
        nextRound = true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        paint.setColor(Color.GREEN);
        canvas.drawText("R=" + Integer.toString(round), position.x, position.y, paint);
        if (frame < 100) {
            paint.setColor(Color.BLACK);
            canvas.drawText("Round " + Integer.toString(round), (float) (0.6 * Utilities.SCREEN_WIDTH),
                    (float) (0.3 * Utilities.SCREEN_HEIGHT), paint);
        }
        if (isGameOver) {
            canvas.drawBitmap(gameOver, Utilities.SCREEN_WIDTH / 2 - gameOver.getWidth() / 2, (float) (0.4 * Utilities.SCREEN_HEIGHT), paint);
        }
        paint.setColor(Color.BLACK);
        if (nextRound) {
            canvas.drawText("Round " + Integer.toString(round), (float) (0.6 * Utilities.SCREEN_WIDTH), (float) (0.45 * Utilities.SCREEN_HEIGHT), paint);
            canvas.drawText("Complete!", (float) (0.63 * Utilities.SCREEN_WIDTH), (float) (0.5 * Utilities.SCREEN_HEIGHT), paint);
        }
        frame++;
    }

    @Override
    public void onUpdate() {

    }

    private void setLevel(int level) {
        if (level >= 0) {
            this.round = level;
        } else {
            this.round = 0;
        }
    }
}
