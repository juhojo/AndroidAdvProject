package com.example.juho.unigamesproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by Juho on 12.9.2016.
 */

public class DBTask extends AsyncTask<String, String, String> {
    public AsyncResponse delegate = null;
    private String un = null;
    private String team = null;

    protected String getTeam(String json, String username) {
        String team = "No team";
        try {

            JSONObject jObject = new JSONObject(json);
            JSONArray jArray = jObject.getJSONArray("accounts");

            for (int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling name from the array
                    String name = oneObject.getString("name");
                    if (name.equals(username)) {
                        team = oneObject.getString("team");
                    }
                } catch (JSONException e) {
                    // Oops
                }
            }

        } catch (JSONException e) {
            System.out.println("Not valid json");
            e.printStackTrace();
        }

        return team;
    }

    @Override
    protected String doInBackground(String... params) {
        // Last given parameter is always the action
        String action = params[params.length - 1];
        String message = "No internet access, try again later.";

        if(action.equals("login") || action.equals("signUp")) {
            String usersJson = HttpManager.get("users");

            // Internet access check
            if (usersJson != null) {

                // Validate if user exists in database
                boolean found = Methods.isRegistered(usersJson, params[0]);

                if (action.equals("login")) {
                    if (found) {
                        message = "Success!";
                        un = params[0];
                        team = getTeam(usersJson, params[0]);
                    } else {
                        message = "Login failed, user not found!";
                    }
                } else if (action.equals("signUp")) {
                    if (found) {
                        message = "Sign up failed, user with that username already exists.";
                    } else {
                        String lastId = HttpManager.get("ids");
                        // Post to database
                        message = HttpManager.signUp(params[0], params[1], lastId);
                        un = params[0];
                        team = params[0];
                    }
                }
            }
        }

        return message;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result, un, team);
    }

    @Override
    protected void onProgressUpdate(String... values) {}
}
