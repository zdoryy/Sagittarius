package ru.rivendel.sagittarius;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Date;

import ru.rivendel.sagittarius.classes.CTask;

/**
 * Created by user on 08.08.16.
 */

public class DateManager {

    private Date startTime;
    private Date endTime;
    public CTask.TaskPeriodType period;

    public DateManager() {

        Calendar cldr = Calendar.getInstance();

        cldr.set(Calendar.HOUR_OF_DAY,0);
        cldr.set(Calendar.MINUTE,0);
        cldr.set(Calendar.SECOND,0);
        cldr.set(Calendar.MILLISECOND,0);
        startTime = cldr.getTime();

        cldr.set(Calendar.HOUR_OF_DAY,23);
        cldr.set(Calendar.MINUTE,59);
        cldr.set(Calendar.SECOND,59);
        cldr.set(Calendar.MILLISECOND,0);
        endTime = cldr.getTime();

        period = CTask.TaskPeriodType.Day;

    }

    public void addDays(int count) {

        startTime.setTime(startTime.getTime() + count * 24 * 60 * 60 * 1000);

    }

    public void shiftLeft() {

        addDays(-1);

    }

    public void shiftRight() {

        addDays(1);

    }

    public String toString(Context context) {

        return  DateUtils.formatDateTime(context, startTime.getTime(), DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR);

    }

    public long getStartTime()
    {
        return startTime.getTime() / 1000;
    }

    public long getEndTime()
    {
        return endTime.getTime() / 1000;
    }

    public static long getMillisOfMidnight() {

        Calendar cldr = Calendar.getInstance();

        cldr.set(Calendar.HOUR_OF_DAY,0);
        cldr.set(Calendar.MINUTE,0);
        cldr.set(Calendar.SECOND,0);
        cldr.set(Calendar.MILLISECOND,0);
        long midnight = cldr.getTime().getTime();

        return midnight;

    }

    public static long getMillisFromMidnight() {

        Calendar cldr = Calendar.getInstance();
        long now = cldr.getTime().getTime();

        cldr.set(Calendar.HOUR_OF_DAY,0);
        cldr.set(Calendar.MINUTE,0);
        cldr.set(Calendar.SECOND,0);
        cldr.set(Calendar.MILLISECOND,0);
        long midnight = cldr.getTime().getTime();

        return now - midnight;

    }

    public static int getStartTaskAlarmTime(int time, int alarm) {

        int start = time - alarm; // все в секундах
        if (start < Settings.wakeTime) start = Settings.wakeTime;

        return start;

    }

    public static int getEndTaskAlarmTime(int time, int alarm) {

        int end = time + alarm; // все в секундах
        if (end > Settings.sleepTime) end = Settings.sleepTime;

        return end;

    }

    public static String timeToString(int time) {

        int hour = time / 3600;
        int min = (time - hour*3600) /60;

        return Integer.toString(hour) + ":" + Integer.toString(min);

    }

}
