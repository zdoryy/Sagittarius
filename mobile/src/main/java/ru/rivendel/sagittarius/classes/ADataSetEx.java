package ru.rivendel.sagittarius.classes;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.rivendel.sagittarius.Environment;

/**
 * Created by elanse on 03.08.16.
 */
public abstract class ADataSetEx< AnyType > {

    protected List< AnyType > list;

    public ADataSetEx(String tableName)
    {
        super();
        list = new ArrayList();
        String[] arg = new String[]{};
        String sql = String.format("SELECT * from %1s$ ORDER BY _order",tableName);
        loadMe(sql,arg);
    }

    public ADataSetEx(String tableName, String where )
    {
        super();
        list = new ArrayList();
        String[] arg = new String[]{};
        String sql = String.format("SELECT * from %1s$ WHERE %2s$ ORDER BY _order",tableName,where);
        loadMe(sql,arg);
    }

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
        ADataEntity[] list =(ADataEntity[]) getList().toArray();
        for (ADataEntity item: list)
        {
            if (item.saveMe() == -1) return -1;
        }
        return 1;
    }

    public List<AnyType> getList()
    {
        return list;
    }

}
