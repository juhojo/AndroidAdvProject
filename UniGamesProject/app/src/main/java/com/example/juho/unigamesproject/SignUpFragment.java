package com.example.juho.unigamesproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Juho on 23.9.2016
 */
public class SignUpFragment extends Fragment {
    private boolean canBeClicked = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    OnLogInSignUpClick _mClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            _mClickListener = (OnLogInSignUpClick)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onDogChange");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final RelativeLayout myView = (RelativeLayout) inflater.inflate(R.layout.fragment_sign_up, container, false);
        final Button signUp = (Button)myView.findViewById(R.id.signupButton);

        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Prevent user from spamming "Sign up"
                if (canBeClicked) {
                    EditText username = (EditText) myView.findViewById(R.id.userNameTxt);
                    EditText team = (EditText) myView.findViewById(R.id.teamNameTxt);
                    String unString = username.getText().toString().replaceAll(" ", "_");
                    String teamString = team.getText().toString().replaceAll(" ", "_");
                    
                    if (unString.length() > 0 && teamString.length() > 0) {
                        _mClickListener.onSignUpClick(unString, teamString);
                        canBeClicked = false;

                        //Allow user to try log in again after 2 second
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                canBeClicked = true;
                            }
                        }, 2000);

                    } else {
                        Toast.makeText(myView.getContext(), R.string.provide_all_inputs, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return myView;
    }

}
