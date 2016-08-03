package ru.rivendel.sagittarius.classes;

import android.database.Cursor;
import ru.rivendel.sagittarius.Database;

/**
 * Created by elanse on 02.08.16.
 */
public class LTimerProgram extends ADataSetEx<CTimerProgram> {

    public LTimerProgram()
    {
        super(Database.tableTimerProgram);
    }
    public LTimerProgram(int _id_task)
    {
        super(Database.tableTimerProgram,String.format("task=%d",_id_task));
    }

    @Override
    void addItem(Cursor cursor) {
        CTimerProgram tp = new CTimerProgram();
        tp.cursorToFields(cursor);
        list.add(tp);
    }
}
