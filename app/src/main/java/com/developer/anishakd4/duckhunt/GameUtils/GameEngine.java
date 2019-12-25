package com.developer.anishakd4.duckhunt.GameUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.developer.anishakd4.duckhunt.Activity.MainActivity;
import com.developer.anishakd4.duckhunt.Activity.StartupActivity;
import com.developer.anishakd4.duckhunt.GamingObjects.Dog;
import com.developer.anishakd4.duckhunt.GamingObjects.Duck;
import com.developer.anishakd4.duckhunt.GamingObjects.IndicatorDucks;
import com.developer.anishakd4.duckhunt.GamingObjects.IndicatorScore;
import com.developer.anishakd4.duckhunt.GamingObjects.IndicatorShots;
import com.developer.anishakd4.duckhunt.GamingObjects.PauseButton;
import com.developer.anishakd4.duckhunt.GamingObjects.StationaryObject;
import com.developer.anishakd4.duckhunt.GamingObjects.UserIndicator;
import com.developer.anishakd4.duckhunt.R;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.GameSoundHandler;
import com.developer.anishakd4.duckhunt.Utils.Utilities;
import com.developer.anishakd4.duckhunt.Utils.Vector2D;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class GameEngine extends SurfaceView implements View.OnTouchListener, Runnable {

    Context context;
    int round;
    boolean roundComplete;
    protected SurfaceHolder surfaceHolder;
    public int SCREEN_HEIGHT;
    public int SCREEN_WIDTH;
    Paint paint;
    private ArrayList<GameObject> gameObjects;
    StationaryObject background_top, background_bottom;
    Dog dog;
    public static float DELTA_TIME;
    DuckFactory duckFactory;
    IndicatorShots indicatorShots;
    IndicatorDucks indicatorDucks;
    public IndicatorScore indicatorScore;
    UserIndicator userIndicator;
    PauseButton pauseButton;
    public int numberOfDucksPerStage;
    Stack<Duck> duckies = new Stack<>();
    boolean completedStartingSequence;
    public boolean pauseButtonPressed = false;
    volatile boolean isPlaying = false;
    protected Thread gameThread;
    Thread soundThread;
    int numDucksHitThisRound = 0;
    boolean outOFBullets = false;
    Stack<Float> deadDuckLandingSpots = new Stack<>();
    int numDucksHitThisStage;


    public GameEngine(Context context, int numberOfDucksPerStage, Point point, int round, int score) {
        super(context);
        this.context = context;
        this.round = round;
        roundComplete = false;
        surfaceHolder = getHolder();
        SCREEN_WIDTH = point.x;
        SCREEN_HEIGHT = point.y;
        paint = new Paint();

        createGameObjects(score);

        this.setOnTouchListener(this);
        this.numberOfDucksPerStage = numberOfDucksPerStage;
        completedStartingSequence = false;
        GameSoundHandler.getInstance(context).playLongSound(GameConstants.STARTING_SEQUENCE_SOUND);
    }

    public void createGameObjects(int score){
        gameObjects = new ArrayList<>();

        background_top = new StationaryObject(R.drawable.background_top, SCREEN_WIDTH,
                (int) (SCREEN_HEIGHT * GameConstants.BACKGROUND_TOP_PERCENTAGE), context);
        background_top.layer = GameConstants.MIDGROUND;
        gameObjects.add(background_top);

        background_bottom = new StationaryObject(R.drawable.background_bottom, SCREEN_WIDTH,
                (int) (SCREEN_HEIGHT * GameConstants.BACKGROUND_BOTTOM_PERCENTAGE), context);
        background_bottom.layer = GameConstants.FOREGROUND;
        background_bottom.setY((int)(SCREEN_HEIGHT * (1 - GameConstants.BACKGROUND_BOTTOM_PERCENTAGE)));
        gameObjects.add(background_bottom);

        dog = new Dog(new BasicPhysicsComponent(), context);
        gameObjects.add(dog);

        duckFactory = new DuckFactory(context);
        for (int ii = 0; ii < GameConstants.NUMBER_OF_DUCKS_DEPLOYED; ++ii) {
            duckies.push(duckFactory.makeRandomDuck());
        }

        userIndicator = new UserIndicator(round, context);
        gameObjects.add(userIndicator);

        indicatorShots = new IndicatorShots(context);
        gameObjects.add(indicatorShots);

        indicatorDucks = new IndicatorDucks(context);
        gameObjects.add(indicatorDucks);

        indicatorScore = new IndicatorScore(score);
        gameObjects.add(indicatorScore);

        pauseButton = new PauseButton(context);
        gameObjects.add(pauseButton);
    }

    @Override
    public void run() {
        while (isPlaying) {
            handleGameLogic();
            long previousTimeMillis = System.currentTimeMillis();
            if (!roundComplete) {
                update();
                draw();
                long currentTimeMillis = System.currentTimeMillis();
                DELTA_TIME = (currentTimeMillis - previousTimeMillis) / 1000.0f;
                try {
                    int DESIRED_FPS = 30;
                    int TIME_BETWEEN_FRAMES = 1000 / DESIRED_FPS;
                    gameThread.sleep(TIME_BETWEEN_FRAMES);
                } catch (InterruptedException e) {

                }
            } else {
                goToNextRound();
                isPlaying = false;
            }
        }
    }

    public void handleGameLogic() {
        if (!dog.jumpDown && !dog.jumpUp && !completedStartingSequence) {
            completedStartingSequence = true;
        }
        if (completedStartingSequence) {
            boolean readyToDeployMoreDucks = true;
            for (GameObject o : gameObjects) {
                if (o instanceof Duck) {
                    readyToDeployMoreDucks = false;
                }
            }
            if (readyToDeployMoreDucks) {
                handleEndOfStage();
                if (duckies.empty()) {
                    roundComplete = true;
                } else {
                    addMoreDucks();
                }
            }
        }
    }

    // updates each game object in the list
    public void update() {
        for (Iterator<GameObject> iterator = gameObjects.iterator(); iterator.hasNext(); ) {
            GameObject gameObject = iterator.next();
            if (!gameObject.destroy) {
                gameObject.onUpdate();
            } else {
                iterator.remove();
            }
        }
    }

    // draws each game object in order of layers so the objects in the background are drawn first, midground second, and foreground last
    // this enables some of the nifty animations we have
    public void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawARGB(255, 63, 191, 255);
            for (int ii = GameConstants.BACKGROUND; ii <= GameConstants.FOREGROUND; ++ii) {
                for (GameObject gameObject : gameObjects) {
                    if (gameObject.layer == ii) {
                        gameObject.onDraw(canvas);
                    }
                }
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    // pauses the game, plays the pause sound, and displays the restart/quit options the pauseButtonPressed flag is used because onPause() is called after finish()
    // and we don't want to play the pause sound every time our activity ends
    public void pause() {
        if (pauseButtonPressed) {
            pauseButton.paused = true;
            isPlaying = !isPlaying;
            GameSoundHandler.getInstance(context).isPlaying = !isPlaying;
            GameSoundHandler.getInstance(context).pauseAllSounds();
            GameSoundHandler.getInstance(context).playSound(GameConstants.PAUSE_SOUND);
            draw();
        }
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.d("GameThread", "Error pausing!");
        }

    }

    //resumes the game after being paused
    public void resume() {
        pauseButtonPressed = false;
        pauseButton.paused = false;
        isPlaying = true;
        GameSoundHandler.getInstance(context).isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
        soundThread = new Thread(GameSoundHandler.getInstance(context));
        soundThread.start();
        GameSoundHandler.getInstance(context).resumeAllSounds();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //handles if the user pressed the pause/start button
                if (pauseButton.pauseButtonBox.contains(event.getRawX(), event.getRawY())) {
                    if (isPlaying) {
                        pauseButtonPressed = true;
                        pause();
                    } else {
                        resume();
                    }
                    break;
                }
                if (!pauseButton.paused) {
                    // ensures the shot sound doesn't play if the game is paused
                    // or if the user is out of bullets
                    if(!outOFBullets) {
                        GameSoundHandler.getInstance(context).playSound(GameConstants.GUN_SHOT_SOUND);
                    }
                    outOFBullets = indicatorShots.shoot();
                    // checks if the user actually hit a duck
                    for (GameObject o : gameObjects) {
                        if (o instanceof Duck) {
                            if(!((Duck) o).timeToFlyAway) {
                                Vector2D screenPos = ((Duck) o).position;
                                float delta_x = event.getRawX() - screenPos.x; // difference in x position between shot and duck
                                float delta_y = event.getRawY() - screenPos.y; // difference in y position between the shot and duck
                                float distance = (float) Math.sqrt(delta_x * delta_x + delta_y * delta_y);
                                if (distance < GameConstants.SHOOTING_RADIUS && ((Duck) o).isAlive) {
                                    shootDuck(((Duck) o));
                                }
                                // if the user ran out of bullets and the duck is alive, the duck should fly away
                                else if(outOFBullets && ((Duck) o).isAlive){
                                    ((Duck) o).timeToFlyAway = true;
                                }
                            }
                        }
                    }
                }
                else {
                    // handles if the user pressed restart or quit after pressing pause
                    // restart - starts the game from Round 1
                    // quit - brings the user back to main menu
                    if (pauseButton.replayButtonBox.contains(event.getRawX(), event.getRawY())) {
                        GameSoundHandler.getInstance(context).stopAllSounds();
                        Intent i_start = new Intent(context, MainActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(GameConstants.NUMBER_OF_DUCKS, numberOfDucksPerStage); //Your id
                        b.putInt(GameConstants.ROUND, 1); //Your id
                        b.putInt(GameConstants.SCORE, 0);
                        i_start.putExtras(b);
                        context.startActivity(i_start);
                    }
                    else if (pauseButton.quitButtonBox.contains(event.getRawX(), event.getRawY())) {
                        GameSoundHandler.getInstance(context).stopAllSounds();
                        Intent i_start = new Intent(context, StartupActivity.class);
                        context.startActivity(i_start);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                break;

        }
        return false;
    }

    public void shootDuck(Duck duck) {
        duck.isAlive = false;
        deadDuckLandingSpots.push(duck.position.x);
        indicatorDucks.hitDuck(true);
        indicatorScore.addToScore(GameConstants.COLOR_TO_SCORE.get(duck.getDuckColor()));
        numDucksHitThisStage++;
        numDucksHitThisRound++;
    }

    public void addMoreDucks() {
        numDucksHitThisStage = 0;
        //the check below covers the case if you are doing 2 duck mode
        //and hit two ducks. The stack would still contain the position of the
        //first duck you hit.
        if (!deadDuckLandingSpots.empty()) {
            deadDuckLandingSpots.pop();
        }
        for (int ii = 0; ii < numberOfDucksPerStage; ++ii) {
            Duck duck = duckies.pop();
            gameObjects.add(duck);
            indicatorShots.setNumShots(3);
            outOFBullets = false;
        }
    }

    public void handleEndOfStage() {
        GameSoundHandler.getInstance(context).purgeSounds();
        if (duckies.size() < 10) {
            float popupShot;
            if (numDucksHitThisStage > 0) {
                popupShot = deadDuckLandingSpots.pop();
                GameSoundHandler.getInstance(context).playSound(GameConstants.GOT_DUCK);
            } else {
                popupShot = Utilities.SCREEN_WIDTH / 2;
                GameSoundHandler.getInstance(context).playSound(GameConstants.DOG_LAUGH);
            }
            dog.comeUpToFinishStage(numDucksHitThisStage, popupShot);
            draw();
            // delay to play the tune/laugh
            try {
                soundThread.sleep(1500);
            } catch (InterruptedException e) {
            }
            for (int i = 0; i < (numberOfDucksPerStage - numDucksHitThisStage); i++) {
                indicatorDucks.hitDuck(false);
            }
            dog.returnToGrass();
            draw();
        }
    }

    private int ducksRequiredToProgress() {
        if (round < 10) {
            return 6;
        } else if (round < 13) {
            return 7;
        } else if (round < 16) {
            return 8;
        } else if (round < 20) {
            return 9;
        } else {
            return 10;
        }
    }

    public void goToNextRound() {
        GameSoundHandler.getInstance(context).stopAllSounds();
        if (ducksRequiredToProgress() <= numDucksHitThisRound) {
            userIndicator.nextRound();
            draw();
            GameSoundHandler.getInstance(context).playLongSound(GameConstants.ROUND_CLEAR);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            if (numDucksHitThisRound == GameConstants.NUMBER_OF_DUCKS_DEPLOYED) {
                GameSoundHandler.getInstance(context).playSound(GameConstants.PERFECT_SCORE);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
            Intent i_start = new Intent(context, MainActivity.class);
            Bundle b = new Bundle();
            round++;
            b.putInt(GameConstants.ROUND, round);
            b.putInt(GameConstants.NUMBER_OF_DUCKS, numberOfDucksPerStage);
            b.putInt(GameConstants.SCORE, indicatorScore.getScore());
            i_start.putExtras(b);
            context.startActivity(i_start);
        } else {
            userIndicator.gameOver();
            draw();
            GameSoundHandler.getInstance(context).playSound(GameConstants.GAME_OVER);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
            Intent i_start = new Intent(context, StartupActivity.class);
            context.startActivity(i_start);
        }
    }
}
