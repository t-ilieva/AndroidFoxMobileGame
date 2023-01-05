package com.example.foxgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {

    TextView tvPoints;
    TextView tvHighest;
    ImageView ivNewHighest;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        tvPoints = findViewById(R.id.tvPoints);
        tvHighest = findViewById(R.id.tvHighest);
        ivNewHighest = findViewById(R.id.ivNewHighest);
        int points = getIntent().getExtras().getInt("points");
        tvPoints.setText(points + " ");
        sharedPreferences = getSharedPreferences("my_preferences", 0);
        int highestScore = sharedPreferences.getInt("highest", 0);

        if (points > highestScore){
            ivNewHighest.setVisibility(View.VISIBLE);
            highestScore = points;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highest", highestScore);
            editor.commit();
        }

        tvHighest.setText(highestScore + " ");
    }

    public void restart(View view){
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view){
        finish();
    }
}
