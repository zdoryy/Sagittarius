package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;

/**
 * Created by user on 01.08.16.
 */

public class LTask extends ADataSet <CTask> {

    public LTask()
    {
        super();
    }

    // этот конструктор загружает из БД список c отбором по ключу Topic
    public LTask(int _topic)
    {
        super("SELECT "+" * "+" from "+ Database.tableTask+" WHERE "+ Database.tableTaskIDTopic + "=? ORDER BY _order",
                new String[] {Integer.toString(_topic)});
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
