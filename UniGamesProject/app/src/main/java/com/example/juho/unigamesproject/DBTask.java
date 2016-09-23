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
    private boolean found = false;
    public AsyncResponse delegate = null;

    protected boolean isRegistered(String json, String username) {

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
                        found = true;
                    }
                } catch (JSONException e) {
                    // Oops
                }
            }

        } catch (JSONException e) {
            System.out.println("Not valid json");
            e.printStackTrace();
        }

        return found;
    }

    @Override
    protected String doInBackground(String... params) {
        // Last given parameter is always the action
        String action = params[params.length - 1];
        String message = "";

        if(action.equals("login") || action.equals("signUp")) {
            String usersJson = HttpManager.get("users");
            System.out.println("JSON:" + usersJson);

            // Validate if user exists in database

            found = isRegistered(usersJson, params[0]);

            if (action.equals("login")) {
                if (found) {
                    message = "Success!";

                    // TODO Initialize User
                    // Create user
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

                    // TODO Initialize User
                    // Create user
                }
            }
        }

        return message;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("Done with result: \n" + result);
        delegate.processFinish(result);
    }

    @Override
    protected void onPreExecute() {
        System.out.println("Starting");
    }

    @Override
    protected void onProgressUpdate(String... values) {}
}
