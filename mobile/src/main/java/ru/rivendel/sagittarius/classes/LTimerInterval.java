package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;
import ru.rivendel.sagittarius.Environment;

/**
 * Created by elanse on 31.07.16.
 */
public class LTimerInterval extends ADataEventObject {

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
    }
    public List<CTimerInterval> getCollection()
    {
        return intervals;
    }
    public int loadMe(int _id_program) {
        int res = 0;
        try
        {
            Cursor c =
                    Environment.db.getReadableDatabase().query(
                            tableName, null, "_id_program = ?",
                            new String[]{String.valueOf(_id_program)},
                            null, null, null
                    );
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        CTimerInterval ti = new CTimerInterval();
                        ti._id = c.getInt(c.getColumnIndex("_id"));
                        ti._id_program = c.getInt(c.getColumnIndex(Database.tableTimerIntervalIDProgram));
                        ti.title = c.getString(c.getColumnIndex(Database.tableTimerIntervalTitle));
                        ti.order = c.getInt(c.getColumnIndex(Database.tableTimerIntervalOrder));
                        ti.time = c.getInt(c.getColumnIndex(Database.tableTimerIntervalTime));
                        ti.sound = c.getString(c.getColumnIndex(Database.tableTimerIntervalSound));
                        ti.waking = c.getInt(c.getColumnIndex(Database.tableTimerIntervalWaking));
                        ti.advance = c.getInt(c.getColumnIndex(Database.tableTimerIntervalAdvance));
                        intervals.add(ti);
                    } while (c.moveToNext());
                }
                c.close();
                this._id_program = _id_program;
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
            for (CTimerInterval ti:intervals)
                ti.saveMe();
            if (onSavedListener !=null) onSavedListener.onAfterSaved();
        }
        catch (Exception ex)
        {
            res=-1;
        }
        return res;
    }
}
