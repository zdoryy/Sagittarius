package ru.rivendel.sagittarius.classes;

import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import ru.rivendel.sagittarius.DateManager;

/**
 * Created by user on 11.08.16.
 */

public abstract class ATaskListAdapter extends BaseAdapter {

    protected LTask taskList;
    protected final LayoutInflater mInflater;

    protected ATaskListAdapter(LayoutInflater inflater) {
        super();
        mInflater = inflater;
    }

    abstract public void updateList(CTopic topic, DateManager period);

    public int getCount() {
        return taskList.size();
    }

    public CTask getItem(int position) {
        return taskList.getList().get(position);
    }

    public long getItemId(int position) {
        return position;
    }

}
