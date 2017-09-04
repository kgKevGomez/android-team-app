package com.example.android.teamsDemoApp.services;

import com.example.android.teamsDemoApp.model.Team;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

/**
 * Created by kevin on 9/4/2017.
 */

public interface TeamService {
    @GET("/external/applaudo_homework.json")
    Call<ArrayList<Team>> getTeams();
}
