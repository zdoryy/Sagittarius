package ru.rivendel.sagittarius.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import ru.rivendel.sagittarius.DateManager;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.classes.CTask;

/**
 * Created by user on 19.09.16.
 */

public class CAlarmDialog extends DialogFragment implements View.OnClickListener {

    View mainView;
    NumberPicker numPicker;
    SeekBar spreadBar;
    TextView startText;
    TextView endText;

    int number=-1;
    float scaleX=1.8f;
    float scaleY=1.7f;

    private int time;
    private int alarm;
    private int count;

    public CAlarmDialog() {
        super();
    }

    public static CAlarmDialog newInstance(CTask task) {
        CAlarmDialog fragment = new CAlarmDialog();
        Bundle args = new Bundle();
        args.putInt("time",task.time);
        args.putInt("alarm",task.alarm);
        args.putInt("count",task.count);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle param = getArguments();
        time = param.getInt("time",0);
        alarm = param.getInt("alarm",0);
        count = param.getInt("count",0);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Title!");
        mainView = inflater.inflate(R.layout.alarm_dialog, null);

        startText = (TextView) mainView.findViewById(R.id.startText);
        endText = (TextView) mainView.findViewById(R.id.endText);

        mainView.findViewById(R.id.btnYES).setOnClickListener(this);
        mainView.findViewById(R.id.btnNO).setOnClickListener(this);

        numPicker = (NumberPicker) mainView.findViewById(R.id.numberPicker);
        numPicker.setMaxValue(21);
        numPicker.setMinValue(9);
        numPicker.setScaleX(scaleX);
        numPicker.setScaleY(scaleY);

        spreadBar = (SeekBar) mainView.findViewById(R.id.spreadBar);
        spreadBar.setMax(24);

        numPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                changeInterval();
            }
        });

        spreadBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                changeInterval();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            });

        if (time == 0) time = 9*60*60;
        if (alarm == -1) alarm = 1*60*60;

        numPicker.setValue(time/3600);
        spreadBar.setProgress(alarm/3600);

        changeInterval();

        return mainView;

    }

    private void changeInterval() {

        time = numPicker.getValue() * 60 * 60;
        alarm = spreadBar.getProgress() * 15 * 60;

        int start = DateManager.getStartTaskAlarmTime(time,alarm);
        int end = DateManager.getEndTaskAlarmTime(time,alarm);

        startText.setText("с " + DateManager.timeToString(start));
        endText.setText("по " + DateManager.timeToString(end));

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnYES) {
            Intent intent = new Intent();
            intent.putExtra("time", time);
            intent.putExtra("alarm", alarm);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        }
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Log.d("myLogs", "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        //Log.d("myLogs", "Dialog 1: onCancel");
    }

}
