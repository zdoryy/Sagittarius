package ru.rivendel.sagittarius.classes;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Environment;

/**
 * Created by user on 01.08.16.
 */

public abstract class ADataSet < AnyType > {

    protected List< AnyType > list;

    public ADataSet()
    {
        super();
        list = new ArrayList();
    }

    public ADataSet(String sql, String[] arg)
    {
        this();
        loadMe(sql,arg);
    }

    abstract AnyType getNew();

    void addItem(Cursor cursor) {
        ADataEntity item  = (ADataEntity) getNew();
        item.cursorToFields(cursor);
        list.add((AnyType) item);
    }

    // выбирает список из БД по SQL запросу
    public int loadMe(String sql,String[] arg)
    {

        int res=0;
        try {
            SQLiteDatabase db = Environment.db.getReadableDatabase();
            Cursor cursor = db.rawQuery(sql, arg);

            if (cursor != null) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    addItem(cursor);
                    cursor.moveToNext();
                }
                cursor.close();
            }
        }
        catch(SQLException sql_ex)
        {
            res=-1;
        }
        return res;

    }

    // сохряняет весь список в БД
    public int saveMe()
    {

        for (AnyType item: list)
        {
            ADataEntity data = (ADataEntity) item;
            if (data.saveMe() == -1) return -1;
        }
        return 1;
    }

    public List<AnyType> getList()
    {
        return list;
    }

    public int size() {
        return list.size();
    }

}
