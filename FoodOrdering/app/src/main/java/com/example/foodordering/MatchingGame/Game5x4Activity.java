package com.example.foodordering.MatchingGame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodordering.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Game5x4Activity extends AppCompatActivity implements TaskCompleted {


    // Sound & Music Variables
    private MediaPlayer backgroundMusic, flipSound, matchSound,winningSound;
    // Variables
    String URL_STRING = "https://shopicruit.myshopify.com/admin/products.json?access_token=c32313df0d0ef512ca64d5b336a0d7c6";

    ArrayList<String> img_urls;
    final int NUM_IMGS = 10;

    AlertDialog.Builder builder;
    ArrayList<ImageButton> buttons;
    Map<ImageButton, Card> hm;
    Set<ImageButton> matchedCards;

    boolean isBoardLocked;
    ImageButton firstCard;
    ImageButton secondCard;

    TextView flips, matches, timerText;
    int flip_count = 0;
    int match_count = 0;
    Button shuffle, pauseButton, playAgainButton, mainMenuButton;

    // Timer variables
    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private int seconds = 0;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5x4);

        builder = new AlertDialog.Builder(this);

        // Initialize UI components
        flips = findViewById(R.id.textView1);
        matches = findViewById(R.id.textView2);
        timerText = findViewById(R.id.timer_text);

        flips.setText("Flips: " + flip_count);
        matches.setText("Matches: " + match_count);

        img_urls = new ArrayList<>();

        shuffle = findViewById(R.id.button_shuffle);
        pauseButton = findViewById(R.id.pause_button);
        playAgainButton = findViewById(R.id.play_again_button);
        mainMenuButton = findViewById(R.id.main_menu_button);

        new AsyncComplex(Game5x4Activity.this).execute(URL_STRING);

        // Start Timer
        startTimer();

        // Start Background Music
        backgroundMusic = MediaPlayer.create(this, R.raw.background_music);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();

        // Pause Button Logic
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = !isPaused;
                if (isPaused) {
                    pauseButton.setText("Resume");
                } else {
                    pauseButton.setText("Pause");
                }
            }
        });

        // Play Again Button Logic
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        // Main Menu Button Logic
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (backgroundMusic != null) {
                    backgroundMusic.stop();
                    backgroundMusic.release();
                    backgroundMusic = null;
                }

                Intent intent = new Intent(Game5x4Activity.this, MemoryMatchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isPaused) {
                    int minutes = seconds / 60;
                    int secs = seconds % 60;
                    timerText.setText(String.format("Time: %d:%02d", minutes, secs));
                    seconds++;
                }
                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void restartGame() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            backgroundMusic = null;
        }


        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    // Method to extract image URLs from JSON response
    private void extractImageUrls(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray products = jsonObj.getJSONArray("products");
            for (int i = 0; i < NUM_IMGS; i++) {
                JSONObject p = products.getJSONObject(i);
                JSONObject image = p.getJSONObject("image");
                String image_url = image.getString("src");
                img_urls.add(image_url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Method to initialize the board and assign images to buttons
    private void initializeBoard(ArrayList<ImageButton> buttons) {
        buttons.add(findViewById(R.id.imageButton1));
        buttons.add(findViewById(R.id.imageButton2));
        buttons.add(findViewById(R.id.imageButton3));
        buttons.add(findViewById(R.id.imageButton4));
        buttons.add(findViewById(R.id.imageButton5));
        buttons.add(findViewById(R.id.imageButton6));
        buttons.add(findViewById(R.id.imageButton7));
        buttons.add(findViewById(R.id.imageButton8));
        buttons.add(findViewById(R.id.imageButton9));
        buttons.add(findViewById(R.id.imageButton10));
        buttons.add(findViewById(R.id.imageButton11));
        buttons.add(findViewById(R.id.imageButton12));
        buttons.add(findViewById(R.id.imageButton13));
        buttons.add(findViewById(R.id.imageButton14));
        buttons.add(findViewById(R.id.imageButton15));
        buttons.add(findViewById(R.id.imageButton16));
        buttons.add(findViewById(R.id.imageButton17));
        buttons.add(findViewById(R.id.imageButton18));
        buttons.add(findViewById(R.id.imageButton19));
        buttons.add(findViewById(R.id.imageButton20));

        Collections.shuffle(buttons, new Random());

        hm = new HashMap<>();
        matchedCards = new HashSet<>();
        isBoardLocked = false;
        firstCard = null;

        for (int i = 0; i < buttons.size(); i++) {
            final int img_id = i % img_urls.size();
            final ImageButton card = buttons.get(i);
            hm.put(card, new Card(img_id, true));

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!canFlipCard(card)) return;

                    // Play Flip Sound
                    if (flipSound == null) {
                        flipSound = MediaPlayer.create(Game5x4Activity.this, R.raw.flip_sound);
                    }
                    flipSound.start();

                    flips.setText("Flips: " + (++flip_count));

                    if (firstCard == null) {
                        firstCard = card;
                        Picasso.get().load(img_urls.get(hm.get(firstCard).img_id)).into(firstCard);
                        return;
                    }

                    if (hm.get(firstCard).img_id == hm.get(card).img_id) {
                        Picasso.get().load(img_urls.get(hm.get(card).img_id)).into(card);
                        processCardMatch(firstCard, card);
                    } else {
                        processCardNoMatch(firstCard, card);
                    }
                }

                private boolean canFlipCard(ImageButton card) {
                    return !isBoardLocked && (card != firstCard) && !matchedCards.contains(card);
                }

                private void processCardMatch(ImageButton card1, ImageButton card2) {
                    matchedCards.add(card1);
                    matchedCards.add(card2);
                    card1.setEnabled(false);
                    card2.setEnabled(false);
                    matches.setText("Matches: " + (matchedCards.size() / 2));

                    if (matchSound != null) {
                        matchSound.release();
                        matchSound = null;
                    }

                    // Play Match Sound
                    matchSound = MediaPlayer.create(Game5x4Activity.this, R.raw.match_sound);
                    matchSound.start();

                    // Glow Effect
                    card1.animate().alpha(0.5f).setDuration(200).withEndAction(() -> card1.animate().alpha(1f).setDuration(200));
                    card2.animate().alpha(0.5f).setDuration(200).withEndAction(() -> card2.animate().alpha(1f).setDuration(200));

                    firstCard = null;

                    // Check if all pairs are matched
                    if (matchedCards.size() == buttons.size()) {
                        endGame();
                    }
                }

                private void processCardNoMatch(ImageButton card1, ImageButton card2) {
                    isBoardLocked = true;
                    firstCard = card1;
                    secondCard = card2;

                    // Load second card image before hiding both
                    Picasso.get().load(img_urls.get(hm.get(secondCard).img_id)).into(secondCard);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            firstCard.setImageResource(0);
                            secondCard.setImageResource(0);
                            firstCard = null;
                            secondCard = null;
                            isBoardLocked = false;
                        }
                    }, 1000);
                }
                private void endGame() {
                    // Stop the timer
                    timerHandler.removeCallbacks(timerRunnable);

                    if (backgroundMusic != null) {
                        backgroundMusic.stop();
                        backgroundMusic.release();
                        backgroundMusic = null;
                    }

                    if (flipSound != null) {
                        flipSound.release();
                        flipSound = null;
                    }

                    if (matchSound != null) {
                        matchSound.release();
                        matchSound = null;
                    }

                    // Play Winning Sound
                    winningSound = MediaPlayer.create(Game5x4Activity.this, R.raw.winning_sound);
                    winningSound.start();

                    // Get final time
                    int minutes = seconds / 60;
                    int secs = seconds % 60;
                    String finalTime = String.format("%d:%02d", minutes, secs);

                    // Launch GameOverActivity
                    Intent intent = new Intent(Game5x4Activity.this, GameOverActivity.class);
                    intent.putExtra("TOTAL_FLIPS", flip_count);
                    intent.putExtra("TIME_TAKEN", finalTime);
                    startActivity(intent);
                    finish(); // Close this activity
                }

            });
        }
    }
    @Override
    public void onTaskComplete(String result) {
        extractImageUrls(result);
        buttons = new ArrayList<>();
        initializeBoard(buttons);

        // Shuffle logic
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ImageButton b : buttons) {
                    Random random = new Random();
                    int idx = random.nextInt(buttons.size());
                    if (!matchedCards.contains(b) && !matchedCards.contains(buttons.get(idx))) {
                        Card temp = hm.get(b);
                        hm.put(b, hm.get(buttons.get(idx)));
                        hm.put(buttons.get(idx), temp);
                    }
                }
            }
        });
    }
}
