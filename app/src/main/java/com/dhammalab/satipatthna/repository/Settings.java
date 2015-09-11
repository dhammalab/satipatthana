package com.dhammalab.satipatthna.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.dhammalab.satipatthna.domain.Meditations;
import com.dhammalab.satipatthna.domain.Session;
import com.dhammalab.satipatthna.domain.Template;

/**
 * Created by Ilya on 13.07.2014.
 */
public class Settings {

    private SharedPreferences prefs;

    private Gson gson;

    public Settings(Context context) {
        prefs = context.getSharedPreferences("settings.xml", Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
        gson = new Gson();
    }

    public long getFirstLaunchDay() {
        return prefs.getLong("first_launch_day", -1);
    }

    public void setFirstLaunchDay(long firstLaunchDay) {
        prefs.edit().putLong("first_launch_day", firstLaunchDay).commit();
    }

    public Session getLastSession() {
        String sessionJson = prefs.getString("session", null);
        if (sessionJson == null) {
            return null;
        }
        return gson.fromJson(sessionJson, Session.class);
    }

    public void setLastSession(Session session) {
        String sessionJson = gson.toJson(session);
        prefs.edit().putString("session", sessionJson).commit();
    }

    public int getMeditationTime() {
        return prefs.getInt("meditation_time", 120);
    }

    public void setMeditationTime(int seconds) {
        prefs.edit().putInt("meditation_time", seconds).commit();
    }

    public long getTotalMeditationLength() {
        return prefs.getLong("meditation_length", 0);
    }

    public void setTotalMeditationLength(long meditationLength) {
        prefs.edit().putLong("meditation_length", meditationLength).commit();
    }

    public int getLesson6VisitCount() {
        return prefs.getInt("lesson6visits", 0);
    }

    public void setLesson6VisitCount(int visits) {
        prefs.edit().putInt("lesson6visits", visits).commit();
    }

    public int getWellDoneVisitCount() {
        return prefs.getInt("wellDone", 0);
    }

    public void setWellDoneVisitCount(int visits) {
        prefs.edit().putInt("wellDone", visits).commit();
    }

    public int getAnalyticVisitCount() {
        return prefs.getInt("analytic", 0);
    }

    public void setAnalyticVisitCount(int visits) {
        prefs.edit().putInt("analytic", visits).commit();
    }

    public Meditations getMeditations() {
        String meditationsJson = prefs.getString("meditations", null);
        if (meditationsJson == null) {
            return null;
        }
        return gson.fromJson(meditationsJson, Meditations.class);
    }

    public void setMeditations(Meditations meditations) {
        String meditationsJson = gson.toJson(meditations);
        prefs.edit().putString("meditations", meditationsJson).commit();
    }

    public void setTemplate(Template template) {
        prefs.edit().putInt("template", Template.toInteger(template)).commit();
    }

    public Template getTemplate() {
        return Template.fromInteger(prefs.getInt("template", 0));
    }
}
