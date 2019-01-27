package com.noobhackers.myemotions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LogHistory extends AppCompatActivity {

    RequestQueue mQueue;
    ArrayList<String> listMood = new ArrayList<>();
    ArrayList<String> listText = new ArrayList<>();

    ListView simpleList;
    int moodList[] = new int[50];
    String logList[] = new String[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loghistory);

        mQueue = Volley.newRequestQueue(this);

        getLogs();

        //System.out.println(moodList);


    }


    private void getLogs() {
        String url = "https://nw-mood-server.herokuapp.com/getLog/12345/1272019";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject mood = response.getJSONObject(i);

                                String currMood = mood.getString("mood");
                                String text = mood.getString("text");
                                //System.out.println(currMood);
                                //System.out.println(text);
                                listMood.add(currMood);
                                listText.add(text);

                            }

                            alterView();

                            simpleList = (ListView)findViewById(R.id.simpleListView);
                            MyAdapter myAdapter = new MyAdapter(LogHistory.this, moodList, logList);
                            simpleList.setAdapter(myAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        mQueue.add(jsonArrayRequest);
    }

    private void alterView() {

        for(int i = 0; i < listMood.size(); i++) {

            if(listMood.get(i).equals("Very Happy")) {
                moodList[i] = R.drawable.veryhappy;
            }
            else if(listMood.get(i).equals("Happy")) {
                moodList[i] = R.drawable.happy;
            }
            else if(listMood.get(i).equals("Neutral")) {
                moodList[i] = R.drawable.neutral;
            }
            else if(listMood.get(i).toLowerCase().equals("sad")) {
                moodList[i] = R.drawable.sad;
            }
            else if(listMood.get(i).equals("Very Sad")) {
                moodList[i] = R.drawable.verysad;
            }

            logList[i] = listText.get(i);
        }
    }
}
