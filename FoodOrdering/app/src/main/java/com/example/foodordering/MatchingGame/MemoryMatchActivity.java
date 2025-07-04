package com.example.foodordering.MatchingGame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.MatchingGame.Game5x4Activity;
import com.example.foodordering.MatchingGame.Game6x5Activity;
import com.example.foodordering.MatchingGame.GameMatch3Activity;
import com.example.foodordering.R;

public class MemoryMatchActivity extends AppCompatActivity {

    Button button5x4;
    Button button6x5;
    Button buttonMatch3;
    ImageView backtoMain2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_match);

        button5x4 = findViewById(R.id.button1);
        button5x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGame5x4Activity();
            }
        });

        button6x5 = findViewById(R.id.button2);
        button6x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGame6x5Activity();
            }
        });

        buttonMatch3 = findViewById(R.id.button3);
        buttonMatch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGameMatch3Activity();
            }
        });

        backtoMain2 = findViewById(R.id.backtoMain2);
        backtoMain2.setOnClickListener(v -> navigateToMainActivity());
    }

    public void openGame5x4Activity() {
        Intent intent5x4 = new Intent(this, Game5x4Activity.class);
        startActivity(intent5x4);
    }

    public void openGame6x5Activity() {
        Intent intent6x5 = new Intent(this, Game6x5Activity.class);
        startActivity(intent6x5);
    }

    public void openGameMatch3Activity() {
        Intent intentMatch3 = new Intent(this, GameMatch3Activity.class);
        startActivity(intentMatch3);
    }

    // Define navigateToMainActivity method
    public void navigateToMainActivity() {
        // This will navigate back to the MainActivity or whatever your main activity is.
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
        finish(); // Optional: If you want to close the current activity after navigating.
    }
}
