package com.dhammalab.satipatthna.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tretyakov on 15.07.2014.
 */
public class Meditations {

    public static final String DATE_PATTERN = "dd/MM/yyyy";
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(DATE_PATTERN);
    private ArrayList<MeditationDay> days;

    public Meditations() {
        days = new ArrayList<MeditationDay>();
    }

    public void addSession(Session session) {
        long sessionLength = session.getSessionEndTime() - session.getSessionStartTime();
        DateTime sessionStart = new DateTime(session.getSessionEndTime());
        String day = sessionStart.toString(DATE_PATTERN);
        MeditationDay meditationDay = getMeditationDay(day);
        if (meditationDay == null) {
            meditationDay = new MeditationDay(day, sessionLength);
            days.add(meditationDay);
        } else {
            meditationDay.setLength(meditationDay.getLength() + sessionLength);
        }
    }

    private MeditationDay getMeditationDay(String day) {
        for (MeditationDay meditationDay : days) {
            if (meditationDay.getDate().equals(day)) {
                return meditationDay;
            }
        }
        return null;
    }

    public List<MeditationDay> prepareChartData() {
        List<MeditationDay> chartData = new ArrayList<MeditationDay>();
        if (days.isEmpty()) {
            return chartData;
        }
        long now = System.currentTimeMillis();
        Session session = new Session(now);
        session.setSessionEndTime(now);
        addSession(session);
        Collections.sort(days);
        DateTime meditationDayDate = FORMATTER.parseDateTime(days.get(0).getDate());
        while (meditationDayDate.isBeforeNow()) {
            MeditationDay day = getMeditationDay(meditationDayDate.toString(DATE_PATTERN));
            if (day == null) {
                chartData.add(new MeditationDay(meditationDayDate.toString(DATE_PATTERN), 0));
            } else {
                chartData.add(day);
            }
            meditationDayDate = meditationDayDate.plusDays(1);
        }
        return chartData;
    }

    public List<Integer> getMeditatingDaysNumberInRow(List<MeditationDay> days) {
        List<Integer> meditatingDaysNumberInRow = new ArrayList<Integer>();
        int daysNumber = 0;
        for (MeditationDay day : days) {
            if (day.getLength() == 0 && daysNumber != 0) {
                meditatingDaysNumberInRow.add(daysNumber);
                daysNumber = 0;
            } else {
                daysNumber++;
            }
        }
        meditatingDaysNumberInRow.add(daysNumber);
        return meditatingDaysNumberInRow;
    }
}
