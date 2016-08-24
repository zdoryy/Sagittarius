package ru.rivendel.sagittarius;

// ПРИВЕТ АНВАР

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.rivendel.sagittarius.classes.LTopic;
import ru.rivendel.sagittarius.fragments.CFragment;

public class MainActivity extends AppCompatActivity {

    MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        instance = this;
        super.onCreate(savedInstanceState);

        Environment.db = new Database(this);
        Settings.init(getPreferences(MODE_PRIVATE));
        Environment.topicList = new LTopic();

        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

//                setContent(new FTimer());


//                AdvancedTimer at = new AdvancedTimer(0,instance);
//                at.addTimerInterval(0,"timer1",1,45,"lalala.mp3",0,15);
//                at.addTimerInterval(0,"timer2",2,90,"lalala.mp3",-3,180);
//                at.addTimerInterval(0,"timer3",1,63,"lalala.mp3",5,7);
//
//                AdvancedTimer.OnTimerQueueListener lst = new AdvancedTimer.OnTimerQueueListener() {
//                    @Override
//                    public void onQueueEnd() {
//                        Log.d("myLogs","********  EVENT TIMER QUEUE END!!!");
//                    }
//                    @Override
//                    public void onTimerBegin(CTimerInterval ti) {
//                        Log.d("myLogs","********  EVENT TIMER BEGIN!!! + Timer title="+ti.title);
//                    }
//                    @Override
//                    public void onTimerEnd(CTimerInterval ti) {
//                        Log.d("myLogs","********  EVENT TIMER  END!!! + Timer title  = "+ti.title );
//                    }
//                };
//                at.setOnQueueEndListener(lst);
//                at.run();
//            }
//        });
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Environment.db.close();
    }

    public void setContent(CFragment fragment) {

        if (findViewById(R.id.fragment_container) != null) {

            //fragment.setArguments(getIntent().getExtras());

            FragmentTransaction manager = getFragmentManager().beginTransaction();

            manager.addToBackStack("main");

            manager.replace(R.id.fragment_container,fragment);
            manager.commit();

        }

      }

}
