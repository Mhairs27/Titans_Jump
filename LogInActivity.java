package com.example.titans_jump;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast; // Import Toast for displaying messages
import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText usernameEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up); // Ensure this layout has the necessary views

        sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);
        usernameEditText = findViewById(R.id.username_input);
        submitButton = findViewById(R.id.enter_btn);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim(); // Trim to remove whitespace
                if (!username.isEmpty()) {
                    saveUsername(username);
                } else {
                    // Show a message if username is empty
                    Toast.makeText(LogInActivity.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUsername(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();

        // Navigate to MainActivity and pass the username
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("USERNAME", username); // Pass the username
        startActivity(intent);
        finish(); // Call finish() if you don't want to return to this activity
    }
}
