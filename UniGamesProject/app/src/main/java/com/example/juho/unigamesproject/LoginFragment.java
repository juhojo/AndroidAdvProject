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
public class LoginFragment extends Fragment {

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
        final RelativeLayout myView = (RelativeLayout) inflater.inflate(R.layout.fragment_login, container, false);
        final Button login = (Button)myView.findViewById(R.id.loginButton);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                System.out.println("Button clicked");
                EditText loginTxt = (EditText)myView.findViewById(R.id.loginTxt);
                String loginString = loginTxt.getText().toString();
                _mClickListener.onLogInClick(loginString);
            }
        });

        return myView;
    }

}
