package ru.rivendel.sagittarius.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import ru.rivendel.sagittarius.AdvancedTimer;
import ru.rivendel.sagittarius.Environment;
import ru.rivendel.sagittarius.MainActivity;
import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.Settings;
import ru.rivendel.sagittarius.classes.CTask;
import ru.rivendel.sagittarius.classes.CTimerInterval;
import ru.rivendel.sagittarius.classes.CTimerProgram;
import ru.rivendel.sagittarius.dialogs.CTimerIntervalDialog;


public class FTimer extends CFragment implements AdvancedTimer.OnTimerQueueListener {

    private CTimerProgram timerProgram;
    private AdvancedTimer timerManager;
    private CTimerIntervalDialog dialog_interval;
    private IntervalListAdapter listAdapter;
    private ListView listView;
    private View view;
    private int curPos=-1;
    private int curPosRunning=-1;
    private long lCurrentRemaining=0;
    private Button startTimerButton;

    enum TTimerStatus {nop, edit_interval,add_interval};
    enum TTimerManagerStatus {tmStop, tmRunning};
    enum TMode {all_intervals,single_interval};

    TTimerStatus status=TTimerStatus.nop;
    TTimerManagerStatus tm_status = TTimerManagerStatus.tmStop;
    TMode tmode = TMode.all_intervals;

    // адаптер для списка интервалов
    class IntervalListAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;

        public IntervalListAdapter(LayoutInflater inflater) {
            super();
            mInflater = inflater;
        }

        public void displayElementsInView(final int position, View view)
        {
            final CTimerInterval item = getItem(position);
            TextView titleView = (TextView) view.findViewById(R.id.tii_title);
            titleView.setText(item.title);

            final TextView timeView = (TextView) view.findViewById(R.id.tii_timer);
            timeView.setText(timeToHHMMSS(item.time));


            TextView txtAdvanced = (TextView)view.findViewById(R.id.tii_advance);
            txtAdvanced.setText(String.format("%02d",item.advance));

            TextView txtWake = (TextView)view.findViewById(R.id.tii_waking);
            txtWake.setText(String.format("%02d",Math.abs(item.waking)));
        }

        public View getView(final int position, View view, ViewGroup parent) {

            view = mInflater.inflate(R.layout.item_timer_interval, parent, false);
            displayElementsInView(position,view);


            final Button btnPlus = (Button) view.findViewById(R.id.tii_btn_plus);
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    plusTime(position);
                }
            });

            final Button btnMinus = (Button) view.findViewById(R.id.tii_btn_minus);
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    minusTime(position);
                }
            });

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
    public FTimer() {
        super();
        timerProgram = new CTimerProgram(Settings.timerProgramID);
    }

    public static FTimer newInstance(CTask task)
    {
        FTimer timer = new FTimer();
        timer.timerProgram = new CTimerProgram(task);
        if (timer.timerProgram._id == 0) {
            timer.timerProgram._id_task = task._id;
            timer.timerProgram.saveMe();
            Settings.timerProgramID = timer.timerProgram._id;
            Settings.saveSettings();
        }
        return timer;
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

        startTimerButton= (Button) view.findViewById(R.id.start_timer_button);

        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // запускаем менеджер таймера
                startStopTimer();
            }
        });

        Button saveTimerButton = (Button) view.findViewById(R.id.save_button);

        saveTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTimer();
            }
        });

        newDialogTI();
//        dialog_interval  = new CTimerIntervalDialog();
//        dialog_interval.setTargetFragment(this, Environment.TI_REQUEST_INTERVAL);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_interval_button1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status=TTimerStatus.add_interval;
                dialog_interval.show(getFragmentManager(),"TimerIntervalDialog");
            }
        });

        timerManager = new AdvancedTimer(timerProgram,(MainActivity)getActivity());
        timerManager.setOnQueueEndListener(this);

//        instance = (MainActivity) getActivity();

        return view;
    }

    private void newDialogTI()
    {
        dialog_interval  = new CTimerIntervalDialog();
        dialog_interval.setTargetFragment(this, Environment.TI_REQUEST_INTERVAL);
    }
    private void toggleStartButton()
    {
        switch (tm_status)
        {
            case tmStop:
                startTimerButton.setText(getResources().getString(R.string.tii_start_button_text_start));
                break;
            case tmRunning:
                startTimerButton.setText(getResources().getString(R.string.tii_start_button_text_stop));
                break;
        }
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
                tmode = TMode.single_interval;
                curPosRunning = curPos;
                runTimerInterval(curPosRunning);
                break;
            case R.id.mnu_edit_interval:
                userWantEditInterval();
                break;
            case R.id.mnu_delete_interval:
                userWantDeleteInterval();
                break;
        }

        return false;
    }

    void userWantEditInterval()  {
        if (curPos!=curPosRunning) {
            status = TTimerStatus.edit_interval;
            CTimerInterval ti = timerProgram.ti.getList().get(curPos);
            String hh = String.format("%02d",ti.time/3600);
            String mm = String.format("%02d",(ti.time/60)%60);
            String ss = String.format("%02d",ti.time%60);
            String advance  = String.format("%02d",ti.advance);
            String rithm="00";
            String waking="00";
            if (ti.waking>0)
                rithm  = String.format("%02d",Math.abs(ti.waking));
            else
                waking  = String.format("%02d",ti.waking);
            newDialogTI();
            dialog_interval.setDialogData(hh,mm,ss,advance,waking,rithm);
            dialog_interval.show(getFragmentManager(),"TimerIntervalDialog");
        }
    }

    void userWantDeleteInterval()
    {
        if (curPos!=curPosRunning) {
            timerProgram.ti.getList().remove(curPos);
            listView.setAdapter(listAdapter);
            view.invalidate();
        }
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

    private void runTimerInterval(int position)
    {
        if (tm_status==TTimerManagerStatus.tmRunning)
            timerManager.stop();
        timerManager.setRunAllInterval(tmode==TMode.all_intervals);
        timerManager.setRunIntervalIndex(position);
        timerManager.run();
        tm_status =TTimerManagerStatus.tmRunning;
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
        status = TTimerStatus.nop;
    }

    public void startStopTimer() {
        timerManager.setRunIntervalIndex(0);
        if (tm_status ==TTimerManagerStatus.tmStop)
        {
            curPos=0;
            curPosRunning=0;
            tmode = TMode.all_intervals;
            tm_status=TTimerManagerStatus.tmRunning;
            timerManager.setRunAllInterval(true);
            timerManager.run();
        }
        else if (tm_status ==TTimerManagerStatus.tmRunning)
        {
            curPos=-1;
            curPosRunning=-1;
            tm_status=TTimerManagerStatus.tmStop;
            timerManager.stop();
        }
        toggleStartButton();
    }

    public void stopTimer()
    {
        if (tm_status ==TTimerManagerStatus.tmRunning)
        {
            curPos=-1;
            curPosRunning=-1;
            tm_status=TTimerManagerStatus.tmStop;
            timerManager.stop();
            toggleStartButton();
        }
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

    @Override
    public void onQueueEnd() {
        listAdapter.notifyDataSetChanged();
        tm_status =TTimerManagerStatus.tmStop;
        curPosRunning=-1;
    }

    @Override
    public void onTimerBegin(CTimerInterval ti) {

    }

    @Override
    public void onTimerEnd() {
        curPosRunning++;
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTimerTick(final long lRemaining) {
        lCurrentRemaining = lRemaining;
        if (curPosRunning<0) return;
        View itemView = listView.getChildAt(curPosRunning);
        final TextView tvTimer = (TextView) itemView.findViewById(R.id.tii_timer);
        final TextView tvStatus = (TextView) itemView.findViewById(R.id.tii_status);
        final LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.tii_layout);

        if (tvTimer!=null)
        {
            MainActivity instance = (MainActivity) getActivity();
            if (instance!=null)
            {
                instance.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTimer.setText(timeToHHMMSS((int)lRemaining));
                        Drawable draw =
                                lRemaining % 2 == 0 ?
                                        getResources().getDrawable(R.drawable.rect_status_green):
                                        getResources().getDrawable(R.drawable.rect_status_grey);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tvStatus.setBackground(draw);
                            layout.setBackground(getResources().getDrawable(R.drawable.dialog_back_running));
                        } else {
                            tvStatus.setBackgroundDrawable(draw);
                            layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.dialog_back_running));
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onTimerCancelled() {
        tm_status = TTimerManagerStatus.tmStop;
        listAdapter.notifyDataSetChanged();
    }

    private void plusTime(int position)
    {
        restartTimer(+1,position);
    }

    private void minusTime(int position)
    {
        restartTimer(-1,position);
    }
    private int getNewTime(int time, int sign, int secondsMax)
    {
        int seconds = secondsMax;
        int minimum = 60;
        int result=0;
        if (sign<0) {
            if (time<=seconds)
                result = (time-minimum)>0 ? time-minimum : time;
            else
                result = time - seconds;
        }
        else
            if (time<secondsMax) result=time+minimum;
            else result = time+seconds;
        return result;
    }
    private void restartTimer(int sign,int position)
    {
        int newTime = 0;
        int seconds = 300;
        CTimerInterval ti = timerProgram.ti.getList().get(position);
        if (curPosRunning!=position)
            newTime = getNewTime(ti.time,sign,seconds);
        else {
            newTime = getNewTime((int)lCurrentRemaining,sign,seconds);
        }
        editInterval(position, ti.title,newTime,ti.advance,ti.waking,ti.sound);
        if (tm_status==TTimerManagerStatus.tmRunning&&curPosRunning==position)
            runTimerInterval(curPosRunning);
        ti.saveMe();
        //updateViewInterval(position);
        listAdapter.notifyDataSetChanged();
    }

    private void updateViewInterval(int position)
    {
        View v = listView.getChildAt(position-listView.getFirstVisiblePosition());
        listAdapter.displayElementsInView(position,view);
    }
}
