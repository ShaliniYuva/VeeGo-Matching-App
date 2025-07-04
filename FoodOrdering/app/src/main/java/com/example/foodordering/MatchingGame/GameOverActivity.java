package com.example.foodordering.MatchingGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.R;

public class GameOverActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Retrieve data from Intent
        int totalFlips = getIntent().getIntExtra("TOTAL_FLIPS", 0);
        String timeTaken = getIntent().getStringExtra("TIME_TAKEN");
        int gameMode = getIntent().getIntExtra("GAME_MODE", 1); // Default to 5x4 mode

        // Update UI elements
        TextView flipsTextView = findViewById(R.id.total_flips);
        TextView timeTextView = findViewById(R.id.time_taken);
        flipsTextView.setText(String.valueOf(totalFlips));
        timeTextView.setText(timeTaken);

        // Play Again button logic
        Button playAgainButton = findViewById(R.id.play_again_button);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (gameMode == 1) {
                    intent = new Intent(GameOverActivity.this, Game5x4Activity.class);
                } else if (gameMode == 2) {
                    intent = new Intent(GameOverActivity.this, Game6x5Activity.class);
                } else {
                    intent = new Intent(GameOverActivity.this, GameMatch3Activity.class);
                }
                intent.putExtra("GAME_MODE", gameMode);
                startActivity(intent);
                finish();

            }
        });
    }
}
