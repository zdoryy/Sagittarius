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

    protected OnAfterLoadedDataListener loadedListener;
    protected OnAfterSavedDataListener savedListener;
    protected OnAfterDeletedDataListener deletedListener;

    public void setAfterLoadedDataListener(OnAfterLoadedDataListener listener)
    {
        this.loadedListener = listener;
    }

    public void setAfterSavedDataListener(OnAfterSavedDataListener listener)
    {
        this.savedListener = listener;
    }

    public void setAfterDeletedData(OnAfterDeletedDataListener listener)
    {
        this.deletedListener = listener;
    }
}
