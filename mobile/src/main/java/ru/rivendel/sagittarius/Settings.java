package ru.rivendel.sagittarius;

import android.content.SharedPreferences;

/**
 * Created by user on 07.08.16.
 */

public class Settings {

    static private SharedPreferences sPref;

    static public int timerProgramID;
    static public int topicID;

    public static void init(SharedPreferences preferences) {
        sPref = preferences;
        loadSettings();
    }

    static public void loadSettings() {
        timerProgramID = sPref.getInt("timerProgramID",0);
        topicID = sPref.getInt("topicID",0);
    }

    static public void saveSettings() {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("timerProgramID",timerProgramID);
        ed.putInt("topicID",topicID);
        ed.commit();
        }

}