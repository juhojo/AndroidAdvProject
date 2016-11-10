package com.example.juho.unigamesproject;

import android.os.AsyncTask;

/**
 * Created by Juho on 12.10.2016.
 */
public class BetTask extends AsyncTask<String, String, String> {
    public BetResponse delegate = null;
    private Bet betObject;

    BetTask(Bet betObject) {
        this.betObject = betObject;
    }

    @Override
    protected String doInBackground(String... params) {
        String message = "Failed";

        if (betObject != null) {
            message = HttpManager.setBet(betObject);
        }
        return message;
    }
    @Override
    protected void onPostExecute(String result) { delegate.betProcessFinish(result); }
}