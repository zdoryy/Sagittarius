package ru.rivendel.sagittarius.classes;

import android.database.Cursor;
import ru.rivendel.sagittarius.Database;

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

    public enum TaskPeriodType {Single,Daily,Weekly,Monthly};
    public enum TaskModeType {Task,Reminder,Check};

    public CTask()
    {
        super(Database.tableTask);
        _id = 0;
        title = "";
        order = 0;
        period = TaskPeriodType.Daily;
        mode = TaskModeType.Task;
    }

    public CTask(String name)
    {
        super(Database.tableTask);
        _id = 0;
        title = name;
        order = 0;
        period = TaskPeriodType.Daily;
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

}
