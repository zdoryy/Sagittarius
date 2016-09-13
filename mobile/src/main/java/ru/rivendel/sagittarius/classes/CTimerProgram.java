package ru.rivendel.sagittarius.classes;

import android.database.Cursor;

import ru.rivendel.sagittarius.Database;

/**
 * Created by elanse on 02.08.16.
 */
public class CTimerProgram extends ADataEntity{

    public int _id_task;
    public String title;
    int _order;
    String waking_sound;
    String advance_sound;
    String finish_sound;
    public LTimerInterval ti;

    public CTimerProgram()
    {
        super(Database.tableTimerProgram);
        // создаем программу по умолчанию
        title = "Программа таймера";
        waking_sound = getDefaultSound("waking");
        advance_sound = getDefaultSound("advance");
        finish_sound = getDefaultSound("finish");
        ti = new LTimerInterval();
    }

    public CTimerProgram(int _id)
    {
        this();
        if (_id != 0) {
            // загружаем программу из БД
            loadMe(_id);
            this._id = _id;
        }
    }

    public static CTimerProgram newInstance(CTask task)
    {
        if (task._id != 0) {
            LTimerProgram list = new LTimerProgram(task._id);
            if (list.size() > 0) return list.getList().get(0);
            else return new CTimerProgram();
        } else {
            return new CTimerProgram();
        }
    }

    public CTimerProgram(CTask task)
    {
        this();
        if (task._id != 0) {
            LTimerProgram list = new LTimerProgram(task._id);

        }
    }

   static public String getDefaultSound(String sound) {

        return "wake";

    }

    public LTimerInterval getTimerInterval()
    {
        if (ti==null)  ti=new LTimerInterval(_id);
        return ti;
    }

    @Override
    int cursorToFields(Cursor c) {
        int res=0;
        try
        {
            _id = c.getInt(c.getColumnIndex("_id"));
            _id_task = c.getInt(c.getColumnIndex(Database.tableTimerProgramIDTask));
            _order = c.getInt(c.getColumnIndex(Database.tableTimerProgramOrder));
            title = c.getString(c.getColumnIndex(Database.tableTimerProgramTitle));
            waking_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramWakingSound));
            advance_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramAdvanceSound));
            finish_sound = c.getString(c.getColumnIndex(Database.tableTimerProgramFinishSound));
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
        cv.put(Database.tableTimerProgramIDTask, _id_task);
        cv.put(Database.tableTimerProgramOrder,_order);
        cv.put(Database.tableTimerProgramTitle,title);
        cv.put(Database.tableTimerProgramWakingSound,waking_sound);
        cv.put(Database.tableTimerProgramAdvanceSound,advance_sound);
        cv.put(Database.tableTimerProgramFinishSound,finish_sound);
    }

    @Override
    public int loadMe(int _id) {

        if (super.loadMe(_id) > 0) {
            ti = new LTimerInterval(_id);
        } else {
            return -1;
        }

        return 1;

    }

    @Override
    public int saveMe() {

        if (super.saveMe() > 0) {
            for (CTimerInterval item: ti.list) {
                item._id_program = _id;
            }
            return ti.saveMe();
        } else {
            return -1;
        }

   }

}
