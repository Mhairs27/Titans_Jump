package com.example.titans_jump;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool; // Import SoundPool
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView; // Import TextView to display the username
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Button optionsButton;
    private Button settingsButton;
    private TextView welcomeTextView; // Add TextView for displaying the welcome message
    private SoundPool soundPool; // Declare SoundPool
    private int soundId; // Declare variable to hold the sound ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity); // Ensure this matches your XML layout file name

        // Initialize views
        startButton = findViewById(R.id.start_btn);
        optionsButton = findViewById(R.id.opt_btn);
        settingsButton = findViewById(R.id.setting_icon);
        welcomeTextView = findViewById(R.id.username_text); // Initialize the TextView for the username

        // Retrieve the username from the intent
        String username = getIntent().getStringExtra("USERNAME");
        if (username != null) {
            welcomeTextView.setText("Welcome, " + username + "!"); // Display the welcome message
        }

        // Initialize SoundPool
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1) // Set max streams to 1 for button clicks
                .setAudioAttributes(audioAttributes)
                .build();

        // Load the sound file
        soundId = soundPool.load(this, R.raw.clicking, 1); // Ensure this matches your resource

        // Navigate to GameActivity when start button is clicked
        startButton.setOnClickListener(v -> {
            soundPool.play(soundId, 1, 1, 0, 0, 1); // Play the sound when button is clicked
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("USERNAME", username); // Pass the username to GameActivity
            startActivity(intent);
        });

        // Navigate to OptionsActivity when options button is clicked
        optionsButton.setOnClickListener(v -> {
            soundPool.play(soundId, 1, 1, 0, 0, 1); // Play the sound when button is clicked
            Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
            startActivity(intent);
        });

        // Navigate to SettingsActivity when settings icon is clicked
        settingsButton.setOnClickListener(v -> {
            soundPool.play(soundId, 1, 1, 0, 0, 1); // Play the sound when icon is clicked
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release(); // Release SoundPool resources
            soundPool = null;
        }
    }
}
