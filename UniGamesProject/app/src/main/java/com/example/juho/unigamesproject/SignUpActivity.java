package com.example.juho.unigamesproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Button signUp = (Button)findViewById(R.id.signupButton);


        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                EditText username = (EditText)findViewById(R.id.userNameTxt);
                EditText team = (EditText)findViewById(R.id.teamNameTxt);
                String unString = username.getText().toString();
                String teamString = team.getText().toString();

                if (unString.length() > 0 && teamString.length() > 0) {
                    new DBTask().execute(unString, teamString, "signUp");
                } else {
                    System.out.println("Show error that you need to give inputs");
                }
            }
        });
    }
}
