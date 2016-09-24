package com.example.juho.unigamesproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Juho on 24.9.2016.
 */
public class UDgetTask extends AsyncTask<String, String, String> {
    public AsyncResponse delegate = null;
    private String id = null;

    protected String getId(String json, String username) {
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
                        return oneObject.getString("id");
                    }
                } catch (JSONException e) {
                    // Oops
                }
            }

        } catch (JSONException e) {
            System.out.println("Not valid json");
            e.printStackTrace();
        }

        return "0";
    }

    @Override
    protected String doInBackground(String... params) {
        // Last given parameter is always the action
        String action = params[params.length - 1];

        if(action.equals("getId")) {
            String usersJson = HttpManager.get("users");
            System.out.println(usersJson);

            // Validate if user exists in database
            boolean found = Methods.isRegistered(usersJson, params[0]);

            if (found) {
                id = getId(usersJson, params[0]);
                return "Success!";
            }
        }
        return "User could not be found, please try again.";
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("Done with result: \n" + result);
        delegate.udProcessFinish(result, id);
    }
}
