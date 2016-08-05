package ru.rivendel.sagittarius.classes;

import android.database.Cursor;

import ru.rivendel.sagittarius.Database;

/**
 * Created by elanse on 02.08.16.
 */
public class CTimerProgram extends ADataEntity{

    int _id_task;
    String title;
    int _order;
    String waking_sound;
    String advance_sound;
    String finish_sound;
    LTimerInterval ti;

    public CTimerProgram()
    {
        super(Database.tableTimerProgram);
    }

    public CTimerProgram(int _id)
    {
        this();
        loadMe(_id);
        this._id = _id;
    }

    public LTimerInterval getTimerInterval()
    {
        if (ti==null)  ti=new LTimerInterval(_id);
        return ti;
    }

    @Override
    int cursorToFields(Cursor c) {
        int res=0;
        try
        {
            _id = c.getInt(c.getColumnIndex("_id"));
            _id_task = c.getInt(c.getColumnIndex(Database.tableTimerProgramIDTask));
            _order = c.getInt(c.getColumnIndex(Database.tableTimerProgramOrder));
            title = c.getString(c.getColumnIndex(Database.tableTimerProgramTitle));
            waking_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramWakingSound));
            advance_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramAdvanceSound));
            finish_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramFinishSound));
        }
        catch (Exception ex)
        {
            res=-1;
        }
        return res;
    }

    @Override
    void setData() {
        if (_id>0) cv.put("_id",_id);
        cv.put(Database.tableTimerProgramIDTask, _id_task);
        cv.put(Database.tableTimerProgramOrder,_order);
        cv.put(Database.tableTimerProgramTitle,title);
        cv.put(Database.tableTimerProgramWakingSound,waking_sound);
        cv.put(Database.tableTimerProgramAdvanceSound,advance_sound);
        cv.put(Database.tableTimerProgramFinishSound,finish_sound);
    }
}
