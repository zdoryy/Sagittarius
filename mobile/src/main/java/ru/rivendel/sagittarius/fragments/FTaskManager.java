package ru.rivendel.sagittarius.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ru.rivendel.sagittarius.DateManager;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.ATaskListAdapter;
import ru.rivendel.sagittarius.classes.CRegister;
import ru.rivendel.sagittarius.classes.CTask;
import ru.rivendel.sagittarius.classes.CTopic;
import ru.rivendel.sagittarius.classes.LRegister;
import ru.rivendel.sagittarius.classes.LTask;
import ru.rivendel.sagittarius.dialogs.CStringDialog;
import ru.rivendel.sagittarius.dialogs.CTaskDialog;

/**
 * Created by user on 15.08.16.
 */

public class FTaskManager extends CFragment implements CTaskDialog.OnSaveListener, CStringDialog.OnEnterListener {

    public enum TaskDetailType {Report,Notes,Setup,Alaram};

    private CTask.TaskPeriodType periodTab = CTask.TaskPeriodType.Day;
    private TaskDetailType detailTab = TaskDetailType.Report;

    private CTask selectedTask;

    private TaskListAdapter taskListAdapter;
    private RegisterListAdapter registerListAdapter;

    private static final int STRING_DIALOG_MODE_TOPIC = 1;

    // адаптер для списка задач
    class TaskListAdapter extends ATaskListAdapter {

        public TaskListAdapter(LayoutInflater inflater) {
            super(inflater);
        }

        public void updateList(CTopic topic, CTask.TaskPeriodType period) {
            taskList = new LTask(topic._id,CTask.TaskModeType.Task,period);
        }

        public View getView(int position, View view, ViewGroup parent) {

            final CTask item = getItem(position);
            view = mInflater.inflate(android.R.layout.simple_list_item_2, parent, false);

            TextView titleView = (TextView) view.findViewById(android.R.id.text1);
            titleView.setText(item.title);

//            Button checkButton = (Button) view.findViewById(R.id.check_task_button);
//            checkButton.setVisibility(View.INVISIBLE);

            view.setTag(item);

            return view;

        }

    }

    // адаптер для списка выполнения
    class RegisterListAdapter extends BaseAdapter {

        protected LRegister regList;
        protected final LayoutInflater mInflater;

        public RegisterListAdapter(LayoutInflater inflater) {
            super();
            mInflater = inflater;
        }

        public void updateList() {
            if (selectedTask == null) regList = new LRegister();
            else regList = new LRegister(selectedTask);
        }

        @Override
        public int getCount() {
            return regList.size();
        }

        @Override
        public Object getItem(int position) {
            return regList.getList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View view, ViewGroup parent) {

            final CRegister reg = (CRegister) getItem(position);
            view = mInflater.inflate(R.layout.item_register, parent, false);

            TextView titleView = (TextView) view.findViewById(R.id.reg_title);
            titleView.setText(Long.toString(reg.time));

            TextView commentView = (TextView) view.findViewById(R.id.reg_comment);
            titleView.setText(reg.comment);

            return view;

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_task_manager, container, false);

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

        taskListAdapter = new TaskListAdapter(inflater);
        taskListAdapter.updateList(Environment.topicList.topic,periodTab);

        ListView taskList = (ListView) view.findViewById(R.id.task_list);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTask = (CTask) view.getTag();
                registerListAdapter.updateList();
                registerListAdapter.notifyDataSetChanged();
            }
        });

        taskList.setAdapter(taskListAdapter);

        final Button tab1 = (Button) view.findViewById(R.id.tab1);
        final Button tab2 = (Button) view.findViewById(R.id.tab2);
        final Button tab3 = (Button) view.findViewById(R.id.tab3);
        final Button tab4 = (Button) view.findViewById(R.id.tab4);

        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodTab = CTask.TaskPeriodType.Single;
                updateView(view);
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodTab = CTask.TaskPeriodType.Day;
                updateView(view);
            }
        });

        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodTab = CTask.TaskPeriodType.Week;
                updateView(view);
            }
        });
        tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodTab = CTask.TaskPeriodType.Month;
                updateView(view);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_task_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTaskDialog dialog = CTaskDialog.newInstance(CTask.TaskModeType.Task,periodTab,getFragmentID());
                dialog.show(getFragmentManager(),"AddTask");
            }
        });

        registerListAdapter = new RegisterListAdapter(inflater);
        registerListAdapter.updateList();

        ListView detailList = (ListView) view.findViewById(R.id.detail_list);
        detailList.setAdapter(registerListAdapter);

        updateView(view);
        return view;

    }

    public void updateView(View view) {

        TextView topicText = (TextView) view.findViewById(R.id.topicText);
        topicText.setText(Environment.topicList.topic.title);

        Button tab1 = (Button) view.findViewById(R.id.tab1);
        tab1.setSelected(false);
        Button tab2 = (Button) view.findViewById(R.id.tab2);
        tab2.setSelected(false);
        Button tab3 = (Button) view.findViewById(R.id.tab3);
        tab3.setSelected(false);
        Button tab4 = (Button) view.findViewById(R.id.tab4);
        tab4.setSelected(false);

        switch (periodTab) {
            case Single: tab1.setSelected(true); break;
            case Day: tab2.setSelected(true); break;
            case Week: tab3.setSelected(true); break;
            case Month: tab4.setSelected(true); break;
        }

        updateTaskList();

    }

    public void updateTaskList() {
        taskListAdapter.updateList(Environment.topicList.topic,periodTab);
        taskListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskSave() {
        updateTaskList();
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
