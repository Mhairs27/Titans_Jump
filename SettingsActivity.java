package com.example.titans_jump;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private RadioButton onMusic, offMusic, onSound, offSound;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        // Initialize RadioButtons and preferences
        onMusic = findViewById(R.id.on_music);
        offMusic = findViewById(R.id.off_music);
        onSound = findViewById(R.id.on_sound);
        offSound = findViewById(R.id.off_sound);
        ImageView homeIcon = findViewById(R.id.home_icon);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Load saved preferences for music and sound
        boolean isMusicOn = sharedPreferences.getBoolean("music", true);
        boolean isSoundOn = sharedPreferences.getBoolean("sound", true);
// Set RadioButton states based on saved preferences
        onMusic.setChecked(isMusicOn);
        offMusic.setChecked(!isMusicOn);
        onSound.setChecked(isSoundOn);
        offSound.setChecked(!isSoundOn);

        // Set listeners for Music RadioButtons
        onMusic.setOnClickListener(v -> {
            editor.putBoolean("music", true);
            editor.apply();
        });

        offMusic.setOnClickListener(v -> {
            editor.putBoolean("music", false);
            editor.apply();
        });

        // Set listeners for Sound RadioButtons
        onSound.setOnClickListener(v -> {
            editor.putBoolean("sound", true);
            editor.apply();
        });

        offSound.setOnClickListener(v -> {
            editor.putBoolean("sound", false);
            editor.apply();
        });

        // Handle Home icon click to navigate back to the main activity
        homeIcon.setOnClickListener(v -> finish());
    }
}