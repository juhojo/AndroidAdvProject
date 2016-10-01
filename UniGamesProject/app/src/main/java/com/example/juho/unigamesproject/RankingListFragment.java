package com.example.juho.unigamesproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class RankingListFragment extends Fragment {

    private OnMAFragmentInteractionListener mListener;
    ListView listView;
    String[] title = { "Juho", "Jussi", "Johannes" };

    private User user;
    private String action;

    private static final String ARG_PARAM1 = "ACTION";
    private static final String ARG_PARAM2 = "USER";

    public RankingListFragment() {
        // Required empty public constructor
    }

    public static RankingListFragment newInstance(String action, User user) {
        RankingListFragment fragment = new RankingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, action);
        args.putSerializable(ARG_PARAM2, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            action = getArguments().getString(ARG_PARAM1);
            user = (User)getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment & save as variable
        final RelativeLayout myView = (RelativeLayout) inflater.inflate(R.layout.fragment_ranking_list, container, false);
        listView = (ListView)myView.findViewById(R.id.ranking_list_view);
        CustomAdapter adapter = new CustomAdapter(this.getContext(), title);
        listView.setAdapter(adapter);

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

}

class CustomAdapter extends ArrayAdapter<String> {

    Context context;
    String[] title;

    CustomAdapter(Context c, String[] title) {

        super(c, R.layout.ranking_list_item,title);
        this.context = c;
        this.title=title;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.ranking_list_item, parent, false);
        TextView listNumber = (TextView)row.findViewById(R.id.list_number);
        TextView listUser = (TextView)row.findViewById(R.id.firstLine);

        int pos = position+1;
        listNumber.setText(Integer.toString(pos));
        listUser.setText(title[position]);
        pos++;
        return row;
    }

}
