package ru.rivendel.sagittarius.classes;

import android.database.Cursor;
import ru.rivendel.sagittarius.Database;

/**
 * Created by user on  01.08.16.
 */

public class CTopic extends ADataEntity {

    public String title;
    public int order;

    public CTopic()
    {
        super(Database.tableTopic);
        _id = 0;
        title = "Topic";
        order = 0;
    }

    CTopic(int _id)
    {
        this();
        loadMe(_id);
    }

    protected void setData()
    {
        if (_id>0) cv.put("_id",_id);
        cv.put(Database.tableTopicTitle,title);
        cv.put(Database.tableTopicOrder,order);
    }

    protected int cursorToFields(Cursor c)
    {
        int res=0;
        try
        {
            _id = c.getInt(c.getColumnIndex("_id"));
            title = c.getString(c.getColumnIndex(Database.tableTopicTitle));
            order = c.getInt(c.getColumnIndex(Database.tableTopicOrder));
        }
        catch (Exception ex)
        {
            res=-1;
        }
        return res;
    }

}