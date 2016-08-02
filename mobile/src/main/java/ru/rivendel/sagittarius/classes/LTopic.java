package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;

/**
 * Created by user on 02.08.16.
 */

public class LTopic extends ADataSet {

    private List<CTopic> list;

    // этот конструктор загружает из БД весь список без отборов
    public LTopic()
    {
        super();
        list = new ArrayList();
        String[] arg = new String[]{};
        String sql = "SELECT "+" * "+" from "+ Database.tableTopic+" ORDER BY _order";
        loadMe(sql,arg);
    }

    public void addTopic(String title) {

        CTopic topic = new CTopic(title);
        topic.saveMe();
        list.add(topic);

    }

    @Override
    ADataEntity[] getList() {

        return (ADataEntity[]) list.toArray();

    }

    @Override
    void addItem(Cursor cursor) {

        CTopic topic = new CTopic();
        topic.cursorToFields(cursor);
        list.add(topic);

    }

}
