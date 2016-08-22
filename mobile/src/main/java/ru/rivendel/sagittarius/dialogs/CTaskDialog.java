package ru.rivendel.sagittarius.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.CTask;

/**
 * Created by user on 12.08.16.
 */

public class CTaskDialog extends DialogFragment {

    private CTask task;
    private OnSaveListener mListener;

        public interface OnSaveListener {
            void onTaskSave();
        }

        public CTaskDialog() {
            super();
        }

        public static CTaskDialog newInstance(int _id, int lst) {
            CTaskDialog fragment = new CTaskDialog();
            Bundle args = new Bundle();
            args.putInt("_id_task", _id);
            args.putInt("listener", lst);
            fragment.setArguments(args);
            return fragment;
        }

        public static CTaskDialog newInstance(CTask.TaskModeType mode, CTask.TaskPeriodType period, int lst) {
            CTaskDialog fragment = new CTaskDialog();
            Bundle args = new Bundle();
            args.putInt("task_mode",mode.ordinal());
            args.putInt("task_period",period.ordinal());
            args.putInt("listener", lst);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle param = getArguments();
            int _id_task = param.getInt("_id_task",0);
            if (_id_task == 0) {
                CTask.TaskModeType mode = CTask.TaskModeType.values()[param.getInt("task_mode", 0)];
                CTask.TaskPeriodType period = CTask.TaskPeriodType.values()[param.getInt("task_period", 0)];
                task = new CTask(Environment.topicList.topic._id, mode, period);
            } else {
                task = new CTask(_id_task);
            }
            int owner = param.getInt("listener");
            mListener = (OnSaveListener) getFragmentManager().findFragmentById(owner);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view  = inflater.inflate(R.layout.dialog_task, null);

            final EditText titleText = (EditText) view.findViewById(R.id.title_text);
            titleText.setText(task.title);

            final EditText countText = (EditText) view.findViewById(R.id.count_text);
            countText.setText(Integer.toString(task.count));

            final RadioGroup periodRadio = (RadioGroup) view.findViewById(R.id.period_radio_group);
            switch (task.period) {
                case Single: periodRadio.check(R.id.period_button_1); break;
                case Day: periodRadio.check(R.id.period_button_2); break;
                case Week: periodRadio.check(R.id.period_button_3); break;
                case Month: periodRadio.check(R.id.period_button_4); break;
            }

            final RadioGroup modeRadio = (RadioGroup) view.findViewById(R.id.mode_radio_group);
            switch (task.mode) {
                case Task: modeRadio.check(R.id.mode_button_1); break;
                case Reminder: modeRadio.check(R.id.mode_button_2); break;
                case Check: modeRadio.check(R.id.mode_button_3); break;
            }

            builder.setView(view)
                    .setTitle("Задание")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            task.title = titleText.getText().toString();
                            task.count = Integer.parseInt(countText.getText().toString());
                            switch (periodRadio.getCheckedRadioButtonId()) {
                                case R.id.period_button_1: task.period = CTask.TaskPeriodType.Single; break;
                                case R.id.period_button_2: task.period = CTask.TaskPeriodType.Day; break;
                                case R.id.period_button_3: task.period = CTask.TaskPeriodType.Week; break;
                                case R.id.period_button_4: task.period = CTask.TaskPeriodType.Month; break;
                            }
                            switch (modeRadio.getCheckedRadioButtonId()) {
                                case R.id.mode_button_1: task.mode = CTask.TaskModeType.Task; break;
                                case R.id.mode_button_2: task.mode = CTask.TaskModeType.Reminder; break;
                                case R.id.mode_button_3: task.mode = CTask.TaskModeType.Check; break;
                            }
                            task.saveMe();
                            mListener.onTaskSave();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();

        }

    }
