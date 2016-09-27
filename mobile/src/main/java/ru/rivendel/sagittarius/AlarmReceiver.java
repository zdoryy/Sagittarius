package ru.rivendel.sagittarius;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

import ru.rivendel.sagittarius.classes.CTask;

/**
 * Created by user on 14.09.16.
 */

public class AlarmReceiver extends BroadcastReceiver {

    SoundPool sp;
    MediaPlayer mediaPlayer;

    @SuppressWarnings("deprecation")
    private void playSound(Context context) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            AudioAttributes attributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_GAME)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .build();
//            sp = new SoundPool.Builder()
//                    .setAudioAttributes(attributes)
//                    .build();
//        } else {
//            sp = new SoundPool(5,AudioManager.STREAM_MUSIC,0);
//        }
//
//        try {
//            int sound  = sp.load(context.getAssets().openFd("waking.mp3"), 1);
//            sp.play(sound, 1, 1, 0, 0, 1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        aum = (AudioManager) mainContext.getSystemService(Context.AUDIO_SERVICE);
//        int maxVol = aum.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//
//        if (aum.getStreamVolume(AudioManager.STREAM_RING) == 0) return;
//
//        int vol = Settings.volume * maxVol / 100;
//        aum.setStreamVolume(AudioManager.STREAM_MUSIC,vol,0);

        mediaPlayer = MediaPlayer.create(context,R.raw.waking);
        mediaPlayer.setLooping(false);

        mediaPlayer.setVolume(1,1);

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            public void onPrepared(MediaPlayer mp)
            {
                mp.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                mp.release();
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle param = intent.getExtras();

        if (param.getString("alarm").equalsIgnoreCase("MakeNextDay")) {
            AlarmService.makeNotifications(context);
            return;
        }

        if (param.getString("alarm").equalsIgnoreCase("task")) {
            if (Environment.db == null) Environment.db = new Database(context);    // убрать коллизию с основной активностью по db
            CTask task = new CTask(param.getInt("id"));
            if (task.findRegisterByPeriod(new DateManager()) != null) return;
        }

        // проверяем актуальность данного увдл и режим дня

//        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Sagittarius");
//        wl.acquire();

        Settings.init(context.getSharedPreferences("Sagittarius",context.MODE_PRIVATE));

        Resources res = context.getResources();
        NotificationManager nm = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent ntfIntent = new Intent(context, NotificationActivity.class);
        ntfIntent.putExtras(param);
        PendingIntent pi = PendingIntent.getActivity(context,0,ntfIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context); // вопрос с Compat и часами v.4

        builder.setContentIntent(pi)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                // большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.button_yes))
                //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker("Уведомление")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle(param.getString("title"))
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText(param.getString("text")); // Текст уведомления

        // Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();
        nm.notify("alarm",param.getInt("id"),notification);

        if (Settings.enableNotificationSound) playSound(context);

        if (Settings.enableNotificationActivity) {
            Intent alarmIntent = new Intent(context, NotificationActivity.class);
            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            alarmIntent.putExtras(param);
            context.startActivity(alarmIntent);
        }

        // Разблокируем поток где-то наверное потом
        //wl.release();

    }

}
