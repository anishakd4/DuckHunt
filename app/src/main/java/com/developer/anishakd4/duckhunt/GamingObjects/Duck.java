package com.developer.anishakd4.duckhunt.GamingObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.developer.anishakd4.duckhunt.GameUtils.BasicPhysicsComponent;
import com.developer.anishakd4.duckhunt.GameUtils.GameEngine;
import com.developer.anishakd4.duckhunt.GameUtils.GameObject;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.GameSoundHandler;
import com.developer.anishakd4.duckhunt.Utils.Utilities;
import com.developer.anishakd4.duckhunt.Utils.Vector2D;

import java.util.Random;

public class Duck extends GameObject {

    private Context context;
    public boolean isAlive;
    public boolean timeToFlyAway = false;
    private int frame;
    private int duckOrientation;
    private int timeToSwitchOrientation;
    private int timeToSwitchHorizontalDirection;
    private int timeToSwitchVerticalDirection;
    private float timeSinceShot;
    private float deathPoint;
    private float timeSinceSpawned;
    private Bitmap[][] sprites;
    private Bitmap current_sprite;
    private Paint paint;
    private String duckColor;
    public BasicPhysicsComponent physicsComponent;


    public Duck(float x, float y, String duckColor, final BasicPhysicsComponent physicsComponent, Context context) {
        this.physicsComponent = physicsComponent;
        this.context = context;
        this.duckColor = duckColor;

        isAlive = true;

        position = new Vector2D(x, y);

        physicsComponent.setForward(new Vector2D(4, -10));

        sprites = new Bitmap[GameConstants.NUMBER_OF_DUCK_ORIENTATIONS][GameConstants.NUMBER_OF_DUCK_SPRITES];
        populateSprites(context);
        scaleSprites();

        duckOrientation = GameConstants.DIAGONAL;
        int frame = new Random().nextInt(GameConstants.NUMBER_OF_DUCK_SPRITES);
        current_sprite = sprites[GameConstants.DIAGONAL][frame];

        boolean isFlipped = Utilities.getRandomNumberInt(1000) == 0;
        if (isFlipped) {
            flipSprites();
            this.physicsComponent.forward.x = this.physicsComponent.forward.x * -1;
        }

        paint = new Paint();

        timeSinceSpawned = 0.0f;
        timeSinceShot = 0.0f;
        timeToSwitchOrientation = new Random().nextInt(20) + 20;
        timeToSwitchHorizontalDirection = new Random().nextInt(60) + 30;
        timeToSwitchVerticalDirection = new Random().nextInt(90) + 60;

        layer = GameConstants.BACKGROUND;
    }

    public void populateSprites(Context context) {
        for (int i = 0; i < GameConstants.NUMBER_OF_DUCK_SPRITES; i++) {
            int j = i + 1;
            sprites[GameConstants.DIAGONAL][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources()
                            .getIdentifier(duckColor + "duck_diagonal" + j, "drawable",
                                    context.getPackageName()));
            sprites[GameConstants.HORIZONTAL][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources()
                            .getIdentifier(duckColor + "duck_horizontal" + j, "drawable",
                                    context.getPackageName()));
            sprites[GameConstants.BACK][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources().getIdentifier(duckColor + "duck_back" + j, "drawable",
                            context.getPackageName()));
            sprites[GameConstants.DEFEAT][i] = BitmapFactory.decodeResource(
                    context.getResources(),
                    context.getResources()
                            .getIdentifier(duckColor + "duck_defeated" + j, "drawable",
                                    context.getPackageName()));
        }

    }

    public void scaleSprites() {
        for (int i = 0; i < GameConstants.NUMBER_OF_DUCK_ORIENTATIONS; ++i) {
            for (int j = 0; j < GameConstants.NUMBER_OF_DUCK_SPRITES; ++j) {
                sprites[i][j] = Bitmap.createScaledBitmap(sprites[i][j], (int)(sprites[i][j].getWidth() * 1.5), (int)(sprites[i][j].getHeight() * 1.5), false);
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isAlive) {
            current_sprite = sprites[duckOrientation][frame % GameConstants.NUMBER_OF_DUCK_SPRITES];
        } else {
            duckOrientation = GameConstants.DEFEAT;
            if (timeSinceShot < GameConstants.DELAY_AFTER_SHOT) {
                deathPoint = position.y;
                current_sprite = sprites[duckOrientation][0];
            } else {
                if (timeSinceShot < GameConstants.DELAY_TO_DISPLAY_SCORE) {
                    canvas.drawBitmap(sprites[duckOrientation][2], position.x, deathPoint, paint);
                }
                current_sprite = sprites[duckOrientation][1];
                if (frame % 2 == 0) {
                    current_sprite = flip(current_sprite);
                }
            }
        }
        canvas.drawBitmap(current_sprite, position.x, position.y, paint);
    }

    @Override
    public void onUpdate() {
        timeToFlyAway |= (timeSinceSpawned > GameConstants.TIME_ON_SCREEN);
        if (isAlive) {
            GameSoundHandler.getInstance(context).playSound(GameConstants.DUCK_FLAP_SOUND);
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
            }
            if (timeToFlyAway) {
                flyAway();
            } else {
                timeSinceSpawned += GameEngine.DELTA_TIME;
                performTimeChecks();
            }
        } else {
            if (timeSinceShot < GameConstants.DELAY_AFTER_SHOT) {
                timeSinceShot += GameEngine.DELTA_TIME;
                this.physicsComponent.forward.x = 0;
                this.physicsComponent.forward.y = 0;
            } else {
                if (this.physicsComponent.forward.y <= 0) {
                    GameSoundHandler.getInstance(context).playSound(GameConstants.DEAD_DUCK_FALL_SOUND);
                }
                this.physicsComponent.forward.y = 30;
            }
        }
        physicsComponent.update(this);
        checkBorder();
        frame++;
    }


    private void performTimeChecks() {
        checkOrientationTime();
        checkHorizontalDirectionTime();
        checkVerticalDirectionTime();
    }

    private void checkHorizontalDirectionTime() {
        if (frame > 0 && frame % timeToSwitchHorizontalDirection == 0) {
            this.physicsComponent.forward.x = this.physicsComponent.forward.x * -1;
            flipSprites();
        }
    }

    private void checkVerticalDirectionTime() {
        if (frame > 0 && frame % timeToSwitchVerticalDirection == 0) {
            this.physicsComponent.forward.y = this.physicsComponent.forward.y * -1;
        }
    }

    private void checkOrientationTime() {
        if (frame > 0 && frame % timeToSwitchOrientation == 0) {
            duckOrientation = new Random().nextInt(GameConstants.NUMBER_OF_DUCK_SPRITES);
        }
    }

    private void flyAway() {
        duckOrientation = GameConstants.BACK;
        this.physicsComponent.forward.y = -20;
        this.physicsComponent.forward.x = 0;
    }

    public void checkBorder() {
        if (this.position.x > (Utilities.SCREEN_WIDTH - current_sprite.getWidth())) {
            this.position.x = Utilities.SCREEN_WIDTH - current_sprite.getWidth();
            this.physicsComponent.forward.x = this.physicsComponent.forward.x * -1;
            flipSprites();
        }
        if (this.position.x < current_sprite.getWidth()) {
            this.position.x = current_sprite.getWidth();
            this.physicsComponent.forward.x = this.physicsComponent.forward.x * -1;
            flipSprites();
        }
        if (this.position.y > 0.6 * Utilities.SCREEN_HEIGHT) {
            if (!isAlive) {
                this.destroy = true;
                GameSoundHandler.getInstance(context).stopAllSounds();
                GameSoundHandler.getInstance(context).playSound(GameConstants.DEAD_DUCK_LAND_SOUND);
            } else {
                this.position.y = (int) (0.6 * Utilities.SCREEN_HEIGHT);
                this.physicsComponent.forward.y = this.physicsComponent.forward.y * -1;
            }
        }
        if (this.position.y < current_sprite.getWidth()) {
            if (timeToFlyAway) {
                this.destroy = true;
            } else {
                this.position.y = current_sprite.getWidth();
                this.physicsComponent.forward.y = this.physicsComponent.forward.y * -1;
            }
        }
    }

    public String getDuckColor() {
        return duckColor;
    }

    private void flipSprites() {
        for (int i = 0; i < GameConstants.NUMBER_OF_DUCK_ORIENTATIONS - 1; ++i) {
            for (int j = 0; j < GameConstants.NUMBER_OF_DUCK_SPRITES; ++j) {
                sprites[i][j] = flip(sprites[i][j]);
            }
        }
    }

    private Bitmap flip(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
