package ru.rivendel.sagittarius.fragments;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import ru.rivendel.sagittarius.AdvancedTimer;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.Settings;
import ru.rivendel.sagittarius.classes.CTimerInterval;
import ru.rivendel.sagittarius.classes.CTimerProgram;
import ru.rivendel.sagittarius.dialogs.CTimerIntervalDialog;

/**
 * Created by user on 06.08.16.
 */
public class FTimer extends CFragment {

    private CTimerProgram timerProgram;
    private AdvancedTimer timerManager;
    private CTimerIntervalDialog dialog;
    // адаптер для списка интервалов
    class IntervalListAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

        public IntervalListAdapter(LayoutInflater inflater) {
            super();
            mInflater = inflater;
        }

         public View getView(int position, View view, ViewGroup parent) {


             final CTimerInterval item = getItem(position);
             view = mInflater.inflate(R.layout.item_timer, parent, false);

             TextView titleView = (TextView) view.findViewById(R.id.interval_title);
             titleView.setText(item.title);

             final TextView timeView = (TextView) view.findViewById(R.id.interval_time);
             timeView.setText(Integer.toString(item.time)+" секунд");

             final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBar);
             seekBar.setMax(60*60);
             seekBar.setProgress(item.time);

              seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                 @Override
                 public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                     item.time = seekBar.getProgress();
                     timeView.setText(Integer.toString(item.time)+" секунд");
                 }

                 @Override
                 public void onStartTrackingTouch(SeekBar seekBar) {

                 }

                 @Override
                 public void onStopTrackingTouch(SeekBar seekBar) {

                 }
             });

             Button deleteButton = (Button) view.findViewById(R.id.delete_button);

             deleteButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                    //timerProgram.deleteInterval(item);
                 }
             });

             final CheckBox wakingFlag = (CheckBox) view.findViewById(R.id.waikingFlag);
             wakingFlag.setChecked(item.waking < 0);

             wakingFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                     if (flag) item.waking = -12;
                     else item.waking = 0;
                 }
             });

             final CheckBox advanceFlag = (CheckBox) view.findViewById(R.id.advanceFlag);
             advanceFlag.setChecked(item.advance > 0);

             advanceFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                     if (flag) item.advance = 60;
                     else item.advance = 0;
                 }
             });

             return view;

        }

        public int getCount() {
            return timerProgram.ti.size();
        }

        public CTimerInterval getItem(int position) {
            return timerProgram.ti.getList().get(position);
        }

        public long getItemId(int position) {
            return position;
        }

    }

    // создает новую программу по умолчанию с одним интервалом
    public FTimer() {
        super();
        timerProgram = new CTimerProgram(Settings.timerProgramID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timer, container, false);

        final IntervalListAdapter listAdapter = new IntervalListAdapter(inflater);
        final ListView listView = (ListView) view.findViewById(R.id.interval_list);
        listView.setAdapter(listAdapter);

        TextView titleView = (TextView) view.findViewById(R.id.titleText);
        titleView.setText(timerProgram.title);

//        Button addIntervalButton = (Button) view.findViewById(R.id.add_interval_button);
//
//        addIntervalButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // добавляем новый интервал в список
//                timerProgram.ti.getList().add(new CTimerInterval(timerProgram.ti));
//                listView.setAdapter(listAdapter);
//                view.invalidate();
//            }
//        });

        Button startTimerButton = (Button) view.findViewById(R.id.start_timer_button);

        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // запускаем менеджер таймера
                startTimer();
             }
        });

        Button saveTimerButton = (Button) view.findViewById(R.id.save_button);

        saveTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTimer();
            }
        });

        dialog  = new CTimerIntervalDialog();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_interval_button1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(getFragmentManager(),"PickerDialog");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
        }
    }


    public void startTimer() {
        // запускаем менеджер таймера
    }

    public void saveTimer() {
        timerProgram.saveMe();
        Settings.timerProgramID = timerProgram._id;
        Settings.saveSettings();
    }

}
