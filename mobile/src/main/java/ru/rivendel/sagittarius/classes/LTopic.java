package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;

/**
 * Created by user on 02.08.16.
 */

public class LTopic extends ADataSet <CTopic> {

    // этот конструктор загружает из БД весь список без отборов
    public LTopic()
    {
        super("SELECT "+" * "+" from "+ Database.tableTopic+" ORDER BY _order",new String[]{});
    }

    @Override
    CTopic getNew()
    {
        return new CTopic();
    }

    public void addTopic(String title)
    {

        CTopic topic = new CTopic(title);
        topic.saveMe();
        list.add(topic);

    }

}
