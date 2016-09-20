package ru.rivendel.sagittarius.classes;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import ru.rivendel.sagittarius.Database;
import ru.rivendel.sagittarius.DateManager;
import ru.rivendel.sagittarius.Environment;

/**
 * Created by user on  01.08.16.
 */

public class CTask extends ADataEntity {

    public int _id_topic;
    public String title;
    public int order;
    public int count;
    public TaskPeriodType period;
    public TaskModeType mode;
    public int alarm;
    public int time;

    public enum TaskPeriodType {Single,Day,Week,Month,Year};
    public enum TaskModeType {Task,Reminder,Check};

    public interface OnSaveListener {
        void onTaskSave();
    }

    public CTask()
    {
        super(Database.tableTask);
        _id = 0;
        title = "";
        order = 0;
        count = 1;
        alarm = -1;
        period = TaskPeriodType.Day;
        mode = TaskModeType.Task;
    }

    public CTask(String name)
    {
        super(Database.tableTask);
        _id = 0;
        title = name;
        order = 0;
        alarm = -1;
        period = TaskPeriodType.Day;
        mode = TaskModeType.Task;
    }

    public CTask(int _id)
    {
        this();
        loadMe(_id);
    }

    public CTask(int topic, TaskModeType _mode, TaskPeriodType _period)
    {
        this();
        _id_topic = topic;
        period = _period;
        mode = _mode;
        count = 1;
        alarm = -1;
    }

    public void setNote(String text)
    {
        if (_id == 0) saveMe();
        LNote list = new LNote(this);
        if (list.size() > 0) {
            CNote note = list.getList().get(0);
            note.content = text;
            note.saveMe();
        } else {
            CNote note = new CNote(text,this);
            note.saveMe();
        }
    }

    public String getNote()
    {
        if (_id == 0) return "";
        else {
            LNote list = new LNote(this);
            if (list.size() > 0) return list.getList().get(0).content;
            else return "";
        }
    }

    protected void setData()
    {
        if (_id>0) cv.put("_id",_id);
        cv.put(Database.tableTaskIDTopic,_id_topic);
        cv.put(Database.tableTaskTitle,title);
        cv.put(Database.tableTaskOrder,order);
        cv.put(Database.tableTaskCount,count);
        cv.put(Database.tableTaskPeriod,period.ordinal());
        cv.put(Database.tableTaskMode,mode.ordinal());
        cv.put(Database.tableTaskAlarm,alarm);
        cv.put(Database.tableTaskTime,time);
    }

    protected int cursorToFields(Cursor c)
    {
        int res=0;
        try
        {
            _id = c.getInt(c.getColumnIndex("_id"));
            _id_topic = c.getInt(c.getColumnIndex(Database.tableTaskIDTopic));
            title = c.getString(c.getColumnIndex(Database.tableTaskTitle));
            order = c.getInt(c.getColumnIndex(Database.tableTaskOrder));
            count = c.getInt(c.getColumnIndex(Database.tableTaskCount));
            period = TaskPeriodType.values()[c.getInt(c.getColumnIndex(Database.tableTaskPeriod))];
            mode = TaskModeType.values()[c.getInt(c.getColumnIndex(Database.tableTaskMode))];
            alarm = c.getInt(c.getColumnIndex(Database.tableTaskAlarm));
            time = c.getInt(c.getColumnIndex(Database.tableTaskTime));
        }
        catch (Exception ex)
        {
            res=-1;
        }
        return res;
    }

    public CRegister findRegisterByPeriod(DateManager period)
    {
        String sql = "SELECT * FROM "+ Database.tableRegister+" " +
                "WHERE "+ Database.tableRegisterIDTask + "=? AND ("+Database.tableRegisterTime+" BETWEEN ? AND ?)";
        String[] arg = new String[] {Integer.toString(_id),Long.toString(period.getStartTime()),Long.toString(period.getEndTime())};
        try {
            SQLiteDatabase db = Environment.db.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, arg);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    CRegister reg  = new CRegister();
                    reg.cursorToFields(cursor);
                    cursor.close();
                    return reg;
                } else {
                    cursor.close();
                    return null;
                }
            } else {
                return null;
            }
        }
        catch(SQLException sql_ex)
        {
            return null;
        }
    }

    public void cancelRegisterByPeriod(DateManager period)
    {
        String sql = "DELETE FROM "+ Database.tableRegister+" " +
                "WHERE "+ Database.tableRegisterIDTask + "=? AND ("+Database.tableRegisterTime+" BETWEEN ? AND ?)";
        String[] arg = new String[] {Integer.toString(_id),Long.toString(period.getStartTime()),Long.toString(period.getEndTime())};
        try {
            SQLiteDatabase db = Environment.db.getWritableDatabase();
            db.execSQL(sql,arg);
        }
        catch(SQLException sql_ex)
        {
        }
    }

    public void commentRegisterByPerid(DateManager period, String comment)
    {
        CRegister reg = findRegisterByPeriod(period);
        if (reg == null) reg = CRegister.registerTask(this,period);
        reg.comment = comment;
        reg.saveMe();
    }

    // TO DO  override deleteMe()

}
