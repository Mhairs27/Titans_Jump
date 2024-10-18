package com.example.titans_jump;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("GamePrefs", MODE_PRIVATE);

        String savedUsername = sharedPreferences.getString("username", null);

        if (savedUsername != null) {
            // If username is already saved, navigate to GameActivity
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("username", savedUsername);
            startActivity(intent);
            finish();
        } else {
            // If no username is saved, navigate to LoginActivity
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
