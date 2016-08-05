package ru.rivendel.sagittarius;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import ru.rivendel.sagittarius.classes.CTimerInterval;
import ru.rivendel.sagittarius.classes.CTimerProgram;

/**
 * Created by elanse on 05.08.16.
 */
public class AdvancedTimer {
    private CTimerProgram tp;
    private CountDownTimer t;
    private int id_program;
    private int indexTimer;
    private long interval;
    private OnTimerQueueListener onTimerListener;
    private SoundPool sp;
    private int wakingSound;
    private int advanceSound;
    private final int MAX_STREAMS = 5;
    private MainActivity instance;

    public AdvancedTimer(int id_program, MainActivity instance)
    {
        tp = new CTimerProgram(id_program);
        this.id_program = id_program;
        this.instance = instance;
        createSoundPool();
    }

    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
        loadSounds();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sp = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        sp = new SoundPool(MAX_STREAMS,AudioManager.STREAM_MUSIC,0);
    }

    private void loadSounds()
    {
        try {
            wakingSound  = sp.load(instance.getAssets().openFd("waking.mp3"), 1);
            advanceSound  = sp.load(instance.getAssets().openFd("click.ogg"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int run()
    {
        mainLoop();
        return 0;
    }

    private int mainLoop()
    {
        if (indexTimer<tp.getTimerInterval().getList().size())
        {
            final CTimerInterval ti = tp.getTimerInterval().getList().get(indexTimer);
            if (ti.waking<0)
                floatingRateGen(ti.time,ti.waking);
            nextInterval(ti.time,0,ti.waking);
            if (onTimerListener!=null) onTimerListener.onTimerBegin(ti);
            t = new CountDownTimer(ti.time*1000,1000) {
                @Override
                public void onTick(long lRemaining) {
                    long passedTime = ti.time*1000 - lRemaining;
                    // звук
                    if (ti.advance>0&&(lRemaining/1000)<=(ti.advance+1))
                    {
                        instance.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sp.play(advanceSound, 1, 1, 0, 0, 1);
                                Log.d("myLogs","ADVANCE SOUND");
                            }
                        });
                        ti.advance=0;
                    }
                    if (passedTime/1000==interval&&ti.waking!=0)
                    {
                        nextInterval(ti.time,passedTime/1000,ti.waking);
                        instance.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sp.play(wakingSound, 1, 1, 0, 0, 1);
                                Log.d("myLogs","WAKING SOUND");
                            }
                        });
                    }
                    Log.d("myLogs","TICK passedTime="+String.valueOf(passedTime/1000)+" interval="+interval);//String.valueOf(getInteval(ti.waking)));
                }
                @Override
                public void onFinish() {
                    // звук финиш
                    Log.d("myLogs","FINISH SOUND");
                    if (onTimerListener!=null) onTimerListener.onTimerEnd(ti);
                    indexTimer+=1;
                    mainLoop();
                }
            }.start();
        }
        else
        {
            if (onTimerListener!=null) onTimerListener.onQueueEnd();
        }
        return 0;
    }

    private int floatingRateGen(int time /*сек.*/, int waking)
    {
//        if (waking==0) return 0;
//        Random r = new Random(System.currentTimeMillis());
//        // длинна интервала в секундах
        int l = time/Math.abs(waking);
//        // для расчета отклонения
//        int n = 8;
//        // отклонение, для нормального распределения
//        int dl = l/n;
//        // генерируем длины промежутков между срабатываниями таймера
//        int _dl = -dl + r.nextInt(2*dl + 1);
//        return l+_dl;
        Random r = new Random(System.currentTimeMillis());
        int rnd = l + (int)r.nextGaussian() * l/8;
        return rnd;
    }

    public void stop()
    {
        if (t!=null) t.cancel();
    }

    private long getInteval(int time, int waking)
    {
        if (waking<0) return floatingRateGen(time,waking);
        else return waking;
    }

    private void nextInterval(int time, long passedTime,int waking)
    {
        interval=getInteval(time,waking)+passedTime;
    }

    public void setOnQueueEndListener(OnTimerQueueListener qel)
    {
        onTimerListener = qel;
    }

    public void addTimerInterval(CTimerInterval ti)
    {
        tp.getTimerInterval().getList().add(ti);
    }

    public void addTimerInterval(int _id_program,String title,int _order,int time,String sound,int waking,int advance)
    {
        CTimerInterval ti = new CTimerInterval();
        ti._id_program = _id_program;
        ti.title = title;
        ti._order = _order;
        ti.time = time;
        ti.sound = sound;
        ti.waking = waking;
        ti.advance = advance;
        tp.getTimerInterval().getList().add(ti);
    }

    public interface OnTimerQueueListener
    {
        void onQueueEnd();
        void onTimerBegin(CTimerInterval ti);
        void onTimerEnd(CTimerInterval ti);
    }
}
