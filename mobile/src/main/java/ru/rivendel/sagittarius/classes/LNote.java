package ru.rivendel.sagittarius.classes;

import ru.rivendel.sagittarius.Database;

/**
 * Created by user on 13.09.16.
 */

public class LNote extends ADataSet <CNote> {

    public LNote() {
        super();
    }

    // этот конструктор загружает из БД список c отбором по ключу Task
    public LNote(CTask task)
    {
        super("SELECT "+" * "+" from "+ Database.tableNote+" WHERE "+ Database.tableNoteIDTask + "=? ORDER BY time",
                new String[] {Integer.toString(task._id)});
    }

    // этот конструктор загружает из БД список c отбором по ключу Topic
    public LNote(CTopic topic)
    {
        super("SELECT "+" * "+" from "+ Database.tableNote+" WHERE "+ Database.tableNoteIDTopic + "=? ORDER BY time",
                new String[] {Integer.toString(topic._id)});
    }

    @Override
    CNote getNew() {
        return new CNote();
    }
}
