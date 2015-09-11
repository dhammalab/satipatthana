package com.dhammalab.satipatthna.domain;

/**
 * Created by Tretyakov on 14.07.2014.
 */
public class Event implements Comparable<Event> {
    private NoteType type;

    // since session was started
    private long time;

    private boolean noteAppear;

    public Event() {

    }

    public Event(NoteType type, long time, boolean noteAppear) {
        this.type = type;
        this.time = time;
        this.noteAppear = noteAppear;
    }

    public NoteType getType() {
        return type;
    }

    public void setType(NoteType type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isNoteAppear() {
        return noteAppear;
    }

    public void setNoteAppear(boolean noteAppear) {
        this.noteAppear = noteAppear;
    }

    @Override
    public String toString() {
        return "Event{" +
                "time=" + time +
                ", type=" + type +
                ", noteAppear=" + noteAppear +
                '}';
    }

    @Override
    public int compareTo(Event another) {
        return (int) (time - another.getTime());
    }
}
