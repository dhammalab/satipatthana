package com.dhammalab.satipatthna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseTimeActivity extends BaseActivity implements View.OnClickListener {

    private static final int[] MEDITATION_MINUTES = new int[]{2 * 60, 5 * 60, 10 * 60,
            30 * 60, 60 * 60, 180 * 60};
    private Button twoMinBtn;
    private Button fiveMinBtn;
    private Button tenMinBtn;
    private Button thirtyMinBtn;
    private Button sixtyMinBtn;
    private Button oneHundredEightyMinBtn;
    private Button[] timeButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);
        twoMinBtn = (Button) findViewById(R.id.btn_2_min);
        fiveMinBtn = (Button) findViewById(R.id.btn_5_min);
        tenMinBtn = (Button) findViewById(R.id.btn_10_min);
        thirtyMinBtn = (Button) findViewById(R.id.btn_30_min);
        sixtyMinBtn = (Button) findViewById(R.id.btn_60_min);
        oneHundredEightyMinBtn = (Button) findViewById(R.id.btn_180_min);
        timeButtons = new Button[]{twoMinBtn, fiveMinBtn, tenMinBtn, thirtyMinBtn, sixtyMinBtn,
                oneHundredEightyMinBtn};
        for (Button button : timeButtons) {
            button.setOnClickListener(this);
        }
        int level = getSettings().getWellDoneVisitCount();
        if (level >= 3) {
            twoMinBtn.setVisibility(View.GONE);
            tenMinBtn.setVisibility(View.VISIBLE);
            thirtyMinBtn.setVisibility(View.VISIBLE);
        }
        if (level == 4) {
            sixtyMinBtn.setVisibility(View.VISIBLE);
        }
        if (level >= 5) {
            oneHundredEightyMinBtn.setVisibility(View.VISIBLE);
        }
    }

    private void startMeditation(int seconds) {
        getSettings().setMeditationTime(seconds);
        Intent prepareIntent = new Intent(this, PrepareActivity.class);
        startActivity(prepareIntent);
        finish();
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < timeButtons.length; i++) {
            if (timeButtons[i] == v) {
                startMeditation(MEDITATION_MINUTES[i]);
            }
        }
    }
}
