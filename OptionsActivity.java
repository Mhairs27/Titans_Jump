package com.example.titans_jump;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OptionsActivity extends Activity {

    private Button btnPrevious, btnNext, btnSelect;
    private ImageView characterDisplay;
    private TextView characterName;
    private ImageButton homeIcon, settingsIcon;

    // Array of character images and names
    private int[] characterImages = {
            R.drawable.character1,
            R.drawable.character2,
            R.drawable.character3,
            R.drawable.character4,
            R.drawable.character5
    };
    private String[] characterNames = {"MHAIRS", "CHARLS", "ABEGAIL", "JAYSON", "KATE"};

    // Current character index
    private int currentCharacterIndex = 0;

    // SharedPreferences for saving the selected character
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_activity);

        // Initialize views
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        btnSelect = findViewById(R.id.select_btn);
        characterDisplay = findViewById(R.id.character_display);
        characterName = findViewById(R.id.character_name);
        homeIcon = findViewById(R.id.home_icon); // Home icon button
        settingsIcon = findViewById(R.id.setting_icon); // Settings icon button

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);

        // Set initial character
        updateCharacterDisplay();

        // Next button action: show the next character
        btnNext.setOnClickListener(v -> {
            currentCharacterIndex = (currentCharacterIndex + 1) % characterImages.length;
            updateCharacterDisplay();
        });

        // Previous button action: show the previous character
        btnPrevious.setOnClickListener(v -> {
            currentCharacterIndex = (currentCharacterIndex - 1 + characterImages.length) % characterImages.length;
            updateCharacterDisplay();
        });

        // Select button action: save the character and navigate to GameActivity
        btnSelect.setOnClickListener(v -> {
            String selectedCharacter = characterNames[currentCharacterIndex];
            saveSelectedCharacter(currentCharacterIndex); // Save selected character index
            Toast.makeText(this, "Selected: " + selectedCharacter, Toast.LENGTH_SHORT).show();

            // Pass selected character index to GameActivity
            Intent intent = new Intent(OptionsActivity.this, GameActivity.class);
            intent.putExtra("selected_character_index", currentCharacterIndex); // Pass character index
            startActivity(intent);
            finish(); // Optional: Finish current activity so it can't be returned to
        });

        // Home icon action: navigate to MainActivity
        homeIcon.setOnClickListener(v -> {
            Intent intent = new Intent(OptionsActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish current activity
        });

        // Settings icon action: navigate to SettingsActivity
        settingsIcon.setOnClickListener(v -> {
            Intent intent = new Intent(OptionsActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    // Method to update the displayed character image and name
    private void updateCharacterDisplay() {
        characterDisplay.setImageResource(characterImages[currentCharacterIndex]);
        characterName.setText(characterNames[currentCharacterIndex]);
    }

    // Method to save the selected character index to SharedPreferences
    private void saveSelectedCharacter(int characterIndex) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("selected_character_id", characterIndex); // Save selected character index
        editor.apply(); // Apply changes
    }
}
