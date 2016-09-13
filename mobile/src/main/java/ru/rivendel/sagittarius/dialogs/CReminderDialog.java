package ru.rivendel.sagittarius.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.CTask;

/**
 * Created by user on 12.09.16.
 */

public class CReminderDialog extends DialogFragment {

    private CTask task;
    private CTask.OnSaveListener mListener;

    float scaleX=1.8f;
    float scaleY=1.7f;

    public CReminderDialog() {
        super();
    }

    public static CReminderDialog newInstance(int _id, int lst) {
        CReminderDialog fragment = new CReminderDialog();
        Bundle args = new Bundle();
        args.putInt("_id_task", _id);
        args.putInt("listener", lst);
        fragment.setArguments(args);
        return fragment;
    }

    public static CReminderDialog newInstance(int lst) {
        CReminderDialog fragment = new CReminderDialog();
        Bundle args = new Bundle();
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
            CTask.TaskModeType mode = CTask.TaskModeType.Reminder;
            CTask.TaskPeriodType period = CTask.TaskPeriodType.Day;
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

        View view  = inflater.inflate(R.layout.dialog_reminder, null);

        final EditText titleText = (EditText) view.findViewById(R.id.title_text);
        titleText.setText(task.title);

        final EditText noteText = (EditText) view.findViewById(R.id.note_text);
        noteText.setText(task.getNote());

        final NumberPicker numPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        numPicker.setMaxValue(99);
        numPicker.setMinValue(1);
        numPicker.setScaleX(scaleX);
        numPicker.setScaleY(scaleY);
        numPicker.setValue(task.count);

        final CReminderDialog dialog = this;

        final Button buttonOK = (Button) view.findViewById(R.id.btnOK);
        buttonOK.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.title = titleText.getText().toString();
                task.count = numPicker.getValue();
                task.setNote(noteText.getText().toString());
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

        builder.setView(view)
                .setTitle("Задание")
        ;

        return builder.create();

    }

}
