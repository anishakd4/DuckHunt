package com.developer.anishakd4.duckhunt.GamingObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.developer.anishakd4.duckhunt.GameUtils.GameObject;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.Utilities;
import com.developer.anishakd4.duckhunt.Utils.Vector2D;

public class IndicatorScore extends GameObject {

    private int score;
    private Paint paint;

    public IndicatorScore(int score) {
        this.score = score;

        position = new Vector2D((float)(0.75 * Utilities.SCREEN_WIDTH), (float)(0.93 * Utilities.SCREEN_HEIGHT));

        layer = GameConstants.FOREGROUND;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawText(Integer.toString(score), position.x, position.y, paint);
        canvas.drawText("SCORE", position.x, position.y + 60, paint);
    }

    @Override
    public void onUpdate() {

    }

    public void setScore(int newScore) {
        score = newScore;
    }

    public int getScore() {
        return score;
    }

    public void addToScore(int points) {
        score += points;
    }
}
