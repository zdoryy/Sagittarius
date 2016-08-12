package ru.rivendel.sagittarius;

import android.content.Context;
import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 08.08.16.
 */

public class DateManager {

    enum PeriodType {Day,Week,Month,Year};

    private Date startTime;
    private Date endTime;
    private PeriodType period;

    public DateManager() {

        Calendar cldr = Calendar.getInstance();

        cldr.set(Calendar.HOUR,0);
        cldr.set(Calendar.MINUTE,0);
        cldr.set(Calendar.SECOND,0);
        cldr.set(Calendar.MILLISECOND,0);
        startTime = cldr.getTime();

        cldr.set(Calendar.HOUR,23);
        cldr.set(Calendar.MINUTE,59);
        cldr.set(Calendar.SECOND,59);
        cldr.set(Calendar.MILLISECOND,0);
        endTime = cldr.getTime();

        period = PeriodType.Day;

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

}
