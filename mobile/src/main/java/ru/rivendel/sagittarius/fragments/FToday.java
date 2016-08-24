package ru.rivendel.sagittarius.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ru.rivendel.sagittarius.DateManager;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.MainActivity;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.ADataEntity;
import ru.rivendel.sagittarius.classes.ATaskListAdapter;
import ru.rivendel.sagittarius.classes.CNote;
import ru.rivendel.sagittarius.classes.CRegister;
import ru.rivendel.sagittarius.classes.CTask;
import ru.rivendel.sagittarius.classes.CTopic;
import ru.rivendel.sagittarius.classes.LTask;
import ru.rivendel.sagittarius.dialogs.CStringDialog;
import ru.rivendel.sagittarius.dialogs.CTaskDialog;

/**
 * Created by user on 08.08.16.
 */

public class FToday extends CFragment implements CTaskDialog.OnSaveListener,CStringDialog.OnEnterListener {

    private DateManager datePointer;
    private CTask.TaskModeType taskTab = CTask.TaskModeType.Task;

    private TaskTodayAdapter taskTodayAdapter;
    private TaskOptionalAdapter taskOptionalAdapter;
    private TaskCheckAdapter taskCheckAdapter;

    private ADataEntity selectedItem;

    private static final int STRING_DIALOG_MODE_TOPIC = 1;
    private static final int STRING_DIALOG_MODE_TASK_COMMENT = 2;
    private static final int STRING_DIALOG_MODE_TASK_NOTE = 3;

    @Override
    public void onTaskSave() {
        switch (taskTab) {
            case Task: {
                taskTodayAdapter.updateList(Environment.topicList.topic,datePointer);
                taskTodayAdapter.notifyDataSetChanged();
            } break;
            case Reminder: {
                taskOptionalAdapter.updateList(Environment.topicList.topic,datePointer);
                taskOptionalAdapter.notifyDataSetChanged();
            } break;
            case Check: {
                taskCheckAdapter.updateList(Environment.topicList.topic,datePointer);
                taskCheckAdapter.notifyDataSetChanged();
            } break;
        }
    }

    public void onEnter(String str, int mode) {

        switch (mode) {
            case STRING_DIALOG_MODE_TOPIC: {
                Environment.topicList.addTopic(str);
                updateView(getView());
            }; break;
            case STRING_DIALOG_MODE_TASK_COMMENT: {
                CTask task = (CTask) selectedItem;
                task.commentRegisterByPerid(datePointer,str);
                onTaskSave();
            }; break;
            case STRING_DIALOG_MODE_TASK_NOTE: {
                CNote note = new CNote(str,(CTask)selectedItem);
                note.saveMe();
                updateView(getView());
            }; break;
        }

    }

    // адаптер для списка задач на сегодня
    class TaskTodayAdapter extends ATaskListAdapter {

        public TaskTodayAdapter(LayoutInflater inflater) {
            super(inflater);
        }

        public void updateList(CTopic topic, DateManager period) {
            taskList = new LTask(topic._id,CTask.TaskModeType.Task);
        }

        public View getView(int position, View view, ViewGroup parent) {

            final CTask item = getItem(position);
            view = mInflater.inflate(R.layout.item_task, parent, false);

            final TextView titleView = (TextView) view.findViewById(R.id.reg_title);
            titleView.setText(item.title);

            final Button checkTaskButton = (Button) view.findViewById(R.id.check_task_button);

            final CRegister reg = item.findRegisterByPeriod(datePointer);
            if (reg != null) {
                checkTaskButton.setTextColor(Color.GREEN);
                titleView.setTextColor(Color.WHITE);
                TextView commentView = (TextView) view.findViewById(R.id.task_comment);
                commentView.setText(reg.comment);
            }

            checkTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.findRegisterByPeriod(datePointer) == null) {
                        CRegister.registerTask(item,datePointer);
                        checkTaskButton.setTextColor(Color.GREEN);
                        titleView.setTextColor(Color.WHITE);
                    }
                }
            });

            titleView.setTag(item);
            checkTaskButton.setTag(item);

            registerForContextMenu(titleView);
            registerForContextMenu(checkTaskButton);

            return view;

        }

    }

    // адаптер для списка дополнительных задач
    class TaskOptionalAdapter extends ATaskListAdapter {

        public TaskOptionalAdapter(LayoutInflater inflater) {
            super(inflater);
        }

        public void updateList(CTopic topic, DateManager period) {
            taskList = new LTask(topic._id,CTask.TaskModeType.Reminder);
        }

        public View getView(int position, View view, ViewGroup parent) {

            final CTask item = getItem(position);
            view = mInflater.inflate(R.layout.item_task, parent, false);

            TextView titleView = (TextView) view.findViewById(R.id.reg_title);
            titleView.setText(item.title);

//            Button deleteButton = (Button) view.findViewById(R.id.delete_button);
//
//            deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //timerProgram.deleteInterval(item);
//                }
//            });

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

    // адаптер для списка чеклиста
    class TaskCheckAdapter extends ATaskListAdapter {

        public TaskCheckAdapter(LayoutInflater inflater) {
            super(inflater);
         }

        public void updateList(CTopic topic, DateManager period) {
            taskList = new LTask(topic._id,CTask.TaskModeType.Check);
        }

        public View getView(int position, View view, ViewGroup parent) {

            final CTask item = getItem(position);
            view = mInflater.inflate(R.layout.item_task, parent, false);

            TextView titleView = (TextView) view.findViewById(R.id.reg_title);
            titleView.setText(item.title);

//            Button deleteButton = (Button) view.findViewById(R.id.delete_button);
//
//            deleteButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //timerProgram.deleteInterval(item);
//                }
//            });

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

    public FToday() {
        super();
        datePointer = new DateManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_today, container, false);

        Button dateLeftbutton = (Button) view.findViewById(R.id.date_left_button);
        dateLeftbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePointer.shiftLeft();
                updateView(view);
            }
        });

        Button dateRightbutton = (Button) view.findViewById(R.id.date_right_button);
        dateRightbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePointer.shiftRight();
                updateView(view);
            }
        });

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

        taskTodayAdapter = new TaskTodayAdapter(inflater);
        taskOptionalAdapter = new TaskOptionalAdapter(inflater);
        taskCheckAdapter = new TaskCheckAdapter(inflater);

        final Button tab1 = (Button) view.findViewById(R.id.tab1);
        final Button tab2 = (Button) view.findViewById(R.id.tab2);
        final Button tab3 = (Button) view.findViewById(R.id.tab3);

        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskTab = CTask.TaskModeType.Task;
                updateView(view);
            }
        });

        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskTab = CTask.TaskModeType.Reminder;
                updateView(view);
            }
        });

        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskTab = CTask.TaskModeType.Check;
                updateView(view);
            }
        });

        Button addTaskButton = (Button) view.findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTodayTask(view);
            }
        });

        Button taskManagerButton = (Button) view.findViewById(R.id.open_taskmanager_button);
        taskManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTaskManager();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.start_timer_button);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MainActivity context = (MainActivity) getActivity();
//                context.setContent(new FTimer());
//            }
//        });

        updateView(view);
        return view;

    }

    public void updateView(View view) {

        TextView dateText = (TextView) view.findViewById(R.id.dateText);
        dateText.setText(datePointer.toString(getActivity()));

        TextView topicText = (TextView) view.findViewById(R.id.topicText);
        topicText.setText(Environment.topicList.topic.title);

        ListView taskList = (ListView) view.findViewById(R.id.task_list);

        Button tab1 = (Button) view.findViewById(R.id.tab1);
        tab1.setSelected(false);
        Button tab2 = (Button) view.findViewById(R.id.tab2);
        tab2.setSelected(false);
        Button tab3 = (Button) view.findViewById(R.id.tab3);
        tab3.setSelected(false);

        switch (taskTab) {
            case Task: tab1.setSelected(true); break;
            case Reminder: tab2.setSelected(true); break;
            case Check: tab3.setSelected(true); break;
        }

        switch (taskTab) {
            case Task: {
                taskTodayAdapter.updateList(Environment.topicList.topic,datePointer);
                taskList.setAdapter(taskTodayAdapter);
            } break;
            case Reminder: {
                taskOptionalAdapter.updateList(Environment.topicList.topic,datePointer);
                taskList.setAdapter(taskOptionalAdapter);
            } break;
            case Check: {
                taskCheckAdapter.updateList(Environment.topicList.topic,datePointer);
                taskList.setAdapter(taskCheckAdapter);
            } break;
        }

    }

    public void addTodayTask(final View view) {
        CTaskDialog dialog = CTaskDialog.newInstance(taskTab,datePointer.period,getFragmentID());
        dialog.show(getFragmentManager(),"AddTask");
    }

    public void openTaskManager() {
        MainActivity context = (MainActivity) getActivity();
        context.setContent(new FTaskManager());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();

        if (view.getId() == R.id.reg_title) {
            selectedItem = (ADataEntity) view.getTag();
            inflater.inflate(R.menu.task_today_item_context, menu);
        } else if (view.getId() == R.id.check_task_button) {
            selectedItem = (ADataEntity) view.getTag();
            inflater.inflate(R.menu.task_today_check_context, menu);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_task_edit: {
                CTaskDialog dialog = CTaskDialog.newInstance(selectedItem._id, getFragmentID());
                dialog.show(getFragmentManager(), "EditTask");
            }; break;

            case R.id.menu_task_remove: {
                selectedItem.deleteMe();
                onTaskSave();
            }; break;

            case R.id.menu_task_timer: {
                MainActivity context = (MainActivity) getActivity();
                context.setContent(new FTimer());
            }; break;

            case R.id.menu_task_comment: {
                CStringDialog dialog = CStringDialog.newInstance("Комментарий выполения",STRING_DIALOG_MODE_TASK_COMMENT,getFragmentID());
                dialog.show(getFragmentManager(),"AddRegisterComment");
            }; break;

            case R.id.menu_task_cancel: {
                CTask task = (CTask) selectedItem;
                task.cancelRegisterByPeriod(datePointer);
                onTaskSave();
            }; break;

            case R.id.menu_task_note: {
                CStringDialog dialog = CStringDialog.newInstance("Добавить заметку",STRING_DIALOG_MODE_TASK_NOTE,getFragmentID());
                dialog.show(getFragmentManager(),"AddTaskNote");
            }; break;

        }

        return true;

    }


    public void selectTopic() {

//        CListDialog dialog = new CListDialog();
//        dialog.show(getFragmentManager(),"TestList");

    }


}
