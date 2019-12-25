package com.developer.anishakd4.duckhunt.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.anishakd4.duckhunt.R;
import com.developer.anishakd4.duckhunt.Utils.GameConstants;

public class StartupActivity extends AppCompatActivity {

    private Button gameA_start;
    private Button gameB_start;
    private TextView topScoreView;
    MediaPlayer mediaPlayer;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        activity = this;

        initialize();

        mediaPlayer = MediaPlayer.create(this, R.raw.title_screen);
        mediaPlayer.start();

        clickListeners();

        displayTopScore();
    }

    private void initialize(){
        gameA_start = findViewById(R.id.gameA_button);
        gameB_start = findViewById(R.id.gameB_button);
        topScoreView = findViewById(R.id.textView_topScore);
        topScoreView.setTextColor(Color.GREEN);
    }

    private void clickListeners(){
        gameA_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(StartupActivity.this,R.raw.gun_shot);
                mediaPlayer.start();
                Intent i = new Intent(activity, MainActivity.class);
                Bundle b = new Bundle();
                b.putInt(GameConstants.NUMBER_OF_DUCKS, 1);
                b.putInt(GameConstants.ROUND, 1);
                b.putInt(GameConstants.SCORE, 0);
                i.putExtras(b);
                startActivity(i);
            }
        });

        gameB_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer = MediaPlayer.create(StartupActivity.this,R.raw.gun_shot);
                mediaPlayer.start();
                Intent i = new Intent(activity, MainActivity.class);
                Bundle b = new Bundle();
                b.putInt(GameConstants.NUMBER_OF_DUCKS, 2);
                b.putInt(GameConstants.ROUND, 1);
                b.putInt(GameConstants.SCORE, 0);
                i.putExtras(b);
                startActivity(i);
            }
        });
    }

    private void displayTopScore(){
        SharedPreferences prefs = this.getSharedPreferences(GameConstants.KEY_TOP_SCORE_PREFS, Context.MODE_PRIVATE);
        int topScore = prefs.getInt(GameConstants.KEY_TOP_SCORE, 0);
        topScoreView.setText(getString(R.string.string_topScore, topScore));
    }
}
