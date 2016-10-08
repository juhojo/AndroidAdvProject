package com.example.juho.unigamesproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Juho on 2.10.2016.
 */
public class ToornamentTask extends AsyncTask <String, String, JSONArray> {
    public AsyncResponse delegate = null;

    @Override
    protected JSONArray doInBackground(String... params) {
        String action = params[0];
        String type = params[1];
        JSONArray jsonArray = null;
        String json;

        if (type.equals("schedule")) {
            json = HttpManager.getToornament("schedule");
            try {
                JSONObject jObject = new JSONObject(json);
                jsonArray = jObject.getJSONArray("schedule");
                System.out.println(jsonArray + ", this is schedule array!");
            } catch (JSONException e) {
                System.out.println("Not valid json");
                e.printStackTrace();
            }
        } else {
            json = HttpManager.getToornament("scores");
            jsonArray = getScores(json, type);
        }

        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        delegate.ttProcessFinish(jsonArray);
    }

    private JSONArray getScores(String json, String type) {
        try {
            JSONObject jObject = new JSONObject(json);
            JSONObject scores = jObject.getJSONObject("scores");

            JSONArray jArray = scores.getJSONArray(type.toLowerCase());

            return jArray;

        } catch (JSONException e) {
            System.out.println("Not valid json");
            e.printStackTrace();
        }
        return null;
    }

}
