package com.example.android.teamsDemoApp.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kevin on 9/4/2017.
 */

public class RestClient{
    private static RestClient instance;
    private static final String BASE_URL = "http://applaudostudios.com";
    private TeamService teamService;

    public RestClient(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss' 'Z")
                .create();

        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();

        teamService = retrofit.create(TeamService.class);
    }

    public static RestClient getInstance(){
        if (instance == null)
            instance = new RestClient();

        return instance;
    }

    public TeamService getTeamService() {
        return teamService;
    }
}
