package com.example.juho.unigamesproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the user
        user = new User();

        textView = (TextView)findViewById(R.id.textView);
        String username = null;
        String team = null;
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            username = extras.getString("username");
            team = extras.getString("team");
            user.setUsername(username);
            user.setTeam(team);
        }

        textView.setText(user.getUsername());

    }
}
