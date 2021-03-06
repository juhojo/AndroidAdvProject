package com.example.juho.unigamesproject;

import android.content.Context;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RankingListFragment extends Fragment implements AsyncResponse {

    private ToornamentTask asyncTask;
    RankingListAdapter adapter;

    ListView listView;
    ArrayList<JSONObject> listItems = new ArrayList<>();

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

        // If user wants to update list
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
        JSONSorter jsonSorter = new JSONSorter(this.getContext());
        jsonSorter.execute(jsonArray);
    }
    
    // Inner AsyncTask for JSON formatting
    private class JSONSorter extends AsyncTask<JSONArray, String, JSONArray> {
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
                    listItems.add(singleObj);
                } catch (Exception e) {
                    // Something went wrong
                }
            }
            // Set the adapter with data
            adapter = new RankingListAdapter(mContext, listItems);
            listView.setAdapter(adapter);
        }

        public JSONArray sortJsonArray(JSONArray array) {
            List<JSONObject> jsons = new ArrayList<>();
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

class RankingListAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> scores = new ArrayList<>();

    RankingListAdapter(Context context, ArrayList<JSONObject> list) {
        super(context, R.layout.ranking_list_item, list);
        this.context = context;

        for ( JSONObject item : list ) {
            try {
                titles.add(item.getString("name"));
                scores.add(item.getString("score"));
            } catch (JSONException e) {
                // Something went wrong
            }
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.ranking_list_item, parent, false);
        TextView listNumber = (TextView)row.findViewById(R.id.list_number);
        TextView primaryText = (TextView)row.findViewById(R.id.firstLine);
        TextView scoreText = (TextView)row.findViewById(R.id.secondLine);

        int pos = position+1;
        listNumber.setText(Integer.toString(pos));
        primaryText.setText(titles.get(position));
        scoreText.setText("Points: " + scores.get(position));
        pos++;
        return row;
    }

}
