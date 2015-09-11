package com.dhammalab.satipatthna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseAnalytics;


public class StartActivity extends BaseActivity {

    private Button launchHomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ParseAnalytics.trackAppOpened(getIntent());
        launchHomeActivity = (Button) findViewById(R.id.btn_start);
        launchHomeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeActivity = new Intent(StartActivity.this, HomeActivity.class);
                startActivity(homeActivity);
            }
        });
    }

}
