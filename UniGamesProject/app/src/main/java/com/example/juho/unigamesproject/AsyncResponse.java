package com.example.juho.unigamesproject;

/**
 * Created by Juho on 19.9.2016.
 */
public interface AsyncResponse {
    void processFinish(String output, String un, String team);
    void udProcessFinish(String output, String id);
}