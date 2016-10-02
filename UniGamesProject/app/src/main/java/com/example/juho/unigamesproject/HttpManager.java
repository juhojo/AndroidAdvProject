package com.example.juho.unigamesproject;

/**
 * Created by Juho on 12.9.2016.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpManager {

    // GETS ------------------------------------------------------------ GETS //
    public static String get (String action) {

        BufferedReader reader = null;
        String uri = Variables.URL_USERS_JSON;

        try {
            URL url = new URL(uri);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            if (action.equals("all") || action.equals("users")) {
                return sb.toString();
            } else if (action.equals("ids")) {
                String lastId = getLastId(sb.toString());
                return lastId;
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }
    public static String getScores() {

        BufferedReader reader = null;
        String uri = Variables.URL_GET_SCORES;

        try {
            URL url = new URL(uri);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    private static String getLastId(String json) {
        try {
            JSONObject jObject = new JSONObject(json);
            JSONArray jArray = jObject.getJSONArray("accounts");
            return Integer.toString(jArray.length() + 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "0";
    }

    // POSTS ------------------------------------------------------------ POSTS //
    public static String signUp (String username, String team, String id) {

        // Make JSON Object that will be sent to php
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", id);
            jsonObject.put("name", username);
            jsonObject.put("team", team);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            URL url = new URL(Variables.URL_SIGN_UP);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();

            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                String line;
                String phpMessage = "";
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line=br.readLine()) != null) {
                    phpMessage+=line; // Message if sign up was succesful
                }
                System.out.println(phpMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Sign up failed!";
        }
        return "Success!";
    }

}