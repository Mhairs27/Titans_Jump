package com.example.titans_jump.api;

import com.example.titans_jump.model.BestScoreResponse;
import com.example.titans_jump.model.ScoreRequest;
import com.example.titans_jump.model.ScoreResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("best-score/{username}") // Correct endpoint
    Call<BestScoreResponse> getBestScore(@Path("username") String username);

    @POST("submit-score") // Correct endpoint
    Call<ScoreResponse> submitScore(@Body ScoreRequest scoreRequest);

    @PUT("update-score/{username}") // New endpoint for updating score
    Call<ScoreResponse> updateScore(@Path("username") String username, @Body ScoreRequest scoreRequest);
}
