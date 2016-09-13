package ru.rivendel.sagittarius.classes;

import android.database.Cursor;

import java.util.Date;

import ru.rivendel.sagittarius.Database;

/**
 * Created by user on 20.08.16.
 */

public class CNote extends ADataEntity {

    public String title;
    public String content;
    public int _id_topic;
    public int _id_task;
    public int _id_term;
    public long time;

    public CNote()
    {
        super(Database.tableNote);
        _id = 0;
        title = "Заметка";
        Date today = new Date();
        time = today.getTime() / 1000;
    }

    public CNote(int _id)
    {
        this();
        loadMe(_id);
    }

    public CNote(String str,CTask task)
    {
        this();
        content = str;
        _id_task = task._id;
        _id_topic = task._id_topic;
    }

    @Override
    int cursorToFields(Cursor c) {
        int res=0;
        try
        {
            _id = c.getInt(c.getColumnIndex("_id"));
            title = c.getString(c.getColumnIndex(Database.tableNoteTitle));
            content = c.getString(c.getColumnIndex(Database.tableNoteContent));
            _id_topic = c.getInt(c.getColumnIndex(Database.tableNoteIDTopic));
            _id_task = c.getInt(c.getColumnIndex(Database.tableNoteIDTask));
            _id_term = c.getInt(c.getColumnIndex(Database.tableNoteIDTerm));
            time = c.getLong(c.getColumnIndex(Database.tableNoteTime));
        }
        catch (Exception ex)
        {
            res=-1;
        }
        return res;
    }

    @Override
    void setData() {
        if (_id>0) cv.put("_id",_id);
        cv.put(Database.tableNoteTitle,title);
        cv.put(Database.tableNoteContent,content);
        cv.put(Database.tableNoteIDTopic,_id_topic);
        cv.put(Database.tableNoteIDTask,_id_task);
        cv.put(Database.tableNoteIDTerm,_id_term);
        cv.put(Database.tableNoteTime,time);
    }
}
