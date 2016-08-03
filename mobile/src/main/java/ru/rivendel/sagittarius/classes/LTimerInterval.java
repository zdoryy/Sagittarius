package ru.rivendel.sagittarius.classes;
import android.database.Cursor;
import ru.rivendel.sagittarius.Database;

/**
 * Created by elanse on 31.07.16.
 */
public class LTimerInterval extends ADataSetEx<CTimerInterval> {


    // этот конструктор загружает из БД весь список без отборов
    public LTimerInterval()
    {
        super(Database.tableTimerInterval);
    }
    // а этот с отборами
    public LTimerInterval(int _id_program)
    {
        super(Database.tableTimerInterval,String.format("program=%d",_id_program));

    }
    public int addTimerInterval(int _id_program,String title,int _order,
                                 int time,String sound, int waking, int advance)
    {
        int res;
        CTimerInterval ti = new CTimerInterval();
        ti._id_program=_id_program;
        ti.title = title;
        ti._order = _order;
        ti.time = time;
        ti.sound = sound;
        ti.waking = waking;
        ti.advance = advance;
        res = ti.saveMe();
        if (res>0) list.add(ti);
        return res;
    }

    @Override
    void addItem(Cursor cursor) {
        CTimerInterval ti = new CTimerInterval();
        ti.cursorToFields(cursor);
        list.add(ti);
    }



}
