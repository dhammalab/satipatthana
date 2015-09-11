package com.dhammalab.satipatthna;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class PrepareActivity extends BaseActivity {

    // in seconds
    private static final int TIMEOUT = 30;
    private int currentSecondsToLaunch = TIMEOUT;
    private TextView countDownText;
    private Timer countDownTimer;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);
        handler = new Handler();
        countDownText = (TextView) findViewById(R.id.prepare_countdown);
        countDownText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PrepareActivity.this, MeditateActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownText.setText(getString(R.string.prepare_countdown, currentSecondsToLaunch));
        countDownTimer = new Timer();
        countDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentSecondsToLaunch--;
                if (currentSecondsToLaunch <= 0) {
                    countDownTimer.cancel();
                    countDownTimer.purge();
                    startActivity(new Intent(PrepareActivity.this, MeditateActivity.class));
                    finish();
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            countDownText.setText(getString(R.string.prepare_countdown, currentSecondsToLaunch));
                        }
                    });
                }
            }
        }, 1000L, 1000L);
    }

    @Override
    protected void onPause() {
        countDownTimer.cancel();
        countDownTimer.purge();
        countDownTimer = null;
        super.onPause();
    }

}
