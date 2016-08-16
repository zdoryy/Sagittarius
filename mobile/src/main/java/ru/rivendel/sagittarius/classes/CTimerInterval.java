package ru.rivendel.sagittarius.classes;
import android.database.Cursor;
import ru.rivendel.sagittarius.Database;

/**
 * Created by elanse on 31.07.16.
 */

public class CTimerInterval extends ADataEntity
{
    public int _id_program;
    public String title;
    public int _order;
    public int time;
    public String sound;
    public int waking;
    public int advance;

    public CTimerInterval()
    {
        super(Database.tableTimerInterval);
        // создаем интервал по умолчанию
        title = "Интервал 1";
        _order = 1;
        time = 60 * 15;
    }

    public CTimerInterval(LTimerInterval ti)
    {
        this();
        _order = ti.size() + 1;
        //title = "Интервал " + Integer.toString(_order);

    }

    public CTimerInterval(int _id)
    {
        this();
        loadMe(_id);
    }

    protected void setData()
    {
        if (_id>0) cv.put("_id",_id);
        cv.put(Database.tableTimerIntervalIDProgram,_id_program);
        cv.put(Database.tableTimerIntervalTitle,title);
        cv.put(Database.tableTimerIntervalOrder, _order);
        cv.put(Database.tableTimerIntervalTime,time);
        cv.put(Database.tableTimerIntervalSound,sound);
        cv.put(Database.tableTimerIntervalWaking,waking);
        cv.put(Database.tableTimerIntervalAdvance,advance);
    }

    protected int cursorToFields(Cursor c)
    {
        int res=0;
        try
        {
            _id = c.getInt(c.getColumnIndex("_id"));
            _id_program = c.getInt(c.getColumnIndex(Database.tableTimerIntervalIDProgram));
            title = c.getString(c.getColumnIndex(Database.tableTimerIntervalTitle));
            _order = c.getInt(c.getColumnIndex(Database.tableTimerIntervalOrder));
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
}
