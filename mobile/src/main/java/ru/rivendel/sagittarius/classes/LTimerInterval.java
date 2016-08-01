package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;

/**
 * Created by elanse on 31.07.16.
 */
public class LTimerInterval  {

    public int _id_program;

    public static String tableName = Database.tableTimerInterval;

    private List<CTimerInterval> intervals;
    private ContentValues cv;
    public LTimerInterval()
    {
        intervals = new ArrayList();
        cv = new ContentValues();
    }
    public LTimerInterval(int _id_program)
    {
        this();
        loadMe(_id_program);
        //...
    }
    public List<CTimerInterval> getCollection()
    {
        return intervals;
    }
    public int loadMe(int _id_program)
    {
        //...
        return 0;
    }
    public int saveMe()
    {
        //...
        for (CTimerInterval ti:intervals)
            ti.saveMe();
        return 0;
    }
    /*
    ...
    bla bla bla
     */
}
