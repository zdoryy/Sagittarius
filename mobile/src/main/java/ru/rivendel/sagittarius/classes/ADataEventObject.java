package ru.rivendel.sagittarius.classes;

/**
 * Created by elanse on 01.08.16.
 */
// для событий внутри классов мапперов
public class ADataEventObject {

    public interface OnDataLoadListener
    {
        void onDataLoad();
    }

    public interface OnDataSaveListener
    {
        void onDataSave();
    }

    protected OnDataLoadListener loadListener;
    protected OnDataSaveListener saveListener;

    public void setLoadDataListener(OnDataLoadListener listener)
    {
        this.loadListener = listener;
    }

    public void setSaveDataListener(OnDataSaveListener listener)
    {
        this.saveListener = listener;
    }
}
