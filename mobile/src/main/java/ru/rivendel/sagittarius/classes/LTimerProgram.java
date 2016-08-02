package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;
import ru.rivendel.sagittarius.Environment;

/**
 * Created by elanse on 02.08.16.
 */
public class LTimerProgram extends ADataEventObject {
    public int _id_task;
    public String tableName = Database.tableTimerProgram;

    private List<CTimerProgram> intervals;
    private ContentValues cv;

    public LTimerProgram()
    {
        intervals = new ArrayList();
        cv = new ContentValues();
    }
    public LTimerProgram(int _id_task)
    {
        this();
        loadMe(_id_task);
    }

    public List<CTimerProgram> getCollection()
    {
        return intervals;
    }

    public int loadMe(int _id_task)
    {
        int res = 0;
        try
        {
            Cursor c =
                    Environment.db.getReadableDatabase().query(
                            tableName, null, "task = ?",
                            new String[]{String.valueOf(_id_task)},
                            null, null, null
                    );
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        CTimerProgram tp = new CTimerProgram();
                        tp._id = c.getInt(c.getColumnIndex("_id"));
                        tp._id_task = c.getInt(c.getColumnIndex(Database.tableTimerProgramIDTask));
                        tp._order = c.getInt(c.getColumnIndex(Database.tableTimerProgramOrder));
                        tp.title = c.getString(c.getColumnIndex(Database.tableTimerProgramTitle));
                        tp.advance_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramAdvanceSound));
                        tp.finish_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramFinishSound));
                        tp.waking_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramWakingSound));
                        intervals.add(tp);
                    } while (c.moveToNext());
                }
                c.close();
                this._id_task = _id_task;
                if (onLoadedListener !=null) onLoadedListener.onAfterLoaded();
            }
        }
        catch (Exception ex) {
            res = -1;
        }
        return res;
    }

    public int saveMe()
    {
        int res=0;
        try
        {
            for (CTimerProgram tp:intervals)
                tp.saveMe();
            if (onSavedListener !=null) onSavedListener.onAfterSaved();
        }
        catch (Exception ex)
        {
            res=-1;
        }
        return res;
    }
}
