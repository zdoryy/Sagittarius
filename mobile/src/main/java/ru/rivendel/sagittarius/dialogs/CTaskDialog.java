package ru.rivendel.sagittarius.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import ru.rivendel.sagittarius.DateManager;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.CTask;

/**
 * Created by user on 12.08.16.
 */

public class CTaskDialog extends DialogFragment {

    private CTask task;
    private CTask.OnSaveListener mListener;
    private TextView alarmText;

    final static int ON_ALARM_SAVE = 1;

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

        public static CTaskDialog newInstance(CTask.TaskPeriodType period, int lst) {
            CTaskDialog fragment = new CTaskDialog();
            Bundle args = new Bundle();
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
                CTask.TaskModeType mode = CTask.TaskModeType.Task;
                CTask.TaskPeriodType period = CTask.TaskPeriodType.values()[param.getInt("task_period", 0)];
                task = new CTask(Environment.topicList.topic._id, mode, period);
            } else {
                task = new CTask(_id_task);
            }
            int owner = param.getInt("listener");
            mListener = (CTask.OnSaveListener) getFragmentManager().findFragmentById(owner);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view  = inflater.inflate(R.layout.dialog_task, null);

            final EditText titleText = (EditText) view.findViewById(R.id.title_text);
            titleText.setText(task.title);

            final RadioGroup periodRadio = (RadioGroup) view.findViewById(R.id.period_radio_group);
            switch (task.period) {
                case Single: periodRadio.check(R.id.period_button_1); break;
                case Day: periodRadio.check(R.id.period_button_2); break;
                case Week: periodRadio.check(R.id.period_button_3); break;
                case Month: periodRadio.check(R.id.period_button_4); break;
            }

            builder.setView(view)
                    .setTitle("Задание");

            final CTaskDialog dialog = this;

            final CheckBox alarmCheck = (CheckBox) view.findViewById(R.id.alarm_check);
            alarmText = (TextView) view.findViewById(R.id.alarm_text);

            if (task.alarm != -1) {
                alarmCheck.setChecked(true);
                alarmText.setText("Уведомлять с " + DateManager.timeToString(DateManager.getStartTaskAlarmTime(task.time,task.alarm))+
                                  " по " + DateManager.timeToString(DateManager.getEndTaskAlarmTime(task.time,task.alarm)));
            } else {
                alarmCheck.setChecked(false);
                alarmText.setText("");
            }

            alarmCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        CAlarmDialog dlg = CAlarmDialog.newInstance(task);
                        dlg.setTargetFragment(dialog,ON_ALARM_SAVE);
                        dlg.show(getFragmentManager(), "AlarmSetup");
                    } else {
                        alarmText.setText("");
                        task.alarm = -1;
                    }

                }
            });

            final Button buttonOK = (Button) view.findViewById(R.id.btnOK);
            buttonOK.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    task.title = titleText.getText().toString();
                    switch (periodRadio.getCheckedRadioButtonId()) {
                        case R.id.period_button_1: task.period = CTask.TaskPeriodType.Single; break;
                        case R.id.period_button_2: task.period = CTask.TaskPeriodType.Day; break;
                        case R.id.period_button_3: task.period = CTask.TaskPeriodType.Week; break;
                        case R.id.period_button_4: task.period = CTask.TaskPeriodType.Month; break;
                    }
                    if (!alarmCheck.isChecked()) task.alarm = -1;
                    task.saveMe();
                    mListener.onTaskSave();
                    dialog.dismiss();
                }
            });

            final Button buttonCancel = (Button) view.findViewById(R.id.btnCancel);
            buttonCancel.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

           return builder.create();

        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ON_ALARM_SAVE:
                    task.time = data.getIntExtra("time",0);
                    task.alarm = data.getIntExtra("alarm",-1);
                    alarmText.setText("Уведомлять с " + DateManager.timeToString(DateManager.getStartTaskAlarmTime(task.time,task.alarm))+
                            " по " + DateManager.timeToString(DateManager.getEndTaskAlarmTime(task.time,task.alarm)));
                    break;
            }
        }
    }

}
