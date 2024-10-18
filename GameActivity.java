package com.example.titans_jump;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.titans_jump.api.ApiClient;
import com.example.titans_jump.api.ApiInterface;
import com.example.titans_jump.model.BestScoreResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameActivity extends AppCompatActivity {
    private GameView gameView;
    private static final String PREFS_NAME = "GamePrefs";
    private static final String BEST_SCORE_KEY = "best_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this, null);
        setContentView(gameView);

        String username = "your_username"; // Replace with actual username
        fetchBestScore(username); // Fetch best score on activity creation
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.startGame();

        // Load the best score from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int bestScore = preferences.getInt(BEST_SCORE_KEY, 0);
        gameView.setBestScore(bestScore);
    }

    private void fetchBestScore(String username) {
        ApiInterface apiInterface = ApiClient.getApi();
        Call<BestScoreResponse> call = apiInterface.getBestScore(username);

        call.enqueue(new Callback<BestScoreResponse>() {
            @Override
            public void onResponse(Call<BestScoreResponse> call, Response<BestScoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int bestScore = response.body().getBestScore();
                    gameView.setBestScore(bestScore); // Update GameView with the best score
                } else {
                    Toast.makeText(GameActivity.this, "Failed to fetch best score", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BestScoreResponse> call, Throwable t) {
                Toast.makeText(GameActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.stopGame();

        // Save the best score to SharedPreferences
        saveBestScore(gameView.getScore());
    }

    private void saveBestScore(int currentScore) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int bestScore = preferences.getInt(BEST_SCORE_KEY, 0);
        if (currentScore > bestScore) {
            editor.putInt(BEST_SCORE_KEY, currentScore);
            editor.apply(); // Save the best score
        }
    }
}
