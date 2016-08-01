package ru.rivendel.sagittarius.classes;

/**
 * Created by elanse on 01.08.16.
 */
// для событий внутри классов мапперов
public class ADataEventObject {

    public interface OnAfterLoadedDataListener
    {
        void onAfterLoaded();
    }

    public interface OnAfterSavedDataListener
    {
        void onAfterSaved();
    }

    public interface OnAfterDeletedDataListener
    {
        void onAfterDeleted();
    }

    protected OnAfterLoadedDataListener onLoadedListener;
    protected OnAfterSavedDataListener onSavedListener;
    protected OnAfterDeletedDataListener onDeletedListener;

    public void setAfterLoadedDataListener(OnAfterLoadedDataListener listener)
    {
        this.onLoadedListener = listener;
    }

    public void setAfterSavedDataListener(OnAfterSavedDataListener listener)
    {
        this.onSavedListener = listener;
    }

    public void setAfterDeletedData(OnAfterDeletedDataListener listener)
    {
        this.onDeletedListener = listener;
    }
}
