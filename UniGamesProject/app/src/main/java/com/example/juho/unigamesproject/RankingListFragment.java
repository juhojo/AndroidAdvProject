package com.example.juho.unigamesproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RankingListFragment extends Fragment implements AsyncResponse {

    private ToornamentTask asyncTask;
    CustomAdapter adapter;

    private OnMAFragmentInteractionListener mListener;
    ListView listView;
    ArrayList<String> titles = new ArrayList<>();

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

        // If updating
        if (asyncTask != null) {
            // Clear the adapter
            adapter.clear();
        }

        // Create new instance of ToornamentTask
        asyncTask = new ToornamentTask();
        asyncTask.execute("get", action); // populate the list

        // Delegate back to this class
        asyncTask.delegate = this;

        return myView;
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
        System.out.println("RankingListFragment gets jsonarray: " + jsonArray);
        JSONSorter jsonSorter = new JSONSorter(this.getContext());
        jsonSorter.execute(jsonArray);
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

    // Inner AsyncTask for JSON formatting
    class JSONSorter extends AsyncTask<JSONArray, String, JSONArray> {
        private Context mContext;

        public JSONSorter (Context context) {
            mContext = context;
        }

        @Override
        protected JSONArray doInBackground(JSONArray... params) {
            return sortJsonArray(params[0]);
        }

        @Override
        protected void onPostExecute(JSONArray sortedByScores) {
            for (int i = 0; i < sortedByScores.length(); i++) {
                try {
                    JSONObject singleObj = sortedByScores.getJSONObject(i);
                    titles.add(singleObj.getString("name"));
                    // TODO Change titles to HashMap and pass both title and scores
                    // Redo the CustomAdapter from ArrayList to HashMap
                } catch (Exception e) {
                    // Something went wrong
                }
            }
            // Set the adapter with data
            adapter = new CustomAdapter(mContext, titles);
            listView.setAdapter(adapter);
        }

        public JSONArray sortJsonArray(JSONArray array) {
            List<JSONObject> jsons = new ArrayList<JSONObject>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    jsons.add(array.getJSONObject(i));
                } catch (Exception e) {
                    // Something went wrong
                }
            }
            Collections.sort(jsons, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject prevObj, JSONObject nextObj) {
                    int prevScore = 0;
                    int nextScore = 0;
                    try {
                        prevScore = prevObj.getInt("score");
                        nextScore = nextObj.getInt("score");
                    } catch (Exception e) {
                        // Something went wrong
                        System.out.println("wrong b");
                    }
                    // Return larger of the two values
                    return (prevScore > nextScore) ? -1 : 1;
                    // ** If previous number is larger do nothing, else swap
                }
            });
            return new JSONArray(jsons);
        }
    }


}

class CustomAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<String> title;

    CustomAdapter(Context context, ArrayList<String> title) {

        super(context, R.layout.ranking_list_item, title);
        this.context = context;
        this.title = title;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.ranking_list_item, parent, false);
        TextView listNumber = (TextView)row.findViewById(R.id.list_number);
        TextView primaryText = (TextView)row.findViewById(R.id.firstLine);

        int pos = position+1;
        listNumber.setText(Integer.toString(pos));
        primaryText.setText(title.get(position));
        pos++;
        return row;
    }

}
