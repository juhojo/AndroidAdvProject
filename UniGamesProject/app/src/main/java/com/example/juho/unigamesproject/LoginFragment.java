package com.example.juho.unigamesproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by Juho on 23.9.2016
 */
public class LoginFragment extends Fragment {
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
        final RelativeLayout myView = (RelativeLayout) inflater.inflate(R.layout.fragment_login, container, false);
        final Button login = (Button)myView.findViewById(R.id.loginButton);
        final EditText loginTxt = (EditText)myView.findViewById(R.id.loginTxt);

        // Preferences, we use to load standard username
        SharedPreferences preferences = getActivity().getSharedPreferences(Variables.MyPREFERENCES, Context.MODE_PRIVATE);
        String standardUn = preferences.getString(Variables.Username, null);

        if(standardUn != null) {
            loginTxt.setText(standardUn);
        }

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Prevent user from spamming "Log in"
                if (canBeClicked) {

                    String loginString = loginTxt.getText().toString();
                    _mClickListener.onLogInClick(loginString);

                    canBeClicked = false;

                    //Allow user to try log in again after 2 second
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            canBeClicked = true;
                        }
                    }, 2000);
                }
            }
        });

        return myView;
    }

}
