package com.developer.anishakd4.duckhunt.GamingObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.developer.anishakd4.duckhunt.GameUtils.GameObject;
import com.developer.anishakd4.duckhunt.R;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.Utilities;
import com.developer.anishakd4.duckhunt.Utils.Vector2D;

public class IndicatorShots extends GameObject {

    private int numShots;
    private Bitmap bmpShot, bmpBullet;
    private Paint paint;
    private Vector2D[] bulletPositions;
    private int spacing = 30;

    public IndicatorShots(Context context) {

        numShots = 3;

        populateSprites(context);

        paint = new Paint();

        position = new Vector2D((float)(0.11 * Utilities.SCREEN_WIDTH), (float)(0.95 * Utilities.SCREEN_HEIGHT));
        bulletPositions = new Vector2D[3];
        bulletPositions[0] = new Vector2D((float)(0.1 * Utilities.SCREEN_WIDTH), (float)(0.92 * Utilities.SCREEN_HEIGHT));
        bulletPositions[1] = new Vector2D((float)(0.1 * Utilities.SCREEN_WIDTH) + spacing, (float)(0.92 * Utilities.SCREEN_HEIGHT));
        bulletPositions[2] = new Vector2D((float)(0.1 * Utilities.SCREEN_WIDTH) + spacing + spacing, (float)(0.92 * Utilities.SCREEN_HEIGHT));

        layer = GameConstants.FOREGROUND;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bmpShot, position.x, position.y, paint);
        if (numShots <= 0) return;

        canvas.drawBitmap(bmpBullet, bulletPositions[0].x, bulletPositions[0].y, paint);
        if (numShots <= 1) return;

        canvas.drawBitmap(bmpBullet, bulletPositions[1].x, bulletPositions[1].y, paint);
        if (numShots <= 2) return;

        canvas.drawBitmap(bmpBullet, bulletPositions[2].x, bulletPositions[2].y, paint);
    }

    @Override
    public void onUpdate() {

    }

    private void populateSprites(Context context) {
        bmpShot = BitmapFactory.decodeResource(context.getResources(), R.drawable.shot);
        bmpBullet = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
    }

    public boolean shoot() {
        numShots--;
        return numShots <= 0;
    }

    public void setNumShots(int numShots) {
        this.numShots = numShots;
    }
}
