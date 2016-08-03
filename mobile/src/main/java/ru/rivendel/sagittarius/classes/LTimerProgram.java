package ru.rivendel.sagittarius.classes;

import android.database.Cursor;
import ru.rivendel.sagittarius.Database;

/**
 * Created by elanse on 02.08.16.
 */
public class LTimerProgram extends ADataSet<CTimerProgram> {

    public LTimerProgram()
    {
        super();
    }

    public LTimerProgram(int _id_task)
    {
        super("SELECT "+" * "+" from "+ Database.tableTimerProgram+" WHERE "+ Database.tableTimerProgramIDTask + "=? ORDER BY _order",
                new String[] {Integer.toString(_id_task)});
    }

    @Override
    CTimerProgram getNew() {
        return new CTimerProgram();
    }


}
