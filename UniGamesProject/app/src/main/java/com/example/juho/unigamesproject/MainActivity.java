package com.example.juho.unigamesproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private UDgetTask asyncTask = new UDgetTask();
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

        // If activity change was done correctly give user details
        if (extras != null) {
            username = extras.getString("username");
            team = extras.getString("team");

            user.setUsername(username);
            user.setTeam(team);
            asyncTask.execute(username, "getId");
        }

        textView.setText(user.getUsername());

        // Delegate back to this class
        asyncTask.delegate = this;
    }

    @Override
    public void processFinish(String output, String un, String team){
        // Override interface method
    }
    @Override
    public void udProcessFinish(String output, String id) {
        if (output.equals("Success!")) {
            user.setId(id);
        } else {
            user.setUsername(null);
            user.setTeam(null);

            Intent intent = new Intent(this, LoginActivity.class);

            // Forward user's data
            intent.putExtra("error", output);
            startActivity(intent);
        }
    }
}
