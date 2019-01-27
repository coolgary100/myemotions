package com.noobhackers.myemotions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.roomorama.caldroid.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import com.android.volley.RequestQueue;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;

public class CalendarActivity extends Fragment {
    CaldroidFragment caldroidFragment;
    private RequestQueue mQueue;
    Context mContext;
    ArrayList<Double> listHappy = new ArrayList<>();
    ArrayList<Double> listSad = new ArrayList<>();
    ArrayList<Double> listNeutral = new ArrayList<>();
    ArrayList<Date> listDate = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.activity_calendar, container, false);

        FloatingActionButton moodButton = myView.findViewById(R.id.moodLog);

        FloatingActionButton historyButton = myView.findViewById(R.id.logHistory);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LogHistory.class));
            }
        });

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LogActivity.class));
            }
        });

        mContext = myView.getContext();

        mQueue = Volley.newRequestQueue(mContext);

        caldroidFragment = new CaldroidFragment();

        jsonParse();

        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);

        caldroidFragment.setArguments(args);

        return myView;
    }

    private void jsonParse() {
        String url = "https://nw-mood-server.herokuapp.com/getMoods/12345/12019";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++) {
                                JSONObject mood = response.getJSONObject(i);

                                double happy = Double.parseDouble(mood.getString("mood_happy"));
                                double sad = Double.parseDouble(mood.getString("mood_sad"));
                                double neutral = Double.parseDouble(mood.getString("mood_neutral"));
                                String date = mood.getString("timestamp");
                                Date newDate = new SimpleDateFormat("MM/dd/yyyy").parse(date);
                                listHappy.add(happy);
                                listSad.add(sad);
                                listNeutral.add(neutral);
                                listDate.add(newDate);
                            }

                            alterCalendar();
                    } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        mQueue.add(jsonArrayRequest);
    }

    private void alterCalendar() {
        for(int i = 0; i < listHappy.size(); i++) {

            double max = Math.max(listHappy.get(i), Math.max(listSad.get(i), listNeutral.get(i)));

            //System.out.println(max);
            //System.out.println(listHappy.get(i));
            //System.out.println(listSad.get(i));
            //System.out.println(listNeutral.get(i));
            //System.out.println(listDate.get(i));

            if (max == listHappy.get(i)) {
                if (max >= 0.7) {
                    //System.out.println("SuperHappy");
                    caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.veryhappy), listDate.get(i));
                } else {
                    //System.out.println("Happy");
                    caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.happy), listDate.get(i));
                }
            } else if (max == listSad.get(i)) {
                if (max >= 0.7) {
                    //System.out.println("SuperSad");
                    caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.verysad), listDate.get(i));
                } else {
                    //System.out.println("Sad");
                    caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.sad), listDate.get(i));
                }
            } else {
                //System.out.println("Neutral");
                caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.neutral), listDate.get(i));
            }
        }

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_caldroid, caldroidFragment).commit();
    }
}