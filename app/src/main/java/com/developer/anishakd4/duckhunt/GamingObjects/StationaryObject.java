package com.developer.anishakd4.duckhunt.GamingObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.developer.anishakd4.duckhunt.GameUtils.GameObject;

public class StationaryObject extends GameObject {

    private Bitmap bitmap;
    private Paint paint;
    private int xPos = 0;
    private int yPos = 0;

    public StationaryObject(int bitmapId, int xScale, int yScale, Context context) {
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), bitmapId), xScale, yScale, true);
        paint = new Paint();
    }

    public void setY(int y){
        this.yPos = y;
    }

    public void setX(int x){
        this.xPos = x;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, xPos, yPos, paint);
    }

    @Override
    public void onUpdate() {

    }
}
