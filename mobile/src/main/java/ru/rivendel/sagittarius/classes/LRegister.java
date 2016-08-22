package ru.rivendel.sagittarius.classes;

import ru.rivendel.sagittarius.Database;

/**
 * Created by user on 21.08.16.
 */

public class LRegister extends ADataSet <CRegister> {

    public LRegister()
    {
        super();
    }

    // этот конструктор загружает из БД список c отбором по ключам Topic Mode
    public LRegister(CTask task)
    {
        super("SELECT "+" * "+" from "+ Database.tableRegister+" WHERE "+Database.tableRegisterIDTask+"=? ORDER BY time",
                new String[] {Integer.toString(task._id)});
    }

    @Override
    CRegister getNew() {
        return new CRegister();
    }
}
