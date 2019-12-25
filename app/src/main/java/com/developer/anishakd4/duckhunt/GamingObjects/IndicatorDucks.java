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

import java.util.Arrays;

public class IndicatorDucks extends GameObject {

    private int NUM_DUCKS = 10;
    private Bitmap bmpHit, bmpBlueLines, bmpDuckWhite, bmpDuckRed;
    protected Bitmap bmpCur;
    private Paint paint;
    private Vector2D hitPosition, blueLinesPosition;
    private Vector2D[] duckPositions;
    private boolean[] hits;
    private int numDucksHit = 0;
    private int numDucksServiced = 0;
    private Context context;

    public IndicatorDucks(Context context) {
        this.context = context;

        duckPositions = new Vector2D[NUM_DUCKS];

        populateSprites(context);
        populatePositions();

        paint = new Paint();

        layer = GameConstants.FOREGROUND;

        hits = new boolean[NUM_DUCKS];
        Arrays.fill(hits, false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bmpHit, hitPosition.x, hitPosition.y, paint);
        canvas.drawBitmap(bmpBlueLines, blueLinesPosition.x, blueLinesPosition.y, paint);
        for (int i = 0; i < NUM_DUCKS; i++) {
            canvas.drawBitmap(hits[i] ? bmpDuckRed : bmpDuckWhite, duckPositions[i].x, duckPositions[i].y, paint);
        }
    }

    @Override
    public void onUpdate() {

    }

    public void hitDuck(boolean gotDuck) {
        if (numDucksServiced > NUM_DUCKS - 1) return;
        if (gotDuck) {
            hits[numDucksServiced] = true;
            numDucksHit++;
        }
        numDucksServiced++;
    }

    private void populateSprites(Context context) {
        bmpHit = BitmapFactory.decodeResource(context.getResources(), R.drawable.hit);
        bmpBlueLines = BitmapFactory.decodeResource(context.getResources(), R.drawable.blue_lines);
        bmpDuckWhite = BitmapFactory.decodeResource(context.getResources(), R.drawable.indicator_duck_white);
        bmpDuckRed = BitmapFactory.decodeResource(context.getResources(), R.drawable.indicator_duck_red);
    }

    private void populatePositions() {
        hitPosition = new Vector2D((float)(0.28 * Utilities.SCREEN_WIDTH),(float)(0.92 * Utilities.SCREEN_HEIGHT));
        blueLinesPosition = new Vector2D(hitPosition.x + bmpHit.getWidth() + 30, (float)(0.95 * Utilities.SCREEN_HEIGHT));
        Vector2D duckStartPos = new Vector2D(hitPosition.x + bmpHit.getWidth() + 30, (float)(0.92 * Utilities.SCREEN_HEIGHT));
        float duckSpacing = 30;
        for (int i = 0; i < duckPositions.length; i++) {
            duckPositions[i] = new Vector2D(duckStartPos.x + (i * duckSpacing), duckStartPos.y);
        }
    }
}
