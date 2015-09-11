package com.dhammalab.satipatthna;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dhammalab.satipatthna.repository.Settings;

public class WellDoneActivity extends BaseActivity {

    private Button returnBtn;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_done);
        settings = getSettings();
        settings.setWellDoneVisitCount(settings.getWellDoneVisitCount() + 1);
        returnBtn = (Button) findViewById(R.id.btn_return);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SatiApplication.getInstance().playBell();
    }

    @Override
    protected void onStop() {
        SatiApplication.getInstance().stopBell();
        super.onStop();
    }
}
