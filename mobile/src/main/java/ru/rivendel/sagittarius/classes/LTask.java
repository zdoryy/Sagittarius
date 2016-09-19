package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;
import ru.rivendel.sagittarius.DateManager;

/**
 * Created by user on 01.08.16.
 */

public class LTask extends ADataSet <CTask> {

    public LTask()
    {
        super();
    }

    // этот конструктор загружает из БД список c отбором по ключам Topic Mode
    public LTask(int topic, CTask.TaskModeType mode)
    {
        super("SELECT "+" * "+" from "+ Database.tableTask+" WHERE "+ Database.tableTaskIDTopic +
                        "=? AND "+Database.tableTaskMode+"=? ORDER BY _order",
                new String[] {Integer.toString(topic),Integer.toString(mode.ordinal())});
    }

    // этот конструктор загружает из БД список c отбором по ключам Topic Mode Period
    public LTask(int topic, CTask.TaskModeType mode, CTask.TaskPeriodType period)
    {
        super("SELECT "+" * "+" from "+ Database.tableTask+" WHERE "+ Database.tableTaskIDTopic +
                        "=? AND "+Database.tableTaskMode+"=? AND "+Database.tableTaskPeriod+"=? ORDER BY _order",
                new String[] {Integer.toString(topic),Integer.toString(mode.ordinal()),Integer.toString(period.ordinal())});
    }

    // этот конструктор загружает из БД список c отбором по ключам Mode
    public LTask(CTask.TaskModeType mode)
    {
        super("SELECT "+" * "+" from "+ Database.tableTask+" WHERE "+ Database.tableTaskMode+"=? ORDER BY _order",
                new String[] {Integer.toString(mode.ordinal())});
    }

    @Override
    CTask getNew()
    {
        return new CTask();
    }

    public void addTask(String title)
    {

        CTask task = new CTask(title);
        task.saveMe();
        list.add(task);

    }

}
