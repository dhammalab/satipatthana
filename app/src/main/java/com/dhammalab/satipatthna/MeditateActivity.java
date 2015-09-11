package com.dhammalab.satipatthna;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.dhammalab.satipatthna.domain.Event;
import com.dhammalab.satipatthna.domain.Meditations;
import com.dhammalab.satipatthna.domain.NoteType;
import com.dhammalab.satipatthna.domain.Session;
import com.dhammalab.satipatthna.domain.Template;
import com.dhammalab.satipatthna.repository.Settings;

import java.util.Timer;
import java.util.TimerTask;

public class MeditateActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout seeIn;
    private LinearLayout hearIn;
    private LinearLayout feelIn;
    private LinearLayout seeOut;
    private LinearLayout hearOut;
    private LinearLayout feelOut;
    private LinearLayout ins;
    private LinearLayout outs;

    private LinearLayout[] buttons;

    private Session session;

    private Settings settings;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_meditate);

        settings = getSettings();

        ins = (LinearLayout) findViewById(R.id.ins);
        ins.setOnClickListener(this);
        seeIn = (LinearLayout) findViewById(R.id.seeIn);
        seeIn.setOnClickListener(this);
        hearIn = (LinearLayout) findViewById(R.id.hearIn);
        hearIn.setOnClickListener(this);
        feelIn = (LinearLayout) findViewById(R.id.feelIn);
        feelIn.setOnClickListener(this);
        outs = (LinearLayout) findViewById(R.id.outs);
        outs.setOnClickListener(this);
        seeOut = (LinearLayout) findViewById(R.id.seeOut);
        seeOut.setOnClickListener(this);
        hearOut = (LinearLayout) findViewById(R.id.hearOut);
        hearOut.setOnClickListener(this);
        feelOut = (LinearLayout) findViewById(R.id.feelOut);
        feelOut.setOnClickListener(this);

        buttons = new LinearLayout[]{ins, seeIn, hearIn, feelIn, outs, seeOut, hearOut, feelOut};

        Template template = settings.getTemplate();

        if (template.equals(Template.INTERNAL)) {
            hearOut.setVisibility(View.GONE);
            seeOut.setVisibility(View.GONE);
            feelOut.setVisibility(View.GONE);
            findViewById(R.id.meditate_out_layout).setVisibility(View.GONE);
            findViewById(R.id.meditate_out_top_divider).setVisibility(View.GONE);
        } else if (template.equals(Template.EXTERNAL)) {
            hearIn.setVisibility(View.GONE);
            seeIn.setVisibility(View.GONE);
            feelIn.setVisibility(View.GONE);
            findViewById(R.id.meditate_in_layout).setVisibility(View.GONE);
            findViewById(R.id.meditate_in_top_divider).setVisibility(View.GONE);
        } else if (template.equals(Template.MIND_BODY)) {
            ins.setVisibility(View.VISIBLE);
            outs.setVisibility(View.VISIBLE);
            hearIn.setVisibility(View.GONE);
            seeIn.setVisibility(View.GONE);
            feelIn.setVisibility(View.GONE);
            hearOut.setVisibility(View.GONE);
            seeOut.setVisibility(View.GONE);
            feelOut.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SatiApplication.getInstance().playBell();
        session = new Session(System.currentTimeMillis());
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MeditateActivity.this, WellDoneActivity.class));
                finish();
            }
        }, settings.getMeditationTime() * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SatiApplication.getInstance().stopBell();
        session.setSessionEndTime(System.currentTimeMillis());
        settings.setLastSession(session);
        Meditations meditations = settings.getMeditations();
        meditations.addSession(session);
        settings.setMeditations(meditations);
        timer.cancel();
        timer.purge();
        finish();
    }

    @Override
    synchronized public void onClick(View view) {
        NoteType type = null;
        if (view == seeIn) {
            type = NoteType.SEE_IN;
        } else if (view == seeOut) {
            type = NoteType.SEE_OUT;
        } else if (view == hearIn) {
            type = NoteType.HEAR_IN;
        } else if (view == hearOut) {
            type = NoteType.HEAR_OUT;
        } else if (view == feelIn) {
            type = NoteType.FEEL_IN;
        } else if (view == feelOut) {
            type = NoteType.FEEL_OUT;
        } else if (view == ins) {
            type = NoteType.INS;
        } else if (view == outs) {
            type = NoteType.OUTS;
        }
        Event lastEvent = session.getLastEvent();
        boolean buttonVisible;
        if (lastEvent == null) {
            session.addEvent(new Event(type, System.currentTimeMillis(), true));
            buttonVisible = false;
        } else {
            if (lastEvent.getType() == type) {
                session.addEvent(new Event(type, System.currentTimeMillis(), !lastEvent.isNoteAppear()));
                buttonVisible = lastEvent.isNoteAppear();
            } else {
                if (lastEvent.isNoteAppear()) {
                    session.addEvent(new Event(lastEvent.getType(), System.currentTimeMillis(), false));
                }
                session.addEvent(new Event(type, System.currentTimeMillis(), true));
                buttonVisible = false;
            }
        }
        for (LinearLayout button : buttons) {
            updateViewsVisibility(button, View.VISIBLE);
        }
        if (buttonVisible) {
            updateViewsVisibility((ViewGroup) view, View.VISIBLE);
        } else {
            updateViewsVisibility((ViewGroup) view, View.GONE);
        }
    }

    private void updateViewsVisibility(ViewGroup rootView, int visibility) {
        View title = rootView.findViewById(R.id.meditate_title);
        View subtitle = rootView.findViewById(R.id.meditate_subtitle);
        View gone = rootView.findViewById(R.id.meditate_gone);
        if (visibility == View.GONE) {
            title.setVisibility(View.GONE);
            subtitle.setVisibility(View.GONE);
            gone.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.VISIBLE);
            subtitle.setVisibility(View.VISIBLE);
            gone.setVisibility(View.GONE);
        }
    }
}
