package com.example.juho.unigamesproject;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by Juho on 2.10.2016.
 */
public class ToornamentTask extends AsyncTask <String, String, JSONArray> {
    public AsyncResponse delegate = null;
    String userName = null;

    @Override
    protected JSONArray doInBackground(String... params) {
        String action = params[0];
        String type = params[params.length - 1];
        JSONArray jsonArray = null;
        String json;

        if (type.equals("schedule")) {
            json = HttpManager.getToornament("schedule");
            userName = params[1];
            JSONObject bets = HttpManager.getBets(userName);
            if (json != null) {
                try {
                    JSONObject jObject = new JSONObject(json);

                    // This is a fix to PHP returning an object instead of an array (just PHP things):
                    if (jObject.get("schedule") instanceof JSONArray) {
                        jsonArray = jObject.getJSONArray("schedule");
                    } else {
                        jsonArray = new JSONArray();
                        JSONObject scheduleObject = jObject.getJSONObject("schedule").getJSONObject("1");
                        jsonArray.put(scheduleObject);
                    }
                    if (bets != null) {
                        jsonArray = addBets(jsonArray, bets);
                    }
                } catch (JSONException e) {
                    System.out.println("Not valid json");
                    e.printStackTrace();
                }
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

    private JSONArray addBets(JSONArray schedule, JSONObject bets) {
        try {
            for (int i=0; i < schedule.length(); i++) {
                JSONObject scheduleObject = schedule.getJSONObject(i);

                Iterator<?> keys = bets.keys();
                while (keys.hasNext()) {
                    String key = (String)keys.next();
                    if (scheduleObject.get("id").equals(key)) {
                        scheduleObject.put("bet", bets.get(key));
                    }
                }
            }
        } catch (JSONException e) {
            System.out.println("Not valid json");
            e.printStackTrace();
        }
        return schedule;
    }

}
