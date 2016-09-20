package ru.rivendel.sagittarius;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import ru.rivendel.sagittarius.classes.CRegister;
import ru.rivendel.sagittarius.classes.CTask;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_notification);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Sagittarius");
        wl.acquire();

        EditText titleText = (EditText) findViewById(R.id.title_text);
        EditText noteText = (EditText) findViewById(R.id.note_text);

        Button okButton = (Button) findViewById(R.id.btnOK);
        Button setupButton = (Button) findViewById(R.id.btnSetup);

        Bundle param = getIntent().getExtras();

        final int id = param.getInt("id");
        final String mode = param.getString("alarm");


        titleText.setText(param.getString("title"));
        noteText.setText(param.getString("text"));

        if (mode.equalsIgnoreCase("task")) {
            okButton.setText("ВЫПОЛНЕНО");
            setupButton.setText("НАПОМНИТЬ");
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode.equalsIgnoreCase("task")) {
                    CTask task = new CTask(id);
                    CRegister.registerTask(task,new DateManager());
                }
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.cancel("alarm", id);
                wl.release();
                finish(); // дописать регистрацию выполнения в БД
            }
        });

        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode.equalsIgnoreCase("task")) {

                } else {
                    Intent setupIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(setupIntent);
                }
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.cancel("alarm", id);
                wl.release();
                finish();
            }
        });

    }

}
