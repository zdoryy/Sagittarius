package ru.rivendel.sagittarius;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_notification);

        EditText titleText = (EditText) findViewById(R.id.title_text);
        EditText noteText = (EditText) findViewById(R.id.note_text);

        Button okButton = (Button) findViewById(R.id.btnOK);
        Button setupButton = (Button) findViewById(R.id.btnSetup);

        Bundle param = getIntent().getExtras();

        final int id = param.getInt("id");

        titleText.setText(param.getString("title"));
        noteText.setText(param.getString("text"));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.cancel("alarm", id);
                finish(); // дописать регистрацию выполнения в БД
            }
        });

        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setupIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(setupIntent);
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.cancel("alarm", id);
                finish();
            }
        });

    }

}
