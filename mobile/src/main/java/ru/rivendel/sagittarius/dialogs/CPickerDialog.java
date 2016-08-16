package ru.rivendel.sagittarius.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import ru.rivendel.sagittarius.R;

/**
 * Created by elanse on 10.08.16.
 */
public class CPickerDialog extends DialogFragment implements View.OnClickListener {
    View mainView;
    NumberPicker numPicker;
    int number=-1;
    float scaleX=1.8f;
    float scaleY=1.7f;
    public static final String NUMBER = "number";
    public CPickerDialog() {super();}
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        mainView = inflater.inflate(R.layout.picker_dialog, null);
        mainView.findViewById(R.id.btnYES).setOnClickListener(this);
        mainView.findViewById(R.id.btnNO).setOnClickListener(this);
        numPicker = (NumberPicker) mainView.findViewById(R.id.numberPicker);
        numPicker.setMaxValue(59);
        numPicker.setMinValue(0);
        numPicker.setScaleX(scaleX);
        numPicker.setScaleY(scaleY);
        if (number>0) numPicker.setValue(number);
        return mainView;
    }
    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btnYES) {
            Intent intent = new Intent();
            intent.putExtra(NUMBER, numPicker.getValue());
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
    public void setNumber(int num)
    {
        number = num;
    }
}
