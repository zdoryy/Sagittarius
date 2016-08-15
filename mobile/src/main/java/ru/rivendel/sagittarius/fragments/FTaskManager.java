package ru.rivendel.sagittarius.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import ru.rivendel.sagittarius.DateManager;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.MainActivity;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.ATaskListAdapter;
import ru.rivendel.sagittarius.classes.CTask;
import ru.rivendel.sagittarius.classes.CTopic;
import ru.rivendel.sagittarius.classes.LTask;
import ru.rivendel.sagittarius.dialogs.CStringDialog;
import ru.rivendel.sagittarius.dialogs.CTaskDialog;

/**
 * Created by user on 15.08.16.
 */

public class FTaskManager extends CFragment implements CTaskDialog.OnSaveListener {

    public enum TaskDetailType {Report,Notes,Setup,Alaram};

    private CTask.TaskPeriodType periodTab = CTask.TaskPeriodType.Daily;
    private TaskDetailType detailTab = TaskDetailType.Report;

    private TaskListAdapter taskListAdapter;

    // адаптер для списка задач
    class TaskListAdapter extends ATaskListAdapter {

        public TaskListAdapter(LayoutInflater inflater) {
            super(inflater);
        }

        public void updateList(CTopic topic, CTask.TaskPeriodType period) {
            taskList = new LTask(topic._id,period);
        }

        public View getView(int position, View view, ViewGroup parent) {

            final CTask item = getItem(position);
            view = mInflater.inflate(R.layout.item_task, parent, false);

            TextView titleView = (TextView) view.findViewById(R.id.task_title);
            titleView.setText(item.title);

            Button openTaskButton = (Button) view.findViewById(R.id.open_task_button);

            openTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CTaskDialog dialog = CTaskDialog.newInstance(item._id,getFragmentID());
                    dialog.show(getFragmentManager(),"EditTask");
                }
            });

            ImageButton openTimerButton = (ImageButton) view.findViewById(R.id.timer_button);
            openTimerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.setContent(new FTimer());
                }
            });

            return view;

        }

    }

    // адаптер для дополнительного списка детализации
    class DetailListAdapter extends ATaskListAdapter {

        public DetailListAdapter(LayoutInflater inflater) {
            super(inflater);
        }

        public void updateList(CTopic topic, DateManager period) {
            taskList = new LTask(topic._id,period);
        }

        public View getView(int position, View view, ViewGroup parent) {

            final CTask item = getItem(position);
            view = mInflater.inflate(R.layout.item_task, parent, false);

            TextView titleView = (TextView) view.findViewById(R.id.task_title);
            titleView.setText(item.title);

            Button openTaskButton = (Button) view.findViewById(R.id.open_task_button);

            openTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CTaskDialog dialog = CTaskDialog.newInstance(item._id,getFragmentID());
                    dialog.show(getFragmentManager(),"EditTask");
                }
            });

            ImageButton openTimerButton = (ImageButton) view.findViewById(R.id.timer_button);
            openTimerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) getActivity();
                    activity.setContent(new FTimer());
                }
            });
//
//            final CheckBox advanceFlag = (CheckBox) view.findViewById(R.id.advanceFlag);
//            advanceFlag.setChecked(item.advance > 0);
//
//            advanceFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
//                    if (flag) item.advance = 60;
//                    else item.advance = 0;
//                }
//            });

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
                CStringDialog dialog = new CStringDialog();
                dialog.setParam("Добавить новую тему","",new CStringDialog.OnEnterListener() {
                    @Override
                    public void onEnter(String str) {
                        Environment.topicList.addTopic(str);
                        updateView(view);
                    }
                });
                dialog.show(getFragmentManager(),"AddTopic");
                return false;
            }
        });

        taskListAdapter = new TaskListAdapter(inflater);
        taskListAdapter.updateList(Environment.topicList.topic,periodTab);

        ListView taskList = (ListView) view.findViewById(R.id.task_list);
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
                periodTab = CTask.TaskPeriodType.Daily;
                updateView(view);
            }
        });

        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodTab = CTask.TaskPeriodType.Weekly;
                updateView(view);
            }
        });
        tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodTab = CTask.TaskPeriodType.Monthly;
                updateView(view);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_task_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CTaskDialog dialog = CTaskDialog.newInstance(periodTab,getFragmentID());
                dialog.show(getFragmentManager(),"AddTask");
            }
        });

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
            case Daily: tab2.setSelected(true); break;
            case Weekly: tab3.setSelected(true); break;
            case Monthly: tab4.setSelected(true); break;
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

}
