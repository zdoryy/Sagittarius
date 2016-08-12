package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Database;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.Settings;

/**
 * Created by user on 02.08.16.
 */

public class LTopic extends ADataSet <CTopic> {

    private int pointer;
    public CTopic topic;

    // этот конструктор загружает из БД весь список без отборов
    public LTopic()
    {
        super("SELECT * from "+ Database.tableTopic+" ORDER BY _order",new String[]{});
        if (size() == 0) addTopic("General topic");
        if (Settings.topicID == 0) {
            pointer = 0;
            topic = list.get(0);
            Settings.topicID = topic._id;
            Settings.saveSettings();
        } else {
            pointer = -1;
            for (int i = 0; i < size(); i++) {
                if (list.get(i)._id == Settings.topicID) {
                    pointer = i;
                    topic = list.get(i);
                }
            }
            if (pointer == -1) {
                pointer = 0;
                topic = list.get(0);
            }
        }
    }

    public boolean shiftLeft() {

        if (pointer > 0) {
            pointer = pointer - 1;
            topic = list.get(pointer);
            Settings.topicID = topic._id;
            Settings.saveSettings();
            return true;
        }
        else {
            return false;
        }

    }

    public boolean shiftRight() {

        if (pointer < size() - 1) {
            pointer = pointer + 1;
            topic = list.get(pointer);
            Settings.topicID = topic._id;
            Settings.saveSettings();
            return true;
        }
        else {
            return false;
        }

    }

    @Override
    CTopic getNew()
    {
        return new CTopic();
    }

    public void addTopic(String title)
    {
        CTopic item = new CTopic(title);
        item.saveMe();
        list.add(item);
        topic = item;
        pointer = size()-1;
        Settings.topicID = topic._id;
        Settings.saveSettings();
    }

}
