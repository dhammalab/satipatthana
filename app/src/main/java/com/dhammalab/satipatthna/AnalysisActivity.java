package com.dhammalab.satipatthna;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.dhammalab.satipatthna.domain.Bin;
import com.dhammalab.satipatthna.domain.Event;
import com.dhammalab.satipatthna.domain.NoteType;
import com.dhammalab.satipatthna.domain.Session;
import com.dhammalab.satipatthna.repository.Settings;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

public class AnalysisActivity extends ChartsBaseActivity implements View.OnClickListener{

    private Settings settings;

    private Button btnPostAnalytics;

    private String frequency;
    private String noteSpeed;
    private String noteLengths;
    private String durationOfNoteTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSettings();
        super.onCreate(savedInstanceState);
        settings.setAnalyticVisitCount(settings.getAnalyticVisitCount() + 1);

        btnPostAnalytics = (Button) findViewById(R.id.btn_send_analytics);
        btnPostAnalytics.setOnClickListener(this);

        if(settings.getLastSession() != null && settings.getLastSession().isSavedInDb()) {
            btnPostAnalytics.setVisibility(View.GONE);
        }

        showUniqueId(false);
        showUserSettingsViews(false);
    }

    @Override
    public String getAssetsFileName() {
        return "analysis.html";
    }

    @Override
    public String[] getData() {
        //Store the values locally after they are calculated as to not do these
        //operations more than once --antlip

        frequency = generateTextForFrequency();
        noteSpeed = generateTextForNoteSpeed();
        noteLengths = generateTextForNoteLengths();
        durationOfNoteTypes = generateDurationForNoteTypes();

        Log.d(AnalysisActivity.class.getSimpleName(), "duration of note types: " + durationOfNoteTypes);

        return new String[]{frequency, durationOfNoteTypes, noteSpeed, noteLengths};
    }

    private String generateDurationForNoteTypes() {
        Session session = settings.getLastSession();
        List<Event> events = session.getEvents();
        Collections.sort(events);

        int insDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.INS);
        int outsDuration =  Utils.getDurationInSecondsForNoteType(events, NoteType.OUTS);
        int seeInDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.SEE_IN);
        int seeOutDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.SEE_OUT);
        int hearInDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.HEAR_IN);
        int hearOutDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.HEAR_OUT);
        int feelInDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.FEEL_IN);
        int feelOutDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.FEEL_OUT);

        StringBuilder dataBuilder = new StringBuilder();

        // ['See in', 11],
        dataBuilder.append("['");
        dataBuilder.append(getString(R.string.meditate_see_in) + " (" + seeInDuration + "s)");
        dataBuilder.append("', ");
        dataBuilder.append(seeInDuration);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_hear_in) + " (" + hearInDuration + "s)");
        dataBuilder.append("', ");
        dataBuilder.append(hearInDuration);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_feel_in) + " (" + feelInDuration + "s)");
        dataBuilder.append("', ");
        dataBuilder.append(feelInDuration);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_see_out) + " (" + seeOutDuration + "s)");
        dataBuilder.append("', ");
        dataBuilder.append(seeOutDuration);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_hear_out) + " (" + hearOutDuration + "s)");
        dataBuilder.append("', ");
        dataBuilder.append(hearOutDuration);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_feel_out) + " (" + feelOutDuration + "s)");
        dataBuilder.append("', ");
        dataBuilder.append(feelOutDuration);
        dataBuilder.append("],\r\n");

        dataBuilder.append("['");
        dataBuilder.append(getString(R.string.meditate_ins) + " (" + insDuration + "s)");
        dataBuilder.append("', ");
        dataBuilder.append(insDuration);
        dataBuilder.append("],\r\n");

        dataBuilder.append("['");
        dataBuilder.append(getString(R.string.meditate_outs) + " (" + outsDuration + "s)");
        dataBuilder.append("', ");
        dataBuilder.append(outsDuration);
        dataBuilder.append("]\r\n");
        return dataBuilder.toString();
    }

    private String generateTextForNoteSpeed() {
        Session session = settings.getLastSession();
        long frame = (session.getSessionEndTime() - session.getSessionStartTime()) / 10;
        frame = frame / 1000 * 1000;
        if (frame == 0) {
            return "['', 0]";
        }
        List<Bin> bins = session.calculateNotingSpeed(frame);
        StringBuilder dataBuilder = new StringBuilder();
        for (int i = 0, n = bins.size(); i < n; i++) {
            dataBuilder.append("['");
            dataBuilder.append(new DateTime(bins.get(i).getStartTime()).toString("hh:mm:ss"));
            dataBuilder.append("', ");
            dataBuilder.append(bins.get(i).getValue());
            if (i == n - 1) {
                dataBuilder.append("]\r\n");
            } else {
                dataBuilder.append("],\r\n");
            }
        }
        return dataBuilder.toString();
    }

    private String generateTextForNoteLengths() {
        Session session = settings.getLastSession();
        List<Bin> bins = session.calculateNoteLengths(8);
        boolean empty = true;
        for (int i = 0, n = bins.size(); i < n; i++) {
            if (bins.get(i).getValue() != 0) {
                empty = false;
                break;
            }
        }
        StringBuilder dataBuilder = new StringBuilder();
        for (int i = 0, n = bins.size(); i < n; i++) {
            dataBuilder.append("['");
            if (!empty) {
                dataBuilder.append(String.format("%.2f", bins.get(i).getStartTime() * 1.0 / 1000));
            } else {
                dataBuilder.append(0);
            }
            dataBuilder.append("', ");
            dataBuilder.append(bins.get(i).getValue());
            if (i == n - 1) {
                dataBuilder.append("]\r\n");
            } else {
                dataBuilder.append("],\r\n");
            }
        }
        return dataBuilder.toString();
    }

    private String generateTextForFrequency() {
        int insCount = 0;
        int outsCount = 0;
        int seeInCount = 0;
        int hearInCount = 0;
        int feelInCount = 0;
        int seeOutCount = 0;
        int hearOutCount = 0;
        int feelOutCount = 0;
        Session session = settings.getLastSession();
        if (session != null) {
            insCount = session.getCompletedNotesCount(NoteType.INS);
            outsCount = session.getCompletedNotesCount(NoteType.OUTS);
            seeInCount = session.getCompletedNotesCount(NoteType.SEE_IN);
            hearInCount = session.getCompletedNotesCount(NoteType.HEAR_IN);
            feelInCount = session.getCompletedNotesCount(NoteType.FEEL_IN);
            seeOutCount = session.getCompletedNotesCount(NoteType.SEE_OUT);
            hearOutCount = session.getCompletedNotesCount(NoteType.HEAR_OUT);
            feelOutCount = session.getCompletedNotesCount(NoteType.FEEL_OUT);
        }
        StringBuilder dataBuilder = new StringBuilder();

        // ['See in', 11],
        dataBuilder.append("['");
        dataBuilder.append(getString(R.string.meditate_see_in));
        dataBuilder.append("', ");
        dataBuilder.append(seeInCount);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_hear_in));
        dataBuilder.append("', ");
        dataBuilder.append(hearInCount);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_feel_in));
        dataBuilder.append("', ");
        dataBuilder.append(feelInCount);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_see_out));
        dataBuilder.append("', ");
        dataBuilder.append(seeOutCount);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_hear_out));
        dataBuilder.append("', ");
        dataBuilder.append(hearOutCount);
        dataBuilder.append("],\r\n");

        dataBuilder.append("          ['");
        dataBuilder.append(getString(R.string.meditate_feel_out));
        dataBuilder.append("', ");
        dataBuilder.append(feelOutCount);
        dataBuilder.append("],\r\n");

        dataBuilder.append("['");
        dataBuilder.append(getString(R.string.meditate_ins));
        dataBuilder.append("', ");
        dataBuilder.append(insCount);
        dataBuilder.append("],\r\n");

        dataBuilder.append("['");
        dataBuilder.append(getString(R.string.meditate_outs));
        dataBuilder.append("', ");
        dataBuilder.append(outsCount);
        dataBuilder.append("]\r\n");

        return dataBuilder.toString();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_send_analytics:
                    Utils.sendDetailedAnalyticsDataToServer(this, settings.getLastSession(), settings, noteSpeed, noteLengths, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                Toast.makeText(SatiApplication.getInstance(), "Successfully saved!", Toast.LENGTH_SHORT).show();
                                btnPostAnalytics.setVisibility(View.GONE);
                            }
                        }
                    });
                break;
            default:
        }
    }
}
