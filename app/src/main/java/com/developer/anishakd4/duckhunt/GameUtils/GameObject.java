package com.developer.anishakd4.duckhunt.GameUtils;

import android.graphics.Canvas;

import com.developer.anishakd4.duckhunt.Utils.Vector2D;

public abstract class GameObject {

    public int layer;
    public Vector2D position;
    public boolean destroy;

    public abstract void onDraw(Canvas canvas);

    public abstract void onUpdate();
}
