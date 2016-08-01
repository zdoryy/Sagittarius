package classes;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elanse on 31.07.16.
 */
public class LTimerInterval {

    private List<LTimerInterval> intervals;
    private ContentValues cv;
    public LTimerInterval()
    {
        intervals = new ArrayList<>();
        cv = new ContentValues();
    }
    public LTimerInterval(int _id_program)
    {
        this();
        loadMe(_id_program);
        //...
    }
    public List<LTimerInterval> getCollection()
    {
        return intervals;
    }
    public int loadMe(int _id_program)
    {
        //...
        return 0;
    }
    public int saveMe()
    {
        //...
        return 0;
    }
    /*
    ...
    bla bla bla
     */
}
