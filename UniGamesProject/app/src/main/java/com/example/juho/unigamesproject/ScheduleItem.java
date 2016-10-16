package com.example.juho.unigamesproject;

import org.json.JSONArray;

/**
 * Created by Juho on 12.10.2016.
 */
public class ScheduleItem {

    String id;
    JSONArray teams;
    String time;
    int bet = -1; // Used to check if user has bet

    // Constructors
    public ScheduleItem() {

    }

    // Setters
    public ScheduleItem newInstance(String id, JSONArray teams, String time) {
        ScheduleItem item = new ScheduleItem();
        this.id = id;
        this.teams = teams;
        this.time = time;
        return item;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    // Getters
    public String getId() {
        return this.id;
    }

    public JSONArray getTeams() {
        return this.teams;
    }

    public String getTime() {
        return this.time;
    }

    public int getBet() {
        return this.bet;
    }
}
