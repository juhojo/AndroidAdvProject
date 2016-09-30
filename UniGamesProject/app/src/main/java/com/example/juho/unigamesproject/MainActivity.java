package com.example.juho.unigamesproject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AsyncResponse, OnMAFragmentInteractionListener {
    private UDgetTask asyncTask = new UDgetTask();
    boolean doubleBackToExitPressedOnce = false;
    TextView textView;
    User user;

    // Navigation
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    private ListView mDrawerList;
    Boolean mSlideState;
    Toolbar toolbar;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar -----------------------------------------------
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null); // Remove Title
        Drawable icon = ResourcesCompat.getDrawable(getResources(), R.drawable.unigames_hamburger, null);
        Drawable toornamentLogo = ResourcesCompat.getDrawable(getResources(), R.drawable.toornament_logo, null);
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        // Scale it to 50 x 50
        Drawable scaledIcon = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 50, 50, true));
        // Set your new, scaled drawable "d"
        toolbar.setNavigationIcon(scaledIcon);
        toolbar.setLogo(toornamentLogo);

        // Set onClick to toornament logo
        View logoView = toolbar.getChildAt(1);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.RIGHT);
        logoView.setLayoutParams(lp);

        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
                Uri uri = Uri.parse(Variables.URL_TOORNAMENT);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

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
        mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        Menu menu = mNavigationView.getMenu();

        setMenuItemListeners(menu);

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

        // Check that the activity has fragment_container
        if (findViewById(R.id.fragment_container) != null) {

            // If we're being restored from a previous state,
            // then we don't need to do anything and should return to prevent
            // overlapping fragments
            if (savedInstanceState != null) {
                return;
            }

            fragment = new ScheduleFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }
    }

    private void setMenuItemListeners(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            final int j = i;

            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    selectItem(j);
                    return true;
                }
            });
        }
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

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        System.out.println("Here!: " + position);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            default:
            case 0:
                fragment = new ScheduleFragment();
                break;
            case 1:
                fragment = new RankingFragment();
                break;
            case 2:
                fragment = new ScheduleFragment();
                break;
            case 3:
                fragment = new ScheduleFragment();
                break;
        }


        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
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
