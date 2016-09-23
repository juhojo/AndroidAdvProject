package com.example.juho.unigamesproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity implements AsyncResponse {
    private DBTask asyncTask = new DBTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Delegate back to this class
        asyncTask.delegate = this;

        final Button signUp = (Button)findViewById(R.id.signupButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                EditText username = (EditText)findViewById(R.id.userNameTxt);
                EditText team = (EditText)findViewById(R.id.teamNameTxt);
                String unString = username.getText().toString();
                String teamString = team.getText().toString();

                if (unString.length() > 0 && teamString.length() > 0) {
                    asyncTask.execute(unString, teamString, "signUp");
                } else {
                    System.out.println("Show error that you need to give inputs");
                }
            }
        });
    }

    @Override
    public void processFinish(String output, String un, String team){
        // Catch the result from AsyncTask from our response interface
        if (output.equals("Success!")) {
            Intent intent = new Intent(this, MainActivity.class);

            // Forward user's data
            intent.putExtra("username", un);
            intent.putExtra("team", team);
            startActivity(intent);
            finish(); // Prevents users from going back to login page.
        } else {
            // If Login or Sign up fails show message
            Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
        }
    }
}
