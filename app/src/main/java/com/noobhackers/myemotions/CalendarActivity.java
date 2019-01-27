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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.roomorama.caldroid.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import com.android.volley.RequestQueue;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class CalendarActivity extends Fragment {

    private RequestQueue mQueue;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.activity_calendar, container, false);

        FloatingActionButton moodButton = myView.findViewById(R.id.moodLog);

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LogActivity.class));
            }
        });

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);
        caldroidFragment.setArguments(args);

        mQueue = Volley.newRequestQueue(mContext);
        jsonParse(caldroidFragment);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_caldroid, caldroidFragment).commit();

        return myView;
    }

    private void jsonParse(final CaldroidFragment caldroidFragment) {
        String url = "https://www.example.com/getThedata";

        int month = caldroidFragment.getMonth();
        final String currMonth = new DateFormatSymbols().getMonths()[month-1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray(currMonth);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject january = jsonArray.getJSONObject(i);
                                double happy = Double.parseDouble(january.getString("mood_happy"));
                                double sad = Double.parseDouble(january.getString("mood_sad"));
                                double neutral = Double.parseDouble(january.getString("mood_neutral"));
                                String date = january.getString("timestamp");
                                double max = Math.max(happy, Math.max(sad, neutral));
                                Date newDate = new SimpleDateFormat("MM/dd/yyyy").parse(date);
                                if(max == happy) {
                                    if(max >= 0.7) {
                                        caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.veryhappy), newDate);
                                    }
                                    else {
                                        caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.happy), newDate);
                                    }
                                }
                                else if(max == sad) {
                                    if(max >= 0.7) {
                                        caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.verysad), newDate);
                                    }
                                    else {
                                        caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.sad), newDate);
                                    }
                                }
                                else {
                                    caldroidFragment.setBackgroundDrawableForDate(getResources().getDrawable(R.drawable.neutral), newDate);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }
}