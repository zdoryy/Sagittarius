package ru.rivendel.sagittarius;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created  by user on 30.07.16.
 */
public class Database extends SQLiteOpenHelper {

    static final String dbName="Sagittarius";                            // имя базы данных

    public static final String tableTopic = "topic";                     // таблица разделов-тем
    public static final String tableTask = "task";                       // таблица упражений - заданий
    public static final String tableTimerProgram = "timer_program";      // таблица программ таймера
    public static final String tableTimerInterval = "timer_interval";    // таблица интервалов таймера
    public static final String tableRegister = "register";               // таблица регистрации исполнения
    public static final String tableTerm = "term";                       // таблица терминов-словарь
    public static final String tableAdvice = "advice";                   // таблица рекомендаций
    public static final String tableNote = "note";                       // таблица заметок пользователя

    public static final String tableTopicTitle = "title";                // название темы
    public static final String tableTopicOrder = "_order";               // сортировка

    public static final String tableTaskIDTopic = "topic";               // внешний ключ к таблице Topic
    public static final String tableTaskTitle = "title";                 // название задачи
    public static final String tableTaskOrder = "_order";                // сортировка
    public static final String tableTaskCount = "count";                 // количество повторов задачи
    public static final String tableTaskPeriod = "period";               // периодичность задачи
    public static final String tableTaskMode = "mode";                   // режим задачи 1/2/3
    public static final String tableTaskAlarm = "alarm";                 // режим уведомления
    public static final String tableTaskTime = "time";                   // конкретное время в сек

    static final String tableTimerProgramTitle = "title";                  // название программы
    static final String tableTimerProgramOrder = "_order";                 // сортировка
    static final String tableTimerProgramIDTask = "task";                  // внешний ключ к Task
    static final String tableTimerProgramWakingSound = "waking_sound";     // имя ресурса
    static final String tableTimerProgramAdvanceSound = "advance_sound";   // имя ресурса
    static final String tableTimerProgramFinishSound = "finish_sound";     // имя ресурса

    public static final String tableTimerIntervalIDProgram = "program"; // внешний ключ к таблице TimerProgram
    public static final String tableTimerIntervalTitle = "title";       // название интервала
    public static final String tableTimerIntervalOrder = "_order";      // сортировка
    public static final String tableTimerIntervalTime = "time";         // длительность в сек
    public static final String tableTimerIntervalSound = "sound";       // имя ресурса для finish sound
    public static final String tableTimerIntervalWaking = "waking";     // режим доп. напоминания + или -
    public static final String tableTimerIntervalAdvance = "advance";   // режим предварительного увдл, сек

    public static final String tableRegisterIDTask = "task";            // внешний ключ к таблице Task
    public static final String tableRegisterTime = "time";              // время регистрации в сек с 1970
    public static final String tableRegisterValue = "value";            // числовой параметр
    public static final String tableRegisterComment = "comment";        // строковый комментарий

    public static final String tableTermTitle = "title";                // название
    public static final String tableTermOrder = "_order";               // сортировка
    public static final String tableTermIDParent = "parent";            // ключ к этой же таблице - родитель
    public static final String tableTermIDTopic = "topic";              // внешний ключ к таблице Topic
    public static final String tableTermSource = "source";              // код источника

    public static final String tableAdviceTitle = "title";              // заголовок
    public static final String tableAdviceOrder = "_order";             // сортировка
    public static final String tableAdviceContent = "content";          // содержание
    public static final String tableAdviceIDTerm = "term";              // внешний ключ к таблице Term
    public static final String tableAdviceSource = "source";            // код источника

    public static final String tableNoteTitle = "title";                // заголовок
    public static final String tableNoteContent = "content";            // содержание
    public static final String tableNoteIDTopic = "topic";              // внешний ключ к таблице Topic
    public static final String tableNoteIDTask = "task";                // внешний ключ к таблице Task
    public static final String tableNoteIDTerm = "term";                // внешний ключ к таблице Term
    public static final String tableNoteIDTime = "time";                // время создания в сек с 1970

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
                tableTimerProgramWakingSound + " TEXT, " +
                tableTimerProgramAdvanceSound + " TEXT, " +
                tableTimerProgramFinishSound + " TEXT)");

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