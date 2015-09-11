package com.dhammalab.satipatthna.domain;

/**
 * Created by Tretyakov on 14.07.2014.
 */
public class Bin {
    private long startTime;
    private long endTime;
    private int value;

    public Bin(long startTime, long endTime, int value) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.value = value;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public int getValue() {
        return value;
    }
}
