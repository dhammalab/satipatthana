package com.dhammalab.satipatthna.domain;

/**
 * Created by Tretyakov on 15.07.2014.
 */
public class MeditationDay implements Comparable<MeditationDay> {

    private String date;

    private long length;

    public MeditationDay(String date, long length) {
        this.date = date;
        this.length = length;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public int compareTo(MeditationDay another) {
        return Meditations.FORMATTER.parseDateTime(date)
                .compareTo(Meditations.FORMATTER.parseDateTime(another.date));
    }
}
