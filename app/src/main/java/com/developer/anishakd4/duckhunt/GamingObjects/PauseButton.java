package com.developer.anishakd4.duckhunt.GamingObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.developer.anishakd4.duckhunt.GameUtils.GameObject;
import com.developer.anishakd4.duckhunt.R;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.Utilities;
import com.developer.anishakd4.duckhunt.Utils.Vector2D;

public class PauseButton extends GameObject {

    private Context context;
    public boolean paused = false;
    public RectF pauseButtonBox;
    public RectF replayButtonBox;
    public RectF quitButtonBox;
    private Paint boxPaint, textPaint;
    private Bitmap replay, quit;

    public PauseButton(Context context) {
        this.context = context;

        position = new Vector2D((float)(0.75 * Utilities.SCREEN_WIDTH), (float)(0.85 * Utilities.SCREEN_HEIGHT));

        boxPaint = new Paint();
        boxPaint.setColor(Color.BLACK);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);
        textPaint.setTextAlign(Paint.Align.LEFT);

        populateBitmaps();

        pauseButtonBox = new RectF(position.x - 20, position.y - 70, (float) (position.x + (0.16 * Utilities.SCREEN_WIDTH)), position.y + 30 );

        replayButtonBox = new RectF((Utilities.SCREEN_WIDTH/2 - replay.getWidth()/2),
                (float)(0.3* Utilities.SCREEN_HEIGHT),
                (Utilities.SCREEN_WIDTH/2 + replay.getWidth()/2),
                (float)(0.3* Utilities.SCREEN_HEIGHT + replay.getHeight())
        );

        quitButtonBox = new RectF((Utilities.SCREEN_WIDTH/2 - quit.getWidth()/2),
                (float)(0.4* Utilities.SCREEN_HEIGHT),
                (Utilities.SCREEN_WIDTH/2 + quit.getWidth()/2),
                (float)(0.4* Utilities.SCREEN_HEIGHT + quit.getHeight())
        );

        layer = GameConstants.FOREGROUND;
    }

    public void populateBitmaps(){
        replay = BitmapFactory.decodeResource(context.getResources(), R.drawable.replay);
        quit = BitmapFactory.decodeResource(context.getResources(), R.drawable.quit);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawRect(pauseButtonBox, boxPaint);
        if (paused) {
            canvas.drawBitmap(replay, null, replayButtonBox, null);
            canvas.drawBitmap(quit, null, quitButtonBox, null);
            canvas.drawText("Start", position.x, position.y, textPaint);
        } else {
            canvas.drawText("Pause", position.x, position.y, textPaint);
        }
    }

    @Override
    public void onUpdate() {

    }
}
