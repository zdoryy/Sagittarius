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

import ru.rivendel.sagittarius.R;

/**
 * Created by elanse on 11.08.16.
 */
public class CPicker3Dialog extends DialogFragment implements View.OnClickListener {
    View mainView;
    NumberPicker numPicker1;
    NumberPicker numPicker2;
    NumberPicker numPicker3;
    int number1=-1;
    int number2=-1;
    int number3=-1;
    float scaleX=1.2f;
    float scaleY=1.2f;
    public static final String NUMBER1 = "number3_1";
    public static final String NUMBER2 = "number2_2";
    public static final String NUMBER3 = "number3_3";
    public CPicker3Dialog(){super();}
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        mainView = inflater.inflate(R.layout.picker_3_dialog, null);
        mainView.findViewById(R.id.btnYES).setOnClickListener(this);
        mainView.findViewById(R.id.btnNO).setOnClickListener(this);

        numPicker1 = (NumberPicker) mainView.findViewById(R.id.numberPicker1);
        numPicker1.setMaxValue(59);
        numPicker1.setMinValue(0);
        numPicker1.setScaleX(scaleX);
        numPicker1.setScaleY(scaleY);
        if (number1>0) numPicker1.setValue(number1);

        numPicker2 = (NumberPicker) mainView.findViewById(R.id.numberPicker2);
        numPicker2.setMaxValue(59);
        numPicker2.setMinValue(0);
        numPicker2.setScaleX(scaleX);
        numPicker2.setScaleY(scaleY);
        if (number2>0) numPicker2.setValue(number2);

        numPicker3 = (NumberPicker) mainView.findViewById(R.id.numberPicker3);
        numPicker3.setMaxValue(59);
        numPicker3.setMinValue(0);
        numPicker3.setScaleX(scaleX);
        numPicker3.setScaleY(scaleY);
        if (number3>0) numPicker3.setValue(number3);

        return mainView;
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnYES) {
            Intent intent = new Intent();
            intent.putExtra(NUMBER1, numPicker1.getValue());
            intent.putExtra(NUMBER2, numPicker2.getValue());
            intent.putExtra(NUMBER3, numPicker3.getValue());
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        }
        dismiss();
    }
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d("myLogs", "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d("myLogs", "Dialog 1: onCancel");
    }
    public void setNumbers(int num1,int num2, int num3)
    {
        number1 = num1;
        number2 = num2;
        number3 = num3;
    }
}
