package com.example.juho.unigamesproject;

/**
 * Created by Juho on 16.9.2016.
 */
public class User {
    private int id;
    private String username;
    private String team;

    public User(String un, String tm, int id) {
        this.id = id;
        this.username = un;
        this.team = tm;
    }

    public String getUsername() {
        return this.username;
    }

    public String getTeam() {
        return this.team;
    }

    public int getId() { return this.id; }

}
