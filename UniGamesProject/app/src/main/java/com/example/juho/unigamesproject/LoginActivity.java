package com.example.juho.unigamesproject;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements AsyncResponse, OnLogInSignUpClick {
    // Instance variables
    MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
    private LoginFragment loginFragment = new LoginFragment();
    private SignUpFragment signUpFragment = new SignUpFragment();
    private DBTask asyncTask = new DBTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Pager
        ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.fixed_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Delegate back to this class
        asyncTask.delegate = this;

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

    @Override
    public void onLogInClick(String username) {
        asyncTask.execute(username.toLowerCase(), "login");
    }
    @Override
    public void onSignUpClick(String username, String team) {
        asyncTask.execute(username.toLowerCase(), team.toLowerCase(), "signUp");
    }

    // Inner class of Activity
    class MyPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{"Log in!", "Sign up!"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return loginFragment;
                case 1:
                    return signUpFragment;
            }
            return null;
        }

        @Override
        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    }
}
