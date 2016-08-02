package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;

/**
 * Created by user on 01.08.16.
 */

public class LTask extends ADataSet {

    private List<CTask> list;

    // этот конструктор загружает из БД список c отбором по ключу Topic
    public LTask(int _topic)
    {
        super();
        list = new ArrayList();
        String[] arg = new String[] {Integer.toString(_topic)};
        String sql = "SELECT "+" * "+" from "+ Database.tableTask+" WHERE "+ Database.tableTaskIDTopic + "=? ORDER BY _order";
        loadMe(sql,arg);
    }

    public void addTask(String title) {

        CTask task = new CTask(title);
        task.saveMe();
        list.add(task);

    }

    @Override
    ADataEntity[] getList() {

        return (ADataEntity[]) list.toArray();

    }

    @Override
    void addItem(Cursor cursor) {

        CTask task = new CTask();
        task.cursorToFields(cursor);
        list.add(task);

    }

}
