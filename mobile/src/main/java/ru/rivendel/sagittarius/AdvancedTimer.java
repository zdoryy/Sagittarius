package ru.rivendel.sagittarius;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.classes.CTimerInterval;
import ru.rivendel.sagittarius.classes.LTimerInterval;

/**
 * Created by elanse on 02.08.16.
 */
public class AdvancedTimer {

    public interface OnQueueEndListener
    {
         void onQueueEnd();
    }

    public AdvancedTimer()
    {
        makeQueue();
    }

    public AdvancedTimer(int _id_tp)
    {
        _id_program = _id_tp;
        makeQueue(_id_program);
    }

    private int _id_program;
    private List<CTimerInterval> queue;
    private OnQueueEndListener onQueueEndListener;
    int index;

    private void makeQueue(int _id_program)
    {
        queue = new LTimerInterval(_id_program).getList();
    }

    private void makeQueue()
    {
        queue = new ArrayList();
    }

    private int mainLoop()
    {
        if (index<queue.size()-1)
        {
            CTimerInterval ti = queue.get(index);
            new CountDownTimer(ti.time,ti.advance) {
                @Override
                public void onTick(long l) {
                    // звук
                    Log.d("myLogs","l="+String.valueOf(l) +" tick!");
                }
                @Override
                public void onFinish() {
                    // звук финиш
                    Log.d("myLogs","finish");
                    index+=1;
                    mainLoop();
                }
            }.start();
        }
        else
        {
            if (onQueueEndListener!=null) onQueueEndListener.onQueueEnd();
        }
        return 0;
    }

    public void setOnQueueEndListener(OnQueueEndListener qel)
    {
        onQueueEndListener = qel;
    }

    public void addTimerInterval(CTimerInterval ti)
    {
        queue.add(ti);
    }
    public void addTimerInterval(/*все параметры*/)
    {

    }

    public int saveMe()
    {
        return 0;
    }
}
