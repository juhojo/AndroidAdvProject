package com.example.juho.unigamesproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class RankingFragment extends Fragment {
    private static final String ARG_PARAM1 = "USER";
    MyPagerAdapter adapter;

    private User user;

    private OnMAFragmentInteractionListener mListener;

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
            adapter = new MyPagerAdapter(getFragmentManager());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment & save as variable
        final RelativeLayout myView = (RelativeLayout) inflater.inflate(R.layout.fragment_ranking, container, false);

        // Pager
        final ViewPager viewPager = (ViewPager)myView.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout)myView.findViewById(R.id.fixed_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMAFragmentInteractionListener) {
            mListener = (OnMAFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMAFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
                    return RankingListFragment.newInstance("USER", user);
                case 1:
                    return RankingListFragment.newInstance("TEAM", user);
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
