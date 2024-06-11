package com.example.luh_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int GAME_ACT = 600;
    SharedPreferences prefs;
    TextView highscoreText;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        class Highscore {
            public Highscore(String name, int score) {
                this.name = name;
                this.score = score;
            }

            String name;
            int score;
        }

        prefs = getSharedPreferences("GAME", 0);
        highscoreText = findViewById(R.id.highscoreTextView);
        listView = findViewById(R.id.listView);
        List<Highscore> values = new ArrayList<Highscore>();
        values.add(new Highscore("Ian",30));
        values.add(new Highscore("Ban",10));
        ArrayAdapter<Highscore> adapter = new ArrayAdapter<Highscore>(this,
                R.layout.highscoreitem, R.id.textViewName,  values){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View result = super.getView(position, convertView, parent);
                TextView txtName = result.findViewById(R.id.textViewName);
                TextView txtScore = result.findViewById(R.id.textViewScore);
                txtName.setText(values.get(position).name);
                txtScore.setText(String.valueOf(values.get(position).score));

                return result;
            }
        };
        listView.setAdapter(adapter);
    }

    public void startGame(View v) {
        Intent switchActivity = new Intent(this, GameActivity.class);
        startActivityForResult(switchActivity, GAME_ACT);
    }

    @Override
    protected void onActivityResult(int request, int result, Intent i) {
        super.onActivityResult(request, result, i);
        if (request == GAME_ACT) {
            if (prefs.getInt("HIGHSCORE", 0) <= result) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("HIGHSCORE", result);
                edit.apply();
                edit.commit();
            }
        }
        updateValues();
        Log.d("", "update");
    }

    public void updateValues() {
        highscoreText.setText("Highscore: " + prefs.getInt("HIGHSCORE", 0));
    }
}