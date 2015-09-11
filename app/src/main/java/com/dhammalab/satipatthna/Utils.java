package com.dhammalab.satipatthna;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.dhammalab.satipatthna.domain.Event;
import com.dhammalab.satipatthna.domain.NoteType;
import com.dhammalab.satipatthna.domain.Session;
import com.dhammalab.satipatthna.repository.Settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ilya on 13.07.2014.
 */
public class Utils {
    private static String uniqueId;

    public static String getUniqueId() {
        if (uniqueId == null) {
            uniqueId = android.provider.Settings.Secure.getString(SatiApplication.getInstance().getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID).substring(0, 5);
        }
        return uniqueId;
    }

    public static void sendDetailedAnalyticsDataToServer(final Context context, final Session session, final Settings settings, final String noteSpeed, String noteLengths, final SaveCallback callback) {
        if (session == null || settings == null) return;

        ParseObject dms = new ParseObject("MeditationSessionDetailed");

        dms.put("uniqueId", uniqueId);

        int seeInCount = 0;
        int hearInCount = 0;
        int feelInCount = 0;
        int seeOutCount = 0;
        int hearOutCount = 0;
        int feelOutCount = 0;
        int insCount = 0;
        int outsCount = 0;
        int totalNoteCount = 0;

        List<Event> events = session.getEvents();
        int insDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.INS);
        int outsDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.OUTS);
        int seeInDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.SEE_IN);
        int seeOutDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.SEE_OUT);
        int hearInDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.HEAR_IN);
        int hearOutDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.HEAR_OUT);
        int feelInDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.FEEL_IN);
        int feelOutDuration = Utils.getDurationInSecondsForNoteType(events, NoteType.FEEL_OUT);

        seeInCount = session.getCompletedNotesCount(NoteType.SEE_IN);
        hearInCount = session.getCompletedNotesCount(NoteType.HEAR_IN);
        feelInCount = session.getCompletedNotesCount(NoteType.FEEL_IN);
        seeOutCount = session.getCompletedNotesCount(NoteType.SEE_OUT);
        hearOutCount = session.getCompletedNotesCount(NoteType.HEAR_OUT);
        feelOutCount = session.getCompletedNotesCount(NoteType.FEEL_OUT);
        insCount = session.getCompletedNotesCount(NoteType.INS);
        outsCount = session.getCompletedNotesCount(NoteType.OUTS);
        totalNoteCount = seeInCount + seeOutCount + hearInCount + hearOutCount + feelInCount + feelOutCount;

        dms.put("startTime", session.getSessionStartTime());
        dms.put("endTime", session.getSessionEndTime());
        dms.put("events", new Gson().toJson(session.getEvents()));

        dms.put("typesOfNotes", getTypesOfNotes(session.getEvents()));
        dms.put("averageNoteDuration", getAverageTimeForEvents(session.getEvents()));

        dms.put("seeIn", seeInCount);
        dms.put("seeOut", seeOutCount);
        dms.put("hearIn", hearInCount);
        dms.put("hearOut", hearOutCount);
        dms.put("feelIn", feelInCount);
        dms.put("feelOut", feelOutCount);
        dms.put("ins", insCount);
        dms.put("outs", outsCount);

        dms.put("seeInDuration", seeInDuration);
        dms.put("seeOutDuration", seeOutDuration);
        dms.put("hearInDuration", hearInDuration);
        dms.put("hearOutDuration", hearOutDuration);
        dms.put("feelInDuration", feelInDuration);
        dms.put("feelOutDuration", feelOutDuration);
        dms.put("insDuration", insDuration);
        dms.put("outsDuration", outsDuration);

        dms.put("noteCount", totalNoteCount);
        dms.put("timespan", settings.getMeditationTime());
        dms.put("noteSpeed", noteSpeed);
        dms.put("noteLengths", noteLengths);

        dms.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(context, "Could not save to server: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    session.setSavedInDb(true);
                    settings.setLastSession(session);
                }

                if (callback != null) {
                    callback.done(e);
                }
            }
        });
    }

    private static int getTypesOfNotes(List<Event> events) {
        HashMap<NoteType, ArrayList<Event>> typeMap = new HashMap<NoteType, ArrayList<Event>>();
        for (Event event : events) {
            if (!typeMap.containsKey(event.getType())) {
                ArrayList<Event> typeEvents = new ArrayList<Event>();
                typeMap.put(event.getType(), typeEvents);
            }
            typeMap.get(event.getType()).add(event);
        }

        return typeMap.keySet().size();
    }

    public static int getAverageTimeForEvents(List<Event> events) {
        if (events.size() == 1 || events.size() == 0) return 0;

        long averageTime = 0;
        Event lastEvent = null;

        for (Event event : events) {
            if (lastEvent == null) {
                lastEvent = event;
                continue;
            }

            averageTime += event.getTime() - lastEvent.getTime();
            lastEvent = event;
        }

        return (int) TimeUnit.MILLISECONDS.toSeconds(averageTime / events.size());
    }

    public static int getDurationInSecondsForNoteType(List<Event> events, NoteType type) {
        int duration = 0;
        Event lastEvent = null;

        for (Event event : events) {
            if (lastEvent == null) {
                lastEvent = event;
                continue;
            }

            if (event.getType().equals(type)) {
                duration += (event.getTime() - lastEvent.getTime());
            }
            lastEvent = event;
        }

        return (int) TimeUnit.MILLISECONDS.toSeconds(duration);
    }
}
