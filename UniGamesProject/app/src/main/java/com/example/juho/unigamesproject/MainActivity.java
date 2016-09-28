package com.example.juho.unigamesproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AsyncResponse, MAFragment.OnFragmentInteractionListener {
    private UDgetTask asyncTask = new UDgetTask();
    boolean doubleBackToExitPressedOnce = false;
    TextView textView;
    User user;

    // Navigation
    DrawerLayout mDrawerLayout;
    Boolean mSlideState;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar -----------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Drawable icon = ResourcesCompat.getDrawable(getResources(), R.drawable.unigames_hamburger, null);
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        // Scale it to 50 x 50
        Drawable scaledIcon = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
        // Set your new, scaled drawable "d"
        toolbar.setNavigationIcon(scaledIcon);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSlideState){
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }else{
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        // Drawer ------------------------------------------------
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mSlideState = false;
        mDrawerLayout.addDrawerListener(new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                0,
                0){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mSlideState = false; // is Closed
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mSlideState = true; // is Opened
            }
        });

        // Initialize the user -----------------------------------
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

    // Exit application if user taps "Back" twice ----------------
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to logout", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment;
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            default:
            case 0:
                fragment = new MAFragment();
                break;
            case 1:
                fragment = new MAFragment();
                break;
            case 2:
                fragment = new MAFragment();
                break;
            case 3:
                fragment = new MAFragment();
                break;
        }


        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

    }

    @Override
    public void setTitle(CharSequence title) {
        /*
        mTitle = title;
        getActionBar().setTitle(mTitle);
        */
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }
}
