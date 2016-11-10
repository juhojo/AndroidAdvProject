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

    private static HttpURLConnection tryConnect(String uri) {
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static StringBuilder readLines(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (Exception e) {
            return null;
        }

        return sb;
    }

    private static String generateMessage(HttpURLConnection con) {
        try {
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String line;
                String phpMessage = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    phpMessage += line; // Message if sign up was successful
                }
                return phpMessage;
            }
        } catch (Exception e) {
            // Something went wrong
        }
        return null;
    }

    private static void postRequestPHP(HttpURLConnection con, JSONObject jsonObject) {
        try {
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();

            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.flush();
            os.close();
        } catch (Exception e) {
            // Something went wrong
        }
    }

    // GETS ------------------------------------------------------------ GETS //
    public static String get (String action) {

        BufferedReader reader = null;
        String uri = Variables.URL_USERS_JSON;

        try {
            HttpURLConnection con = tryConnect(uri);
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = readLines(reader);

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
    public static JSONObject getBets(String username) {

        String uri = Variables.URL_GET_BETS;

        // Make JSON Object that will be sent to php
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection con = tryConnect(uri);
            postRequestPHP(con, jsonObject);
            if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String line;
                String phpMessage = "";
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    phpMessage += line; // Message if sign up was successful
                }
                try {
                    JSONObject helperObj = new JSONObject(phpMessage);
                    JSONObject returnObj = new JSONObject(helperObj.get("0").toString());
                    return returnObj;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static String getToornament(String searchType) {

        BufferedReader reader = null;
        String uri = searchType.equals("scores") ? Variables.URL_GET_SCORES : Variables.URL_GET_SCHEDULE;

        try {
            HttpURLConnection con = tryConnect(uri);
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = readLines(reader);
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
            HttpURLConnection con = tryConnect(Variables.URL_SIGN_UP);
            postRequestPHP(con, jsonObject);
            return generateMessage(con);
        } catch (Exception e) {
            e.printStackTrace();
            return "Sign up failed!";
        }
    }

    public static String setBet(Bet betObject) {
        String username = betObject.getUsername();
        String gameId = betObject.getGameId();
        String team = betObject.getTeam();
        int bet = betObject.getBet();
        String message = "";

        // Make JSON Object that will be sent to php
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", username);
            jsonObject.put("id", gameId);
            jsonObject.put("team", team);
            jsonObject.put("bet", bet);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection con = tryConnect(Variables.URL_SET_BETS);
            postRequestPHP(con, jsonObject);
            message = generateMessage(con);
        } catch (Exception e) {
            e.printStackTrace();
            return "Bet add failed!";
        }
        return message;
    }

}