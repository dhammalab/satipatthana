package com.dhammalab.satipatthna.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tretyakov on 14.07.2014.
 */
public class Session {

    private ArrayList<Event> events;

    private long sessionStartTime;
    private long sessionEndTime = -1;
    private boolean isSavedInDb = false;

    public Session(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
        events = new ArrayList<Event>();
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public long getSessionEndTime() {

        return sessionEndTime;
    }

    public void setSessionEndTime(long sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public void addEvent(Event event) {
        if (event == null) {
            throw new IllegalArgumentException("Event could not be null");
        }
        Event latestEvent = getLatestEvent(event.getType());
        if (latestEvent == null || latestEvent.isNoteAppear() != event.isNoteAppear()) {
            events.add(event);
        } else {
            throw new IllegalArgumentException("Note could not appear twice in a row. It could only appear and then disappear");
        }
    }

    public int getCompletedNotesCount(NoteType noteType) {
        int disappearCount = 0;
        for (Event event : events) {
            if (!event.isNoteAppear() && noteType == event.getType()) {
                disappearCount++;
            }
        }
        return disappearCount;
    }

    public Event getLatestEvent(NoteType noteType) {
        for (int i = events.size() - 1; i >= 0; i--) {
            if (noteType == events.get(i).getType()) {
                return events.get(i);
            }
        }
        return null;
    }

    public Event getLastEvent() {
        if (!events.isEmpty()) {
            return events.get(events.size() - 1);
        }
        return null;
    }

    public List<Bin> calculateNotingSpeed(long timeframe) {
        ArrayList<Bin> bins = new ArrayList<Bin>();
        if (sessionEndTime == -1 && !events.isEmpty()) {
            sessionEndTime = events.get(events.size() - 1).getTime() + 1;
        }
        for (long start = sessionStartTime; start < sessionEndTime; start += timeframe) {
            long binStart = start;
            long binEnd = start + timeframe;
            int notes = 0;
            for (Event event : events) {
                if (event.isNoteAppear() && event.getTime() >= binStart
                        && (event.getTime() < binEnd || (binEnd == sessionEndTime && event.getTime() < binEnd))) {
                    notes++;
                }
            }
            bins.add(new Bin(binStart, binEnd, notes));
        }
        return bins;
    }

    public List<Bin> calculateNoteLengths(int binsNumber) {
        long maxLength = 0;
        long minLength = Long.MAX_VALUE;
        ArrayList<Long> lengths = new ArrayList<Long>();
        for (int i = 0, n = events.size(); i < n; i++) {
            if (events.get(i).isNoteAppear()) {
                Event nextEvent = getNextDisappearEventWithTheSameType(events.get(i), i);
                if (nextEvent != null) {
                    long length = nextEvent.getTime() - events.get(i).getTime();
                    lengths.add(length);
                    if (length > maxLength) {
                        maxLength = length;
                    }
                    if (length < minLength) {
                        minLength = length;
                    }
                }
            }
        }
        ArrayList<Bin> bins = new ArrayList<Bin>();
        long frame = (maxLength - minLength) / binsNumber;
        if (frame * binsNumber + minLength < maxLength) {
            frame++;
        }
        for (int i = 0; i < binsNumber; i++) {
            long binStart = minLength + frame * i;
            long binEnd = minLength + frame * (i + 1);
            int noteCount = 0;
            for (Long length : lengths) {
                if (length >= binStart && (length < binEnd || (binEnd >= maxLength && length <= binEnd))) {
                    noteCount++;
                }
            }
            bins.add(new Bin(binStart, binEnd, noteCount));
        }

        return bins;
    }

    private Event getNextDisappearEventWithTheSameType(Event event, int position) {
        for (int i = position, n = events.size(); i < n; i++) {
            if (events.get(i).getType() == event.getType() && !events.get(i).isNoteAppear()) {
                return events.get(i);
            }
        }
        return null;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return "Session{" +
                "events=" + events +
                ", sessionStartTime=" + sessionStartTime +
                ", sessionEndTime=" + sessionEndTime +
                '}';
    }

    public boolean isSavedInDb() {
        return isSavedInDb;
    }

    public void setSavedInDb(boolean isSavedInDb) {
        this.isSavedInDb = isSavedInDb;
    }
}
