package ru.rivendel.sagittarius;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 30.07.16. ПРИВЕТ АНВАР !!!
 */
public class Database extends SQLiteOpenHelper {

    static final String dbName="Sagittarius";                     // имя базы данных

    static final String tableTopic = "topic";                     // таблица разделов-тем
    static final String tableTask = "task";                       // таблица упражений - заданий
    static final String tableTimerProgram = "timer_program";      // таблица программ таймера
    static final String tableTimerInterval = "timer_interval";    // таблица интервалов таймера
    static final String tableRegister = "register";               // таблица регистрации исполнения
    static final String tableTerm = "term";                       // таблица терминов-словарь
    static final String tableAdvice = "advice";                   // таблица рекомендаций
    static final String tableNote = "note";                       // таблица заметок пользователя

    static final String tableTopicTitle = "title";                // название темы
    static final String tableTopicOrder = "order";                // сортировка

    static final String tableTaskIDTopic = "topic";               // внешний ключ к таблице Topic
    static final String tableTaskTitle = "title";                 // название задачи
    static final String tableTaskOrder = "order";                 // сортировка
    static final String tableTaskCount = "count";                 // количество повторов задачи
    static final String tableTaskPeriod = "period";               // периодичность задачи
    static final String tableTaskMode = "mode";                   // режим задачи 1/2/3
    static final String tableTaskAlarm = "alarm";                 // режим уведомления
    static final String tableTaskTime = "time";                   // конкретное время в сек

    static final String tableTimerProgramTitle = "title";                   // название программы
    static final String tableTimerProgramOrder = "order";                   // сортировка
    static final String tableTimerProgramIDTask = "task";                   // внешний ключ к Task
    static final String tableTimerIntervalWakingSound = "waking_sound";     // имя ресурса
    static final String tableTimerIntervalAdvanceSound = "advance_sound";   // имя ресурса
    static final String tableTimerIntervalFinishSound = "finish_sound";     // имя ресурса

    static final String tableTimerIntervalIDProgram = "program"; // внешний ключ к таблице TimerProgram
    static final String tableTimerIntervalTitle = "title";       // название интервала
    static final String tableTimerIntervalOrder = "order";       // сортировка
    static final String tableTimerIntervalTime = "time";         // длительность в сек
    static final String tableTimerIntervalSound = "sound";       // имя ресурса для finish sound
    static final String tableTimerIntervalWaking = "waking";     // режим доп. напоминания + или -
    static final String tableTimerIntervalAdvance = "advance";   // режим предварительного увдл, сек

    static final String tableRegisterIDTask = "task";            // внешний ключ к таблице Task
    static final String tableRegisterTime = "time";              // время регистрации в сек с 1970
    static final String tableRegisterValue = "value";            // числовой параметр
    static final String tableRegisterComment = "comment";        // строковый комментарий

    static final String tableTermTitle = "title";                // название
    static final String tableTermOrder = "order";                // сортировка
    static final String tableTermIDParent = "parent";            // ключ к этой же таблице - родитель
    static final String tableTermIDTopic = "topic";                // внешний ключ к таблице Topic
    static final String tableTermSource = "source";              // код источника

    static final String tableAdviceTitle = "title";              // заголовок
    static final String tableAdviceOrder = "order";              // сортировка
    static final String tableAdviceContent = "content";          // содержание
    static final String tableAdviceIDTerm = "term";              // внешний ключ к таблице Term
    static final String tableAdviceSource = "source";            // код источника

    static final String tableNoteTitle = "title";                // заголовок
    static final String tableNoteContent = "content";            // содержание
    static final String tableNoteIDTopic = "topic";              // внешний ключ к таблице Topic
    static final String tableNoteIDTask = "task";                // внешний ключ к таблице Task
    static final String tableNoteIDTerm = "term";                // внешний ключ к таблице Term
    static final String tableNoteIDTime = "time";                // время создания в сек с 1970

    public Database(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + tableTopic + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tableTopicTitle + " TEXT, " +
                tableTopicOrder + " INTEGER)");

        db.execSQL("CREATE TABLE " + tableTask + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tableTaskIDTopic + " INTEGER, " +
                tableTaskTitle + " TEXT, " +
                tableTaskOrder + " INTEGER, " +
                tableTaskCount + " INTEGER, " +
                tableTaskPeriod + " INTEGER, " +
                tableTaskMode + " INTEGER, " +
                tableTaskAlarm + " INTEGER, " +
                tableTaskTime + " INTEGER)");

        db.execSQL("CREATE TABLE " + tableTimerProgram + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tableTimerProgramTitle + " TEXT, " +
                tableTimerProgramOrder + " INTEGER, " +
                tableTimerProgramIDTask + " INTEGER, " +
                tableTimerIntervalWakingSound + " TEXT, " +
                tableTimerIntervalAdvanceSound + " TEXT, " +
                tableTimerIntervalFinishSound + " TEXT)");

        db.execSQL("CREATE TABLE " + tableTimerInterval + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tableTimerIntervalIDProgram + " INTEGER, " +
                tableTimerIntervalTitle + " TEXT, " +
                tableTimerIntervalOrder + " INTEGER, " +
                tableTimerIntervalTime + " INTEGER, " +
                tableTimerIntervalSound + " TEXT, " +
                tableTimerIntervalWaking + " INTEGER, " +
                tableTimerIntervalAdvance + " INTEGER)");

        db.execSQL("CREATE TABLE " + tableRegister + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tableRegisterIDTask + " INTEGER, " +
                tableRegisterTime + " INTEGER, " +
                tableRegisterValue + " INTEGER, " +
                tableRegisterComment + " TEXT)");

        db.execSQL("CREATE TABLE " + tableTerm + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tableTermTitle + " TEXT, " +
                tableTermOrder + " INTEGER, " +
                tableTermIDParent + " INTEGER, " +
                tableTermIDTopic + " INTEGER, " +
                tableTermSource + " INTEGER)");

        db.execSQL("CREATE TABLE " + tableAdvice + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tableAdviceTitle + " TEXT, " +
                tableAdviceOrder + " INTEGER, " +
                tableAdviceContent + " TEXT, " +
                tableAdviceIDTerm + " INTEGER, " +
                tableAdviceSource + " INTEGER)");

        db.execSQL("CREATE TABLE " + tableNote + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                tableNoteTitle + " TEXT, " +
                tableNoteContent + " TEXT, " +
                tableNoteIDTopic + " INTEGER, " +
                tableNoteIDTask + " INTEGER, " +
                tableNoteIDTerm + " INTEGER, " +
                tableNoteIDTime + " INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}