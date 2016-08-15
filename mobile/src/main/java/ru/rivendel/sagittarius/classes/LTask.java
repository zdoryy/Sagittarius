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

    // этот конструктор загружает из БД список c отбором по ключу Topic и Период
    public LTask(int topic, DateManager period)
    {
        super("SELECT "+" * "+" from "+ Database.tableTask+" WHERE "+ Database.tableTaskIDTopic + "=? ORDER BY _order",
                new String[] {Integer.toString(topic)});
    }

    // этот конструктор загружает из БД список c отбором по ключу Topic и Тип период
    public LTask(int topic, CTask.TaskPeriodType period)
    {
        super("SELECT "+" * "+" from "+ Database.tableTask+" WHERE "+ Database.tableTaskIDTopic + "=? AND "+Database.tableTaskPeriod+"=? ORDER BY _order",
                new String[] {Integer.toString(topic),Integer.toString(period.ordinal())});
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
