package com.example.juho.unigamesproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;


public class ScheduleFragment extends Fragment implements AsyncResponse {

    private OnMAFragmentInteractionListener mListener;
    ListView listView;

    private ToornamentTask asyncTask;
    CustomAdapter adapter;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance() {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Create new instance of ToornamentTask
        asyncTask = new ToornamentTask();
        asyncTask.execute("get", "schedule"); // populate the list

        // Delegate back to this class
        asyncTask.delegate = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final RelativeLayout myView = (RelativeLayout)inflater.inflate(R.layout.fragment_schedule, container, false);
        listView = (ListView)myView.findViewById(R.id.schedule_list);
        return myView;
    }



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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processFinish(String output, String un, String team){
        // Override interface method
    }
    @Override
    public void udProcessFinish(String output, String id) {
        // Override interface method
    }
    @Override
    public void ttProcessFinish(JSONArray jsonArray){
        System.out.println("ScheduleFragment gets jsonarray: " + jsonArray);
        /*
        JSONSorter jsonSorter = new JSONSorter(this.getContext());
        jsonSorter.execute(jsonArray);
        */
    }
}
