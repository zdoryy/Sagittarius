package ru.rivendel.sagittarius.classes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import ru.rivendel.sagittarius.Environment;

/**
 * Created by user on 01.08.16.
 */

public abstract class ADataSet {

    public ADataSet()
    {
    }

    abstract ADataEntity[] getList();
    abstract void addItem(Cursor cursor);

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

        ADataEntity[] list = getList();

        for (ADataEntity item: list)
        {
            if (item.saveMe() == -1) return -1;
        }

        return 1;

    }

}
