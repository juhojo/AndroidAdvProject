package com.example.juho.unigamesproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class RankingFragment extends Fragment {
    private static final String ARG_PARAM1 = "USER";
    MyPagerAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    RankingListFragment fragment;
    private boolean canBeUpdated = true;

    private User user;

    public RankingFragment() {
        // Required empty public constructor
    }

    public static RankingFragment newInstance(User user) {
        RankingFragment fragment = new RankingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User)getArguments().getSerializable(ARG_PARAM1);

            // Set adapter after receiving user data
            adapter = new MyPagerAdapter(getChildFragmentManager());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment & save as variable
        final RelativeLayout myView = (RelativeLayout) inflater.inflate(R.layout.fragment_ranking, container, false);

        // Update button
        final ImageButton imageButton = (ImageButton)myView.findViewById(R.id.update_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // "Only" allow update once in a second
                if (canBeUpdated) {
                    // Update lists
                    adapter.notifyDataSetChanged();

                    Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.button_rotate);
                    rotation.setRepeatCount(Animation.INFINITE);

                    // Start animation
                    imageButton.startAnimation(rotation);
                    canBeUpdated = false;

                    //Stop animation after 1 second
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imageButton.clearAnimation();
                            canBeUpdated = true;
                        }
                    }, 1000);
                }

            }
        });

        // Pager
        viewPager = (ViewPager)myView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        tabLayout = (TabLayout)myView.findViewById(R.id.fixed_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return myView;
    }

    // Inner class of Activity
    class MyPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[]{"User", "Team"};

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
                    return fragment.newInstance("USER", user);
                case 1:
                    return fragment.newInstance("TEAM", user);
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
