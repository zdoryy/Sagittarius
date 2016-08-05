package ru.rivendel.sagittarius.classes;
import ru.rivendel.sagittarius.Database;

/**
 * Created by elanse on 31.07.16.
 */
public class LTimerInterval extends ADataSet<CTimerInterval> {
    // этот конструктор загружает из БД весь список без отборов
    public LTimerInterval()
    {
        super();
    }
    // а этот с отборами
    public LTimerInterval(int id_program)
    {
        super(String.format("SELECT * FROM %s1$ where "+Database.tableTimerIntervalIDProgram+"=?",
                Database.tableTimerInterval),new String[] {String.valueOf(id_program)});
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
    CTimerInterval getNew() {
        return new CTimerInterval();
    }
}
