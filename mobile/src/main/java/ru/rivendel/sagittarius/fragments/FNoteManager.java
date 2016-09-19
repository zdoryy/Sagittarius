package ru.rivendel.sagittarius.fragments;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ru.rivendel.sagittarius.Database;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.ATaskListAdapter;
import ru.rivendel.sagittarius.classes.CNote;
import ru.rivendel.sagittarius.classes.CTask;
import ru.rivendel.sagittarius.classes.CTopic;
import ru.rivendel.sagittarius.classes.LNote;
import ru.rivendel.sagittarius.classes.LTask;
import ru.rivendel.sagittarius.dialogs.CStringDialog;
import ru.rivendel.sagittarius.dialogs.CTaskDialog;

/**
 * Created by user on 13.09.16.
 */

public class FNoteManager extends CFragment implements CStringDialog.OnEnterListener {

    private NoteListAdapter noteListAdapter;

    private static final int STRING_DIALOG_MODE_TOPIC = 1;

    // адаптер для списка заметок
    class NoteListAdapter extends BaseAdapter {

        private LNote noteList;
        public final LayoutInflater mInflater;

        public NoteListAdapter(LayoutInflater inflater) {
            super();
            mInflater = inflater;
        }

        public int getCount() {
            return noteList.size();
        }

        public CNote getItem(int position) {
            return noteList.getList().get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public void updateList(CTopic topic) {
            //noteList = new LNote(topic);
            noteList = new LNote();
            try {
                Cursor cursor = Environment.db.getWritableDatabase().query(Database.tableAlarm,/*columns*/null,
                            /*selection*/"",/*selectionArgs*/new String[] {},
                            /*groupBy*/null,/*having*/null,/*orderBy*/Database.tableAlarmTime);
                if (cursor != null) {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        CNote item = new CNote();
                        item.title = cursor.getString(cursor.getColumnIndex(Database.tableAlarmTitle));
                        item.time = cursor.getLong(cursor.getColumnIndex(Database.tableAlarmCount));
                        item.content = cursor.getString(cursor.getColumnIndex(Database.tableAlarmDate));
                        noteList.getList().add(item);
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            }
            catch(SQLException sql_ex) {}

        }

        public View getView(int position, View view, ViewGroup parent) {

            final CNote item = getItem(position);
            view = mInflater.inflate(R.layout.item_remainder, parent, false);

            TextView titleView = (TextView) view.findViewById(R.id.task_title);
            titleView.setText(item.title);

            TextView commentView = (TextView) view.findViewById(R.id.task_comment);
            commentView.setText(item.content);

            TextView countView = (TextView) view.findViewById(R.id.task_count);
            countView.setText(Integer.toString((int)item.time));

//            view.setTag(item);
//            registerForContextMenu(view);

            return view;

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_note_manager, container, false);

        Button topicLeftbutton = (Button) view.findViewById(R.id.topic_left_button);
        topicLeftbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Environment.topicList.shiftLeft()) updateView(view);
                else Environment.beep();
            }
        });

        Button topicRightbutton = (Button) view.findViewById(R.id.topic_right_button);
        topicRightbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Environment.topicList.shiftRight()) updateView(view);
                else Environment.beep();
            }
        });
        topicRightbutton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                CStringDialog dialog = CStringDialog.newInstance("Добавить новую тему",STRING_DIALOG_MODE_TOPIC,getFragmentID());
                dialog.show(getFragmentManager(),"AddTopic");
                return true;
            }
        });

        noteListAdapter = new NoteListAdapter(inflater);
        noteListAdapter.updateList(Environment.topicList.topic);

        ListView noteList = (ListView) view.findViewById(R.id.note_list);

//        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedTask = (CTask) view.getTag();
//            }
//        });

        noteList.setAdapter(noteListAdapter);

        final Button tab1 = (Button) view.findViewById(R.id.tab1);
        final Button tab2 = (Button) view.findViewById(R.id.tab2);
        final Button tab3 = (Button) view.findViewById(R.id.tab3);
        final Button tab4 = (Button) view.findViewById(R.id.tab4);

//        tab1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                periodTab = CTask.TaskPeriodType.Single;
//                updateView(view);
//            }
//        });
//
//        tab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                periodTab = CTask.TaskPeriodType.Day;
//                updateView(view);
//            }
//        });
//
//        tab3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                periodTab = CTask.TaskPeriodType.Week;
//                updateView(view);
//            }
//        });
//        tab4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                periodTab = CTask.TaskPeriodType.Month;
//                updateView(view);
//            }
//        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_note_button);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CTaskDialog dialog = CTaskDialog.newInstance(periodTab,getFragmentID());
//                dialog.show(getFragmentManager(),"AddTask");
//            }
//        });

        updateView(view);

        return view;

    }

    public void updateView(View view) {

        TextView topicText = (TextView) view.findViewById(R.id.topicText);
        topicText.setText(Environment.topicList.topic.title);

//        Button tab1 = (Button) view.findViewById(R.id.tab1);
//        tab1.setSelected(false);
//        Button tab2 = (Button) view.findViewById(R.id.tab2);
//        tab2.setSelected(false);
//        Button tab3 = (Button) view.findViewById(R.id.tab3);
//        tab3.setSelected(false);
//        Button tab4 = (Button) view.findViewById(R.id.tab4);
//        tab4.setSelected(false);
//
//        switch (periodTab) {
//            case Single: tab1.setSelected(true); break;
//            case Day: tab2.setSelected(true); break;
//            case Week: tab3.setSelected(true); break;
//            case Month: tab4.setSelected(true); break;
//        }

        updateNoteList();

    }

    public void updateNoteList() {
        noteListAdapter.updateList(Environment.topicList.topic);
        noteListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEnter(String str, int mode) {

        switch (mode) {
            case STRING_DIALOG_MODE_TOPIC: {
                Environment.topicList.addTopic(str);
                updateView(getView());
            }; break;
        }

    }
}
