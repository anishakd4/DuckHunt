package com.developer.anishakd4.duckhunt.GamingObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.developer.anishakd4.duckhunt.GameUtils.BasicPhysicsComponent;
import com.developer.anishakd4.duckhunt.GameUtils.GameObject;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.GameSoundHandler;
import com.developer.anishakd4.duckhunt.Utils.Utilities;
import com.developer.anishakd4.duckhunt.Utils.Vector2D;

public class Dog extends GameObject {

    private boolean readyToJump;
    private boolean barked;
    private boolean finishingStage;
    public boolean jumpUp;
    public boolean jumpDown;
    private Bitmap[] sprites;
    public Bitmap current_sprite;
    private BasicPhysicsComponent physicsComponent;
    private Paint paint;
    private int frame = 0;
    private Context context;

    public Dog(BasicPhysicsComponent physicsComponent, Context context) {
        this.physicsComponent = physicsComponent;
        this.context = context;

        readyToJump = false;
        barked = false;
        finishingStage = false;
        jumpUp = true;
        jumpDown = false;

        sprites = new Bitmap[12];
        populateSprites();
        scaleSprites();
        current_sprite = sprites[0];

        this.physicsComponent.forward = new Vector2D((float)(0.0037 * Utilities.SCREEN_WIDTH), 0);

        position = new Vector2D(0.0f, (float) ((1 - 0.27)* Utilities.SCREEN_HEIGHT));

        paint = new Paint();
        layer = GameConstants.FOREGROUND;
    }

    private void populateSprites() {
        for (int i = 0; i < GameConstants.NUMBER_OF_DOG_SPRITES; i++) {
            int j = i + 1;
            sprites[i] = BitmapFactory.decodeResource(context.getResources(),
                    context.getResources()
                            .getIdentifier("dog" + j, "drawable", context.getPackageName()));
        }
    }

    private void scaleSprites() {
        for (int i = 0; i < GameConstants.NUMBER_OF_DOG_SPRITES; i++) {
            sprites[i] = Bitmap.createScaledBitmap(
                    sprites[i],
                    (int)(Utilities.SCREEN_WIDTH * 0.2),
                    (int)((Utilities.SCREEN_WIDTH * 0.2 * sprites[i].getHeight())/sprites[i].getWidth()),
                    false);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!readyToJump) {
            if (this.position.x < (Utilities.SCREEN_WIDTH/2) - current_sprite.getWidth() / 2) {
                current_sprite = sprites[frame % 5];
            } else {
                current_sprite = sprites[5];
                this.physicsComponent.forward.x = 0;
                readyToJump = true;
            }
        } else {
            if (!finishingStage) {
                bark();
                jump();
            }
        }
        canvas.drawBitmap(current_sprite, position.x, position.y, paint);
    }

    public void comeUpToFinishStage(int numDucksShot, float newX) {
        layer = GameConstants.MIDGROUND;
        finishingStage = true;
        switch (numDucksShot) {
            case 1:
                current_sprite = sprites[8];
                break;
            case 2:
                current_sprite = sprites[9];
                break;
            case 0:
                current_sprite = sprites[10];
                break;
        }
        position.x = newX;
        position.y = (float) (Utilities.SCREEN_HEIGHT * 0.6 - current_sprite.getHeight());
    }

    public void returnToGrass() {
        //this.physicsComponent.forward.y = -13;
        position.y = (float) ((1 - 0.27)*Utilities.SCREEN_HEIGHT);
        layer = GameConstants.BACKGROUND;
    }

    private void jump() {
        if (jumpUp) {
            current_sprite = sprites[6];
            this.physicsComponent.forward.y = -13;
            if(this.position.y <= (Utilities.SCREEN_HEIGHT/2)){
                jumpUp = false;
                jumpDown = true;
            }
        } else if (jumpDown) {
            this.physicsComponent.forward.y = 13;
            current_sprite = sprites[7];
            layer = GameConstants.BACKGROUND;
            if(this.position.y >= ((1 - 0.27)*Utilities.SCREEN_HEIGHT)){
                jumpDown = false;
                this.position.y = (float) ((1 - 0.27)*Utilities.SCREEN_HEIGHT);
            }
        } else {
            this.physicsComponent.forward.y = 0;
            this.physicsComponent.forward.x = 0;
        }
    }

    private void bark() {
        if (!barked) {
            GameSoundHandler.getInstance(context).stopLongSound();
            GameSoundHandler.getInstance(context).playSound(GameConstants.DOG_BARKING_SOUND);
            barked = true;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {

            }
        }
    }

    @Override
    public void onUpdate() {
        this.physicsComponent.update(this);
        frame++;
    }
}
