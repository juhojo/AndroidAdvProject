package com.example.juho.unigamesproject;

/**
 * Created by Juho on 16.9.2016.
 */
public class User {
    private String username;
    private String team;
    private int id;

    public User() {
    }

    // Getters
    public String getUsername() {
        return this.username;
    }

    public String getTeam() {
        return this.team;
    }

    public int getId() {
        return this.id;
    }

    // Setters
    public void setUsername(String name) {
        System.out.println("Got here: " + name);
        this.username = name;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

}
