package ru.rivendel.sagittarius.fragments;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import ru.rivendel.sagittarius.R;
import ru.rivendel.sagittarius.dialogs.CPicker3Dialog;
import ru.rivendel.sagittarius.dialogs.CPickerDialog;

/**
 * Created by elanse on 10.08.16.
 */
public class FTimerInterval extends CFragment  {
    CPicker3Dialog dialog3;
    CPickerDialog dialog;
    TextView txtHour;
    TextView txtMinutes;
    TextView txtSeconds;
    TextView txtAdvance;
    TextView txtWaking;
    TextView txtRithm;
    TextView currentControl=null;
    private static final int REQUEST_NUMBER = 1;
    private static final int REQUEST_3_NUMBER = 2;
    public FTimerInterval() {
        super();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//
//    @Override
//    public void onButtonPressed(Uri uri) {
//        super.onButtonPressed(uri);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_interval, container, false);
        View.OnClickListener onClickListener3=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog3For((TextView)view);
            }
        };
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogFor((TextView)view);
            }
        };
        dialog3 = new CPicker3Dialog();
        dialog3.setTargetFragment(this,REQUEST_3_NUMBER);

        dialog = new CPickerDialog();
        dialog.setTargetFragment(this,REQUEST_NUMBER);

        txtHour = (TextView)view.findViewById(R.id.txtHours);
        txtHour.setOnClickListener(onClickListener3);

        txtMinutes = (TextView)view.findViewById(R.id.txtMinutes);
        txtMinutes.setOnClickListener(onClickListener3);

        txtSeconds = (TextView)view.findViewById(R.id.txtSeconds);
        txtSeconds.setOnClickListener(onClickListener3);

        txtAdvance = (TextView)view.findViewById(R.id.txtAdvance);
        txtAdvance.setOnClickListener(onClickListener);

        txtWaking = (TextView)view.findViewById(R.id.txtWake);
        txtWaking.setOnClickListener(onClickListener);

        txtRithm = (TextView)view.findViewById(R.id.txtRithm);
        txtRithm.setOnClickListener(onClickListener);


        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode==REQUEST_3_NUMBER) {
                Integer number1 = data.getIntExtra(dialog3.NUMBER1, -1);
                Integer number2 = data.getIntExtra(dialog3.NUMBER2, -1);
                Integer number3 = data.getIntExtra(dialog3.NUMBER3, -1);
                txtHour.setText(number1.toString());
                txtMinutes.setText(number2.toString());
                txtSeconds.setText(number3.toString());

            }
            else if (requestCode==REQUEST_NUMBER)
            {
                if (currentControl!=null) {
                    Integer number = data.getIntExtra(dialog.NUMBER,-1);
                    currentControl.setText(number.toString());
                }
            }
        }
    }

    private void dialog3For(TextView textField)
    {
        dialog3.setNumbers(Integer.valueOf(txtHour.getText().toString()),
                Integer.valueOf(txtMinutes.getText().toString()),
                Integer.valueOf(txtSeconds.getText().toString()));
        dialog3.show(getFragmentManager(),"Picker3Dialog");
    }
    private void dialogFor(TextView textField)
    {
        currentControl = textField;
        dialog.setNumber(Integer.valueOf(textField.getText().toString()));
        dialog.show(getFragmentManager(),"PickerDialog");
    }
}
