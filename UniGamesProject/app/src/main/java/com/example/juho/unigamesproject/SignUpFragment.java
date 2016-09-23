package com.example.juho.unigamesproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by Juho on 23.9.2016
 */
public class SignUpFragment extends Fragment {

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
                // Perform action on click

                EditText username = (EditText)myView.findViewById(R.id.userNameTxt);
                EditText team = (EditText)myView.findViewById(R.id.teamNameTxt);
                String unString = username.getText().toString();
                String teamString = team.getText().toString();

                if (unString.length() > 0 && teamString.length() > 0) {
                    _mClickListener.onSignUpClick(unString, teamString);
                } else {
                    System.out.println("Show error that you need to give inputs");
                }
            }
        });

        return myView;
    }

}
