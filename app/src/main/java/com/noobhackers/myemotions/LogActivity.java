package com.noobhackers.myemotions;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LogActivity extends AppCompatActivity {

    Spinner moodspin;
    RequestQueue mQueue;
    CustomAdaptor adapter;
    EditText edit;
    TextView text;
    String[] moods = {"Very Happy", "Happy", "Neutral", "Sad", "Very Sad"};
    int[] images = {R.drawable.veryhappy, R.drawable.happy, R.drawable.neutral, R.drawable.sad, R.drawable.verysad};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        moodspin = (Spinner) findViewById(R.id.moodspinner);
        adapter = new CustomAdaptor(this, moods, images);

        moodspin.setAdapter(adapter);

        edit = (EditText) findViewById(R.id.logText);

        ImageView submitLog = (ImageView) findViewById(R.id.submitLog);


        submitLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendWorkPostRequest();

                startActivity(new Intent(LogActivity.this, MainPageActivity.class));
            }
        });

        mQueue = Volley.newRequestQueue(this);

        moodspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), moods[i], Toast.LENGTH_LONG).show();
                text = view.findViewById(R.id.moodText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void sendWorkPostRequest() {

        try {
            String URL = "https://nw-mood-server.herokuapp.com/addLog";
            JSONObject logData = new JSONObject();

            String content = edit.getText().toString();
            String mood = text.getText().toString();
                logData.put("userId", "12345");
                logData.put("mood", mood);
                logData.put("log", content);

            JsonObjectRequest jsonOblect = new JsonObjectRequest(Request.Method.POST, URL, logData, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    onBackPressed();

                }
            });

            mQueue.add(jsonOblect);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_LONG).show();

        }
}