package com.example.juho.unigamesproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Juho on 24.9.2016.
 */
public class Methods {

    public static boolean isRegistered(String json, String username) {

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
                        return true;
                    }
                } catch (JSONException e) {
                    // Oops
                }
            }

        } catch (JSONException e) {
            System.out.println("Not valid json");
            e.printStackTrace();
        }

        return false;
    }
}
