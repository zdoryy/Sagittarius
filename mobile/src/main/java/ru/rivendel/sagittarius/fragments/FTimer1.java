package ru.rivendel.sagittarius.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import ru.rivendel.sagittarius.AdvancedTimer;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.Settings;
import ru.rivendel.sagittarius.classes.CTimerInterval;
import ru.rivendel.sagittarius.classes.CTimerProgram;
import ru.rivendel.sagittarius.dialogs.CTimerIntervalDialog;


public class FTimer1 extends CFragment {
    private CTimerProgram timerProgram;
    private AdvancedTimer timerManager;
    private CTimerIntervalDialog dialog_interval;
    private IntervalListAdapter listAdapter;
    private ListView listView;
    private View view;
    private int curPos=-1;
    enum FTimerStatus {nop, edit_interval,add_interval};
    FTimerStatus status=FTimerStatus.nop;
    // адаптер для списка интервалов
    class IntervalListAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

        public IntervalListAdapter(LayoutInflater inflater) {
            super();
            mInflater = inflater;
        }

        public View getView(int position, View view, ViewGroup parent) {
            final CTimerInterval item = getItem(position);
            view = mInflater.inflate(R.layout.item_timer_interval, parent, false);

            TextView titleView = (TextView) view.findViewById(R.id.tii_title);
            titleView.setText(item.title);

            final TextView timeView = (TextView) view.findViewById(R.id.tii_timer);
            timeView.setText(timeToHHMMSS(item.time));


            TextView txtAdvanced = (TextView)view.findViewById(R.id.tii_advance);
            txtAdvanced.setText(String.format("%02d",item.advance));

            TextView txtWake = (TextView)view.findViewById(R.id.tii_waking);
            txtWake.setText(String.format("%02d",item.waking));

            registerForContextMenu(view);
            view.setTag(position);

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
    public FTimer1() {
        super();
        timerProgram = new CTimerProgram(Settings.timerProgramID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_timer, container, false);

        listAdapter= new IntervalListAdapter(inflater);
        listView= (ListView) view.findViewById(R.id.interval_list);
        listView.setAdapter(listAdapter);

        TextView titleView = (TextView) view.findViewById(R.id.titleText);
        titleView.setText(timerProgram.title);

        Button addIntervalButton = (Button) view.findViewById(R.id.add_interval_button);

        addIntervalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // добавляем новый интервал в список
                timerProgram.ti.getList().add(new CTimerInterval(timerProgram.ti));
                listView.setAdapter(listAdapter);
                view.invalidate();
            }
        });

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

        dialog_interval  = new CTimerIntervalDialog();
        dialog_interval.setTargetFragment(this, Environment.TI_REQUEST_INTERVAL);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_interval_button1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status=FTimerStatus.add_interval;
                dialog_interval.show(getFragmentManager(),"TimerIntervalDialog");
            }
        });


        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.ti_menu_context, menu);
        curPos = (int)v.getTag();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

//        AdapterView.AdapterContextMenuInfo info =
//                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//
//        int position = (int) info.id;

        switch(item.getItemId())
        {
            case R.id.mnu_run_interval:
                break;
            case R.id.mnu_edit_interval:
                status = FTimerStatus.edit_interval;
                dialog_interval.show(getFragmentManager(),"TimerIntervalDialog");
                break;
            case R.id.mnu_delete_interval:
                timerProgram.ti.getList().remove(curPos);
                listView.setAdapter(listAdapter);
                view.invalidate();
                break;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode== Environment.TI_REQUEST_INTERVAL)
            {
                addOrEditInterval(data);
                listView.setAdapter(listAdapter);
                view.invalidate();
            }
        }
    }

    private void addOrEditInterval(Intent data)
    {
        switch(status)
        {
            case nop: case add_interval:
                addInterval(data.getStringExtra(dialog_interval.retTITLE_INTERVAL),
                        data.getIntExtra(dialog_interval.retTIME,0),
                        data.getIntExtra(dialog_interval.retADVANCE,0),
                        data.getIntExtra(dialog_interval.retWAKING,0),
                        "");
                break;
            case edit_interval:
                editInterval(curPos,data.getStringExtra(dialog_interval.retTITLE_INTERVAL),
                        data.getIntExtra(dialog_interval.retTIME,0),
                        data.getIntExtra(dialog_interval.retADVANCE,0),
                        data.getIntExtra(dialog_interval.retWAKING,0),
                        "");
                break;
        }
        status = FTimerStatus.nop;
    }

    public void startTimer() {
        // запускаем менеджер таймера
    }

    public void saveTimer() {
        timerProgram.saveMe();
        Settings.timerProgramID = timerProgram._id;
        Settings.saveSettings();
    }

    private String timeToHHMMSS(int time)
    {
        return String.format("%02d:%02d:%02d",time/3600,(time/60)%60,time%60);
    }

    private int addInterval(String title, int time, int advance, int waking, String sound)
    {
        // добавляем новый интервал в список
        CTimerInterval ti = new CTimerInterval(timerProgram.ti);
        ti.title = title;
        ti.time = time;
        ti.advance = advance;
        ti.waking = waking;
        ti.sound= sound;
        timerProgram.ti.getList().add(ti);
        return 0;
    }
    private int editInterval(int pos, String title, int time, int advance, int waking, String sound)
    {
        // добавляем новый интервал в список
        CTimerInterval ti = timerProgram.ti.getList().get(pos);
        if (ti!=null)
        {
            ti.title = title;
            ti.time = time;
            ti.advance = advance;
            ti.waking = waking;
            ti.sound= sound;
        }

        return 0;
    }
}
