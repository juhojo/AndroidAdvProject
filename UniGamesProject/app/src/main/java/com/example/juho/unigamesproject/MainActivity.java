package com.example.juho.unigamesproject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;


public class MainActivity extends AppCompatActivity implements AsyncResponse, OnMAFragmentInteractionListener {
    private UDgetTask asyncTask = new UDgetTask();
    boolean doubleBackToExitPressedOnce = false;
    User user;
    private boolean soundIsOn;

    // Navigation
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    private ListView mDrawerList;
    Boolean mSlideState;
    Toolbar toolbar;
    private Fragment fragment;

    // Background Music
    MediaPlayer player;

    ConnectionReceiver mBroadcastReceiver = new ConnectionReceiver();
    private AlertDialog alertDialog;
    boolean mount = true;

    // Preferences, we use to check & update if used wants sound on / off
    SharedPreferences sharedpreferences;

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
        // Set scaled drawable as navigation-icon
        toolbar.setNavigationIcon(scaledIcon);
        toolbar.setLogo(toornamentLogo);

        // Set onClick to toornament logo
        View logoView = toolbar.getChildAt(1);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(Gravity.RIGHT);
        logoView.setLayoutParams(lp);

        logoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        // Get default preferences
        sharedpreferences = getSharedPreferences(Variables.MyPREFERENCES, Context.MODE_PRIVATE);
        String soundString = sharedpreferences.getString(Variables.SoundIsOn, null);

        if (soundString == null || soundString.equals("yes")) {
            soundIsOn = true;
        } else {
            soundIsOn = false;
        }

        // Use boolean comparisons from now on
        // Set menu item sound icon
        MenuItem item = menu.findItem(R.id.sound);
        if (soundIsOn) {
            item.setIcon(R.drawable.unigames_speaker);
        } else {
            item.setIcon(R.drawable.unigames_speaker_mute);
        }
    }

    private void setMenuItemListeners(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            final int j = i;

            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    selectItem(j, item);
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
    @Override
    public void ttProcessFinish(JSONArray jsonArray){
        // Override interface method
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position, MenuItem item) {

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            default:
            case 0:
                fragment = new ScheduleFragment();
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case 1:
                fragment = RankingFragment.newInstance(user);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case 2:
                String soundString;
                if (soundIsOn) {
                    item.setIcon(R.drawable.unigames_speaker_mute);
                    soundString = "no";
                    player.pause();
                } else {
                    item.setIcon(R.drawable.unigames_speaker);
                    soundString = "yes";
                    player.start();
                }
                // Update for this session
                soundIsOn = !soundIsOn;

                // If device has gone to sleep while MainActivity is on view
                // get default preferences again.
                if (sharedpreferences == null) {
                    sharedpreferences = getSharedPreferences(Variables.MyPREFERENCES, Context.MODE_PRIVATE);
                }
                // Update as standard soundIsOn to preferences
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Variables.SoundIsOn, soundString);
                editor.commit();
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
        // TODO
    }

    @Override
    public void onResume() {
        super.onResume();
        // Sound
        player = MediaPlayer.create(this, R.raw.unigameses);
        player.setLooping(true); // Set looping
        player.setVolume(100,100);
        if (soundIsOn){
            player.start();
        }
        // Connectivity change
        IntentFilter iff = new IntentFilter();
        iff.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        // Put whatever message you want to receive as the action
        this.registerReceiver(this.mBroadcastReceiver, iff);
    }

    @Override
    public void onPause() {
        super.onPause();
        player.stop();
        this.unregisterReceiver(this.mBroadcastReceiver);
    }

    public class ConnectionReceiver extends BroadcastReceiver {

        public ConnectionReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected && !mount) {
                    MainActivity.this.broadcastAlertHide(intent);
                } else if (!isConnected){
                    MainActivity.this.broadcastAlertShow(intent);
                    mount = false;
                }
            }
        }
    }

    private void broadcastAlertShow(Intent intent) {
        if (alertDialog == null || !alertDialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);
            builder.setView(dialogView);

            TextView title = (TextView)dialogView.findViewById(R.id.dialog_title);
            TextView message = (TextView)dialogView.findViewById(R.id.dialog_message);

            title.setText(R.string.dialog_title_internet);
            message.setText(R.string.dialog_message_internet);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    private void broadcastAlertHide(Intent intent) {
        try {
            if (alertDialog != null && alertDialog.isShowing())
                alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
