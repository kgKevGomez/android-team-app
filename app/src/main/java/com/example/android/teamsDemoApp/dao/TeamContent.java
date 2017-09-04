package com.example.android.teamsDemoApp.dao;

import com.example.android.teamsDemoApp.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TeamContent {
    public static final List<Team> ITEMS = new ArrayList<>();
    public static final Map<Integer, Team> ITEM_MAP = new HashMap<>();
    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createTeamItem(i));
        }
    }

    private static void addItem(Team item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    private static Team createTeamItem(int position) {
        return new Team(position, "Item " + position, makeDetails(position), "Address " + position);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore description information here.");
        }
        return builder.toString();
    }



}
