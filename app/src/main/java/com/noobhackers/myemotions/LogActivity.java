package com.noobhackers.myemotions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;

public class LogActivity extends AppCompatActivity {

    Spinner moodspin;
    CustomAdaptor adapter;
    String[] moods = {"Very Happy", "Happy", "Neutral", "Sad", "Very Sad"};
    int[] images = {R.drawable.veryhappy, R.drawable.happy, R.drawable.neutral, R.drawable.sad, R.drawable.verysad};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        moodspin = (Spinner) findViewById(R.id.moodspinner);
        adapter = new CustomAdaptor(this, moods, images);

        moodspin.setAdapter(adapter);

        ImageView submitLog = (ImageView) findViewById(R.id.submitLog);

        submitLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogActivity.this, MainPageActivity.class));
            }
        });

        moodspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), moods[i], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
