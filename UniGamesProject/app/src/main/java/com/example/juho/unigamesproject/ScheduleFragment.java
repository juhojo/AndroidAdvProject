package com.example.juho.unigamesproject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ScheduleFragment extends Fragment implements AsyncResponse {
    private static final String ARG_PARAM1 = "USER";

    ListView listView;
    ArrayList<JSONObject> listItems = new ArrayList<>();

    private ToornamentTask asyncTask;
    ScheduleAdapter adapter;

    private String username;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    public static ScheduleFragment newInstance(String username) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // user = (User)getArguments().getSerializable(ARG_PARAM1);
            username = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (asyncTask != null) {
            // Clear the adapter
            adapter.clear();
        }
        // Create new instance of ToornamentTask
        asyncTask = new ToornamentTask();

        asyncTask.execute("get", username, "schedule"); // populate the list

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
        protected void onPostExecute(JSONArray sortedByDate) {
            for (int i = 0; i < sortedByDate.length(); i++) {
                try {
                    JSONObject singleObj = sortedByDate.getJSONObject(i);
                    listItems.add(singleObj);
                } catch (Exception e) {
                    // Something went wrong
                }
            }
            // Set the adapter with data
            adapter = new ScheduleAdapter(mContext, listItems);
            listView.setAdapter(adapter);
        }

        public JSONArray sortJsonArray(JSONArray array) {
            List<JSONObject> jsons = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    jsons.add(array.getJSONObject(i));
                } catch (Exception e) {
                    // Something went wrong
                    e.printStackTrace();
                }
            }
            Collections.sort(jsons, new Comparator<JSONObject>() {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

                @Override
                public int compare(JSONObject prevObj, JSONObject nextObj) {
                    System.out.println("prevObj: " + prevObj + ", nextObj: " + nextObj);
                    Date prevTime = new Date();
                    Date nextTime = new Date();
                    try {
                        prevTime = formatter.parse(prevObj.getString("time"));
                        nextTime = formatter.parse(nextObj.getString("time"));
                    } catch (Exception e) {
                        // Something went wrong
                        e.printStackTrace();
                    }
                    // Return larger of the two values
                    int before = prevTime.compareTo(nextTime);
                    return (before == 0) ? 1 : -1; // Ascending order
                    // ** If previous number is larger do nothing, else swap
                }
            });
            return new JSONArray(jsons);
        }
    }


    class ScheduleAdapter extends ArrayAdapter<JSONObject> {

        Context context;
        ArrayList<ScheduleItem> itemsList = new ArrayList<>();

        ScheduleAdapter(Context context, ArrayList<JSONObject> list) {
            super(context, R.layout.ranking_list_item, list);
            this.context = context;

            for ( JSONObject item : list ) {
                ScheduleItem scheduleItem = new ScheduleItem();
                try {
                    scheduleItem.newInstance(item.getString("id"), item.getJSONArray("teams"), item.getString("time"));
                    if (item.has("bet")) { // If is betted
                        scheduleItem.setBet(item.getInt("bet"));
                    }
                    itemsList.add(scheduleItem);
                } catch (JSONException e) {
                    // Something went wrong
                }
            }

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View row = inflater.inflate(R.layout.schedule_list_item, parent, false);

            TextView timeTxt = (TextView)row.findViewById(R.id.time);
            TextView teamOneTxt = (TextView)row.findViewById(R.id.team_one);
            TextView teamTwoTxt = (TextView)row.findViewById(R.id.team_two);

            final ScheduleItem scheduleItem = itemsList.get(position);

            timeTxt.setText(scheduleItem.getTime());
            if (position % 2 == 0) { // Style listview items' backgrounds.
                timeTxt.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.list_even));
            }

            try {
                teamOneTxt.setText(scheduleItem.getTeams().get(0).toString());
                teamTwoTxt.setText(scheduleItem.getTeams().get(1).toString());

                if (scheduleItem.getBet() != -1) {
                    TextView chosen = teamOneTxt;
                    if (scheduleItem.getBet() == 1) {
                        chosen = teamTwoTxt;
                    }
                    chosen.setTextColor(ContextCompat.getColor(getContext(), R.color.icons));
                }

                // Set onClick listeners for TextViews
                teamOneTxt.setOnClickListener(new ScheduleItemOnClickListener(username, scheduleItem.getId(), teamOneTxt.getText().toString(), 0));
                teamTwoTxt.setOnClickListener(new ScheduleItemOnClickListener(username, scheduleItem.getId(), teamTwoTxt.getText().toString(), 1));
                // Set other team as a tag that is used to update UI
                teamOneTxt.setTag(teamTwoTxt);
                teamTwoTxt.setTag(teamOneTxt);
            } catch (JSONException e) {
                // Something went wrong
            }

            return row;
        }
    }
}


