package ru.rivendel.sagittarius.classes;

import android.database.Cursor;
import ru.rivendel.sagittarius.Database;
import ru.rivendel.sagittarius.DateManager;


/**
 * Created by user on  01.08.16.
 */

public class CRegister extends ADataEntity {

    public int _id_task;
    public long time;
    public int value;
    public String comment;

    public CRegister()
    {
        super(Database.tableRegister);
        _id = 0;
    }

    public CRegister(CTask task, DateManager date)
    {
        this();
        _id_task = task._id;
        time = date.getStartTime();
    }

    public CRegister(int _id)
    {
        this();
        loadMe(_id);
    }

    public static CRegister registerTask(CTask task, DateManager date)
    {
        CRegister reg = new CRegister(task,date);
        reg.saveMe();
        return reg;
    }

    protected void setData()
    {
        if (_id>0) cv.put("_id",_id);
        cv.put(Database.tableRegisterIDTask,_id_task);
        cv.put(Database.tableRegisterTime,time);
        cv.put(Database.tableRegisterValue,value);
        cv.put(Database.tableRegisterComment,comment);
    }

    protected int cursorToFields(Cursor c)
    {
        int res=0;
        try
        {
            _id = c.getInt(c.getColumnIndex("_id"));
            _id_task = c.getInt(c.getColumnIndex(Database.tableRegisterIDTask));
            time = c.getLong(c.getColumnIndex(Database.tableRegisterTime));
            value = c.getInt(c.getColumnIndex(Database.tableRegisterValue));
            comment = c.getString(c.getColumnIndex(Database.tableRegisterComment));
        }
        catch (Exception ex)
        {
            res=-1;
        }
        return res;
    }

}
