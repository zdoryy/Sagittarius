package ru.rivendel.sagittarius;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;

import java.util.Calendar;

import ru.rivendel.sagittarius.classes.CTask;
import ru.rivendel.sagittarius.classes.LTask;

public class AlarmService {

    // формирует уведомления за текущий день
    public static void makeNotifications(Context context) {

        Settings.init(context.getSharedPreferences("Sagittarius",context.MODE_PRIVATE));
        Environment.db = new Database(context);    // убрать коллизию с основной активностью по db
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        if (Settings.alarmCounter > 0) {
            for (int id = 1; id <= Settings.alarmCounter; id ++) {
                Intent intent = new Intent(context, AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                am.cancel(pi);
            }
            clearAlarm(); // LOG
        }

        LTask alarmList = new LTask(CTask.TaskModeType.Reminder);

        long now = DateManager.getMillisFromMidnight();
        long midnight = DateManager.getMillisOfMidnight();

        long sys = System.currentTimeMillis();

        int count = 0;

        for (CTask item: alarmList.getList()) {
            int[] timeSet = getTimeSet(item.count);
            for (int time: timeSet) {
                long alarm = (long) time * 1000;
                if (alarm > now) {
                    Intent intent = new Intent(context, AlarmReceiver.class);
                    Bundle param = new Bundle();
                    param.putString("alarm", "reminder");
                    param.putInt("id", item._id);
                    param.putInt("count", count);
                    param.putString("title", item.title);
                    param.putString("text", item.getNote());
                    intent.putExtras(param);
                    count = count + 1;
                    PendingIntent pi = PendingIntent.getBroadcast(context, count, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        am.setExact(AlarmManager.RTC_WAKEUP, midnight + alarm, pi);
                    } else {
                        am.set(AlarmManager.RTC_WAKEUP, midnight + alarm, pi);
                    }
                    registerAlarm(context, item.title,midnight + alarm,count,item._id);  // LOG
                }
            }
        }


        Settings.alarmCounter = count;
        Settings.saveSettings();

        // запускаем себя же на следующий день
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle param = new Bundle();
        param.putString("alarm", "MakeNextDay");
        intent.putExtras(param);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() - now + 25*60*60*1000 , pi); // надо чтобы экран не зажигался
        } else {
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() - now + 25*60*60*1000 , pi); // надо чтобы экран не зажигался
        }

    }

    private static int[] getTimeSet(int count) {

        int day = Settings.sleepTime - Settings.wakeTime;
        int period = day / (count + 1);

        int time = Settings.wakeTime + period;
        int[] list = new int[count];
        for (int i=0; i<count; i++) {
            list[i] = time;
            time = time + period;
        }

        return list;

    }

    private static void registerAlarm(Context context, String title, long time, int count, int task) {

        ContentValues cv = new ContentValues();

        cv.put(Database.tableAlarmTitle,title);
        cv.put(Database.tableAlarmTime,time);
        cv.put(Database.tableAlarmCount,count);
        cv.put(Database.tableAlarmIDTask,task);

        Calendar cldr = Calendar.getInstance();
        cldr.setTimeInMillis(time);

        String frm = DateUtils.formatDateTime(context, time, DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR|DateUtils.FORMAT_SHOW_TIME);
        cv.put(Database.tableAlarmDate,frm);

        Environment.db.getWritableDatabase().insert(Database.tableAlarm, null, cv);

    }

    private static void clearAlarm() {

        Environment.db.getWritableDatabase().delete(Database.tableAlarm,"",new String[]{});

    }

}
