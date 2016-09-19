package ru.rivendel.sagittarius;

import android.content.SharedPreferences;

/**
 * Created by user on 07.08.16.
 */

public class Settings {

    static private SharedPreferences sPref;

    static public int timerProgramID;
    static public int topicID;

    static public int wakeTime;   // в секундах от 00-00
    static public int sleepTime;  // в секундах от 00-00

    static public int gaussK;     // коэффициент разброса гауссиана

    static public boolean enableNotificationActivity;
    static public boolean enableNotificationSound;

    static public int alarmCounter;  // счетчик ид алармов

    public static void init(SharedPreferences preferences) {
        sPref = preferences;
        loadSettings();
    }

    static public void loadSettings() {
        timerProgramID = sPref.getInt("timerProgramID",0);
        topicID = sPref.getInt("topicID",0);
        wakeTime = sPref.getInt("wakeTime",9*60*60);
        sleepTime = sPref.getInt("sleepTime",21*60*60);
        gaussK = sPref.getInt("gaussK",1);
        enableNotificationActivity = sPref.getBoolean("enableNotificationActivity",true);
        enableNotificationSound = sPref.getBoolean("enableNotificationSound",true);
        alarmCounter = sPref.getInt("alarmCounter",0);
    }

    static public void saveSettings() {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("timerProgramID",timerProgramID);
        ed.putInt("topicID",topicID);
        ed.putInt("wakeTime",wakeTime);
        ed.putInt("sleepTime",sleepTime);
        ed.putInt("gaussK",gaussK);
        ed.putBoolean("enableNotificationActivity",enableNotificationActivity);
        ed.putBoolean("enableNotificationSound",enableNotificationSound);
        ed.putInt("alarmCounter",alarmCounter);
        ed.commit();
    }

}
