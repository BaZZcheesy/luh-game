package com.example.luh_game;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements Runnable  {
    Handler handler;
    int timeSlot = 1000;
    int dracheCatched = 0;
    int dracheToCatch = 10;
    int score = 0;
    int level = 1;
    final int LEVEL_TIME = 20;
    double timeRemaining;
    long startTime;
    TextView levelCount, scoreText, timeRemainingText, dracheRemainingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        scoreText = findViewById(R.id.scoreView);
        timeRemainingText = findViewById(R.id.timeRemaining);
        levelCount = findViewById(R.id.levelCount);
        dracheRemainingText = findViewById(R.id.remainingDrache);

        startTime = new Date().getTime();
        handler = new Handler();
        handler.postDelayed(this,timeSlot);
    }

    public void goToStart(View v) {
        this.finish();
    }

    public void spawnLord(View v) {
        FrameLayout gameFrame = findViewById(R.id.frameLayout);

        int dHeight = gameFrame.getHeight()/10;
        int dWidth = gameFrame.getWidth()/10;

        ImageView drache = new ImageView(this);
        drache.setImageResource(R.drawable.drache_sitz);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dWidth, dWidth);


        drache.setOnClickListener(killedDrache -> {
            gameFrame.removeView(killedDrache);
            dracheCatched++;
            score++;
        });

        int rt = (int)(Math.random()*(gameFrame.getHeight() - dHeight));
        int rl = (int)(Math.random()*(gameFrame.getWidth() - dWidth));

        params.topMargin = rt;
        params.leftMargin = rl;

        drache.setLayoutParams(params);

        gameFrame.addView(drache);
    }

    @Override
    public void run() {
        decrementTime();
        updateValues(null);
    }

    public void decrementTime() {
        timeRemaining = LEVEL_TIME - (double) (new Date().getTime() - startTime) /1000;
        if (!gameFinished()) {
            if (!levelFinished()) {
                spawnLord(null);
                updateValues(null);
                handler.postDelayed(this, timeSlot);
            }
            else {
                startNextLevel();
            }
        }
        else {
            gameOver();
        }
    }

    public boolean levelFinished() {
        return dracheCatched >= dracheToCatch;
    }

    public boolean gameFinished() {
        return timeRemaining <= 0 && dracheCatched < dracheToCatch;
    }

    public void startNextLevel() {
        startTime = new Date().getTime();
        level++;
        dracheCatched = 0;
        updateValues(null);
        handler.postDelayed(this, timeSlot);
    }

    public void gameOver() {
        FrameLayout gameFrame = findViewById(R.id.frameLayout);
        gameFrame.removeAllViews();
        Toast.makeText(this, "GAME OVER", Toast.LENGTH_SHORT).show();
        setResult(score);
        handler.postDelayed(() -> {
            finish();
        }, 1000);
    }

    public void updateValues(View v) {
        scoreText.setText("Score: " + score);
        timeRemainingText.setText("Time remaining: " + (int)timeRemaining);
        levelCount.setText("Level " + level);
        dracheRemainingText.setText((dracheToCatch - dracheCatched) + " Remaining");
    }
}