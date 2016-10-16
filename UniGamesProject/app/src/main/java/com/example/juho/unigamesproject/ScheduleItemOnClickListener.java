package com.example.juho.unigamesproject;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Juho on 12.10.2016.
 */
public class ScheduleItemOnClickListener implements View.OnClickListener, BetResponse {

    View view;
    String username;
    String gameId;
    String team;
    int bet;
    Bet betObject;

    public ScheduleItemOnClickListener(String username, String gameId, String team, int bet) {
        this.username = username;
        this.gameId = gameId;
        this.team = team;
        this.bet = bet;

        betObject = new Bet(this.username, this.gameId, this.team, this.bet);
    }

    @Override
    public void onClick(View v) {
        BetTask betTask = new BetTask(betObject);
        betTask.execute();
        betTask.delegate = this;
        view = v;
    }

    @Override
    public void betProcessFinish(String response) {
        if (response.equals("Success!")) {
            TextView otherTeam = (TextView)view.getTag();
            ((TextView) view).setTextColor(ContextCompat.getColor(view.getContext(), R.color.icons)); // Update to selected
            otherTeam.setTextColor(ContextCompat.getColor(view.getContext(), R.color.primary_text)); // Update to unselected
        } else {
            System.out.println("Something went wrong");
        }
    }
}
