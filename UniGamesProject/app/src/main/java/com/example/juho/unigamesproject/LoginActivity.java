package com.example.juho.unigamesproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements AsyncResponse {
    private DBTask asyncTask = new DBTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Delegate back to this class
        asyncTask.delegate = this;

        final Button login = (Button)findViewById(R.id.loginButton);
        final Button signUp = (Button)findViewById(R.id.signupButton);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                System.out.println("Button clicked");
                EditText loginTxt = (EditText)findViewById(R.id.loginTxt);
                String loginString = loginTxt.getText().toString();
                /*
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
                */

                asyncTask.execute(loginString.toLowerCase(), "login");
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                System.out.println("Sign up! - Button clicked");

                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);

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
