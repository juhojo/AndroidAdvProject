package com.example.juho.unigamesproject;

/**
 * Created by Juho on 12.10.2016.
 */
public class Bet {

    String username;
    String gameId;
    String team;
    int bet;

    public Bet(String username, String gameId, String team, int bet) {
        this.username = username;
        this.gameId = gameId;
        this.team = team;
        this.bet = bet;
    }

    public String getUsername() {
        return this.username;
    }

    public String getGameId() {
        return this.gameId;
    }

    public String getTeam() {
        return this.team;
    }

    public int getBet() {
        return this.bet;
    }
}
