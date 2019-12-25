package com.developer.anishakd4.duckhunt.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.anishakd4.duckhunt.GameUtils.GameEngine;
import com.developer.anishakd4.duckhunt.Listeners.OnHomePressedListener;
import com.developer.anishakd4.duckhunt.R;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;
import com.developer.anishakd4.duckhunt.Utils.HomeWatcher;
import com.developer.anishakd4.duckhunt.Utils.Utilities;

public class MainActivity extends AppCompatActivity {

    GameEngine gameEngine;
    HomeWatcher homeWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Point point = getScreenHeightAndWidth();

        Bundle b = getIntent().getExtras();
        final int numberOfDucks = b.getInt(GameConstants.NUMBER_OF_DUCKS);
        final int level = b.getInt(GameConstants.ROUND);
        final int score = b.getInt(GameConstants.SCORE);

        gameEngine = new GameEngine(this, numberOfDucks, point, level, score);
        setContentView(gameEngine);

        homeWatcher = new HomeWatcher(this);
        homeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                gameEngine.pauseButtonPressed = true;
                gameEngine.pause();
            }
        });
        homeWatcher.startWatch();
    }

    private Point getScreenHeightAndWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        Utilities.SCREEN_HEIGHT = point.y;
        Utilities.SCREEN_WIDTH = point.x;

        Log.i("MainActivity: ", "" + width + " " + point.x + " " + height + " " + point.y);
        return point;
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScore();
        gameEngine.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.resume();
    }

    @Override
    protected void onStop() {
        saveScore();
        super.onStop();
    }

    private void saveScore() {
        SharedPreferences prefs = this.getSharedPreferences(GameConstants.KEY_TOP_SCORE_PREFS, Context.MODE_PRIVATE);
        if (prefs.getInt(GameConstants.KEY_TOP_SCORE, 0) < gameEngine.indicatorScore.getScore()) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(GameConstants.KEY_TOP_SCORE, gameEngine.indicatorScore.getScore());
            editor.commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        homeWatcher.stopWatching();
        gameEngine.pauseButtonPressed = true;
        gameEngine.pause();
        finish();
    }
}
