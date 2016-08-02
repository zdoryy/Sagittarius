package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import ru.rivendel.sagittarius.Environment;

/**
 * Created by elanse on 01.08.16.
 */
public abstract class ADataEntity extends ADataEventObject {
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
                if (c.getCount()>0)
                {
                    res = _id;
                    this._id = _id;
                    c.moveToFirst();
                    cursorToFields(c);
                    c.close();
                    if (onLoadedListener !=null) onLoadedListener.onAfterLoaded();
                }
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
            if (onSavedListener !=null) onSavedListener.onAfterSaved();
        }
        catch(SQLException sql_ex)
        {
            retID=-1;
        }
        return retID;
    }

    public int deleteMe()
    {
        int retID=-1;
        try{
            if (_id>0) {
                retID = Environment.db.getWritableDatabase().delete(tableName, "_id = ?",
                        new String[] { String.valueOf(_id) });
                if (onDeletedListener != null) onDeletedListener.onAfterDeleted();
            }
        }
        catch(SQLException sql_ex)
        {
            retID=-1;
        }
        return retID;
    }
}

