package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import ru.rivendel.sagittarius.Database;
import ru.rivendel.sagittarius.Environment;

/**
 * Created by elanse on 31.07.16.
 */

public class CTimerInterval
{
    public int _id;
    public int _id_program;
    public String title;
    public int order;
    public int time;
    public String sound;
    public int waking;
    public int advance;
    public static String tableName = Database.tableTimerInterval;

    private ContentValues cv;

    public CTimerInterval()
    {
        cv = new ContentValues();
    }

    public CTimerInterval(int _id)
    {
        this();
        loadMe(_id);
    }

    private void setData()
    {
        cv.put("_id",_id);
        cv.put(Database.tableTimerIntervalIDProgram,_id_program);
        cv.put(Database.tableTimerIntervalTitle,title);
        cv.put(Database.tableTimerIntervalOrder,order);
        cv.put(Database.tableTimerIntervalTime,time);
        cv.put(Database.tableTimerIntervalSound,sound);
        cv.put(Database.tableTimerIntervalWaking,waking);
        cv.put(Database.tableTimerIntervalAdvance,advance);
    }

    private int cursorToFields(Cursor c)
    {
        int res=0;
        try
        {
            _id = c.getInt(c.getColumnIndex("_id"));
            _id_program = c.getInt(c.getColumnIndex(Database.tableTimerIntervalIDProgram));
            title = c.getString(c.getColumnIndex(Database.tableTimerIntervalTitle));
            order = c.getInt(c.getColumnIndex(Database.tableTimerIntervalOrder));
            time = c.getInt(c.getColumnIndex(Database.tableTimerIntervalTime));
            sound = c.getString(c.getColumnIndex(Database.tableTimerIntervalSound));
            waking = c.getInt(c.getColumnIndex(Database.tableTimerIntervalWaking));
            advance = c.getInt(c.getColumnIndex(Database.tableTimerIntervalAdvance));
        }
        catch (Exception ex)
        {
            res=-1;
        }
        return res;
    }

    public int loadMe(int _id)
    {
        int res=0;
        try
        {
            Cursor c =
                    Environment.db.getWritableDatabase().query(tableName,/*columns*/null,
                            /*selection*/"_id = ?",/*selectionArgs*/new String[] {String.valueOf(_id)},
                            /*groupBy*/null,/*having*/null,/*orderBy*/null);
            if (c!=null)
            {
                this._id = _id;
                cursorToFields(c);
            }
        }
        catch(SQLException sql_ex)
        {
            res=-1;
        }
        return res;
    }

    public int saveMe()
    {
        int retID;
        try{
            setData();
            if (_id>0)
            { // обновление
                Environment.db.getWritableDatabase().update(tableName, cv, "_id = ?",
                    new String[] { String.valueOf(_id) });
                retID = _id;
            }
            else
            {  // вставка
                retID = (int)Environment.db.getWritableDatabase().insert(tableName, null, cv);
                _id=retID;
            }
        }
        catch(SQLException sql_ex)
        {
            retID=-1;
        }
        return retID;
    }
}
