package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import ru.rivendel.sagittarius.Environment;

/**
 * Created by elanse on 01.08.16.
 */
public abstract class ADataEntity {

    public int _id;
    public String tableName;
    protected ContentValues cv;

    protected ADataEntity(String tableName)
    {
        this.tableName = tableName;
        cv = new ContentValues();
    }

    abstract int cursorToFields(Cursor c);
    abstract void setData();

    public int loadMe(int _id)
    {
        int res=0;
        try
        {
            Cursor c =
                    Environment.db.getWritableDatabase().query(tableName,/*columns*/null,
                            /*selection*/"_id = ?",/*selectionArgs*/new String[] {String.valueOf(_id)},
                            /*groupBy*/null,/*having*/null,/*orderBy*/null);
            if (c!=null)
            {
                res = _id;
                cursorToFields(c);
                c.close();
            }
        }
        catch(SQLException sql_ex)
        {
            res=-1;
        }
        return res;
    }

    public int saveMe()
    {
        int retID;
        try{
            setData();
            if (_id>0)
            { // обновление
                Environment.db.getWritableDatabase().update(tableName, cv, "_id = ?",
                        new String[] { String.valueOf(_id) });
                retID = _id;
            }
            else
            {  // вставка
                retID = (int)Environment.db.getWritableDatabase().insert(tableName, null, cv);
                _id=retID;
            }
        }
        catch(SQLException sql_ex)
        {
            retID=-1;
        }
        return retID;
    }
}
